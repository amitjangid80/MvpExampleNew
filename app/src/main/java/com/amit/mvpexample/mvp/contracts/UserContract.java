package com.amit.mvpexample.mvp.contracts;

import com.amit.mvpexample.models.User;

import java.util.ArrayList;

// Created by AMIT JANGID on 22/02/21.
public interface UserContract
{
    interface Model
    {
        interface OnUserResultListener
        {
            void onResponseFailure();

            void onResponseSuccess(ArrayList<User> userArrayList);
        }

        void getUsersListFromServer(int pageNo, OnUserResultListener onUserResultListener);
    }

    interface View
    {
        void showProgress();

        void hideProgress();

        void showUsersList(ArrayList<User> userArrayList);

        void onResponseFailure();
    }

    interface Presenter
    {
        void onDestroy();

        void requestUsersListFromServer(int pageNo);
    }
}
