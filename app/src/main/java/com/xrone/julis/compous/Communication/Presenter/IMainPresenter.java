package com.xrone.julis.compous.Communication.Presenter;


public interface IMainPresenter {

   // void switchTab(@NonNull Tab tab);

    void refreshTopicListAsyncTask();

    void loadMoreTopicListAsyncTask(int page);

    void getUserAsyncTask();

    void getMessageCountAsyncTask();

}
