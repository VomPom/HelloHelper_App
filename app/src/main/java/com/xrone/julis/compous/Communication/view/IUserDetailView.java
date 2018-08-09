package com.xrone.julis.compous.Communication.view;

import android.support.annotation.NonNull;


import com.xrone.julis.compous.Communication.Model.User;

public interface IUserDetailView {

    void onGetUserOk(@NonNull User user);

    void onGetUserError(@NonNull String message);

    void onGetUserStart();

    void onGetUserFinish();

}
