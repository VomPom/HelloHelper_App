package com.xrone.julis.compous.HomeFragment.communication.listener;


public interface IMainPresenter {

   // void switchTab(@NonNull Tab tab);

    void refreshTopicListAsyncTask();

    void loadMoreTopicListAsyncTask(int page);

    void getUserAsyncTask();

    void getMessageCountAsyncTask();

}
