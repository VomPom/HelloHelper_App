package com.xrone.julis.compous.HomeFragment.communication.view;

import android.support.annotation.NonNull;


import com.xrone.julis.compous.HomeFragment.communication.Model.User;

public interface IUserDetailView {

    void onGetUserOk(@NonNull User user);

    void onGetUserError(@NonNull String message);

    void onGetUserStart();

    void onGetUserFinish();

}
