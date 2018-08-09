package com.xrone.julis.compous.Communication.Presenter;

import android.support.annotation.NonNull;


public interface ICreateTopicPresenter {

    void createTopicAsyncTask(@NonNull String tab, String title, String content);

}
