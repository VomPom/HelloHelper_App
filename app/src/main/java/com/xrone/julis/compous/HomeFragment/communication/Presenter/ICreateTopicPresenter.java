package com.xrone.julis.compous.HomeFragment.communication.Presenter;

import android.support.annotation.NonNull;


public interface ICreateTopicPresenter {

    void createTopicAsyncTask(@NonNull String tab, String title, String content);

}
