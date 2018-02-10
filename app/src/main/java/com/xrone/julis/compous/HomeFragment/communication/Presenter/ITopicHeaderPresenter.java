package com.xrone.julis.compous.HomeFragment.communication.Presenter;

import android.support.annotation.NonNull;

public interface ITopicHeaderPresenter {

    void collectTopicAsyncTask(@NonNull String topicId);

    void decollectTopicAsyncTask(@NonNull String topicId);

}
