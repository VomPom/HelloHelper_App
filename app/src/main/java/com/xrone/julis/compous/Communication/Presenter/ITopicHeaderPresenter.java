package com.xrone.julis.compous.Communication.Presenter;

import android.support.annotation.NonNull;

public interface ITopicHeaderPresenter {

    void collectTopicAsyncTask(@NonNull String topicId);

    void decollectTopicAsyncTask(@NonNull String topicId);

    interface IUserDetailPresenter {

        void getUserAsyncTask(@NonNull String loginName);

    }
}
