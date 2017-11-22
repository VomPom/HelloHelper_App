package com.xrone.julis.compous.view.HomeFragment.communication.listener;

import android.support.annotation.NonNull;

import com.xrone.julis.compous.view.HomeFragment.communication.Model.entity.Tab;


public interface IMainPresenter {

    void switchTab(@NonNull Tab tab);

    void refreshTopicListAsyncTask();

    void loadMoreTopicListAsyncTask(int page);

    void getUserAsyncTask();

    void getMessageCountAsyncTask();

}
