package com.amit.mvpexample.models;

import android.util.Log;

import com.amit.mvpexample.mvp.contracts.UserContract;
import com.amit.mvpexample.rest.ApiClient;
import com.amit.mvpexample.rest.ApiInterface;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// Created by AMIT JANGID on 22/02/21.
public class UserModel implements UserContract.Model
{
    private static final String TAG = UserModel.class.getSimpleName();
    private static final ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

    @Override
    public void getUsersListFromServer(int pageNo, OnUserResultListener onUserResultListener)
    {
        try
        {
            Call<User> userCall = apiService.getUserListApi(pageNo);
            userCall.enqueue(new Callback<User>()
            {
                @Override
                public void onResponse(@NotNull Call<User> call, @NotNull Response<User> userResponse)
                {
                    if (userResponse.body() != null && userResponse.code() == 200)
                    {
                        ArrayList<User> userArrayList = userResponse.body().getData();
                        onUserResultListener.onResponseSuccess(userArrayList);
                    }
                }

                @Override
                public void onFailure(@NotNull Call<User> call, @NotNull Throwable t)
                {
                    onUserResultListener.onResponseFailure();

                    Log.e(TAG, "onFailure: exception while getting data from server");
                    t.printStackTrace();
                }
            });
        }
        catch (Exception e)
        {
            onUserResultListener.onResponseFailure();

            Log.e(TAG, "UserController: exception while making api call for getting user list");
            e.printStackTrace();
        }
    }
}
