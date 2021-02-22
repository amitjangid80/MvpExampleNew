package com.amit.mvpexample.mvp.presenter;

import com.amit.mvpexample.mvp.contracts.UserContract;
import com.amit.mvpexample.models.User;
import com.amit.mvpexample.models.UserModel;

import java.util.ArrayList;

// Created by AMIT JANGID on 22/02/21.
public class UserPresenter implements UserContract.Presenter, UserContract.Model.OnUserResultListener
{
    private UserContract.View userContractView;
    private final UserContract.Model userContractModel;

    public UserPresenter(UserContract.View userContractView)
    {
        this.userContractView = userContractView;
        this.userContractModel = new UserModel();
    }

    @Override
    public void onResponseFailure()
    {
        userContractView.onResponseFailure();
        userContractView.hideProgress();
    }

    @Override
    public void onResponseSuccess(ArrayList<User> userArrayList)
    {
        userContractView.showUsersList(userArrayList);
        userContractView.hideProgress();
    }

    @Override
    public void onDestroy()
    {
        userContractView = null;
    }

    @Override
    public void requestUsersListFromServer(int pageNo)
    {
        userContractView.showProgress();
        userContractModel.getUsersListFromServer(pageNo, this);
    }
}
