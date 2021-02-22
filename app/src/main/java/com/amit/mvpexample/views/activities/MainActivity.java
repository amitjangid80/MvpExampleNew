package com.amit.mvpexample.views.activities;

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

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // calling init activity
        initActivity();
    }

    private void initActivity()
    {
        try
        {
            rvUsersList = findViewById(R.id.rvUsersList);
            userPresenter = new UserPresenter(MainActivity.this);
            userPresenter.requestUsersListFromServer(1);
        }
        catch (Exception e)
        {
            Log.e(TAG, "initActivity: exception while initializing activity");
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

            UserListAdapter userListAdapter = new UserListAdapter(userArrayList);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this, RecyclerView.VERTICAL, false);

            rvUsersList.setLayoutManager(linearLayoutManager);
            rvUsersList.setAdapter(userListAdapter);
            rvUsersList.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        userPresenter.onDestroy();
    }
}
