package com.amit.mvpexample.views.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.amit.mvpexample.R;
import com.amit.mvpexample.mvp.contracts.UserContract;
import com.amit.mvpexample.models.User;
import com.amit.mvpexample.mvp.presenter.UserPresenter;
import com.amit.mvpexample.views.adapters.UserListAdapter;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements UserContract.View
{
    private static final String TAG = MainActivity.class.getSimpleName();

    private RecyclerView rvUsersList;
    private UserPresenter userPresenter;
    private ProgressDialog progressDialog;
    private UserListAdapter userListAdapter;
    private LinearLayoutManager linearLayoutManager;

    private ArrayList<User> mUserArrayList;

    private boolean isLoading = true;
    private final int visibleThreshold = 6;
    private int pageNo = 1, previousTotal = 0, firstVisibleItem, visibleItemCount, totalItemCount;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // calling init activity
        initActivity();

        // calling event listener method
        eventListener();
    }

    private void initActivity()
    {
        try
        {
            mUserArrayList = new ArrayList<>();
            rvUsersList = findViewById(R.id.rvUsersList);
            userPresenter = new UserPresenter(MainActivity.this);
            userPresenter.requestUsersListFromServer(pageNo);

            userListAdapter = new UserListAdapter(mUserArrayList);
            linearLayoutManager = new LinearLayoutManager(MainActivity.this, RecyclerView.VERTICAL, false);

            rvUsersList.setLayoutManager(linearLayoutManager);
            rvUsersList.setAdapter(userListAdapter);
        }
        catch (Exception e)
        {
            Log.e(TAG, "initActivity: exception while initializing activity");
        }
    }

    private void eventListener()
    {
        try
        {
            rvUsersList.addOnScrollListener(new RecyclerView.OnScrollListener()
            {
                @Override
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState)
                {
                    super.onScrollStateChanged(recyclerView, newState);
                }

                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy)
                {
                    super.onScrolled(recyclerView, dx, dy);

                    visibleItemCount = rvUsersList.getChildCount();
                    totalItemCount = linearLayoutManager.getItemCount();
                    firstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition();

                    // Handling the infinite scroll
                    if (isLoading)
                    {
                        if (totalItemCount > previousTotal)
                        {
                            isLoading = false;
                            previousTotal = totalItemCount;
                        }
                    }

                    if (!isLoading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold))
                    {
                        userPresenter.requestUsersListFromServer(pageNo);
                        isLoading = true;
                    }
                }
            });
        }
        catch (Exception e)
        {
            Log.e(TAG, "eventListener: exception while setting recycler view scroll listener:\n");
            e.printStackTrace();
        }
    }

    @Override
    public void showProgress()
    {
        progressDialog = ProgressDialog.show(MainActivity.this, null, getResources().getString(R.string.app_name), false, false);
    }

    @Override
    public void hideProgress()
    {
        if (progressDialog != null)
        {
            progressDialog.hide();
        }
    }

    @Override
    public void onResponseFailure()
    {
        rvUsersList.setVisibility(View.GONE);
    }

    @Override
    public void showUsersList(ArrayList<User> userArrayList)
    {
        if (userArrayList != null && userArrayList.size() > 0)
        {
            Log.e(TAG, "showUsersList: size of users array list is: " + userArrayList.size());

            mUserArrayList.addAll(userArrayList);
            userListAdapter.notifyDataSetChanged();

            pageNo++;
        }
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        userPresenter.onDestroy();
    }
}
