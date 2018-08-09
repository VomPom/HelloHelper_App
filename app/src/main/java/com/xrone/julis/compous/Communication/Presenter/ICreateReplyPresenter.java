package com.xrone.julis.compous.Communication.Presenter;

import android.support.annotation.NonNull;

public interface ICreateReplyPresenter {

    void createReplyAsyncTask(@NonNull String topicId, String content, String targetId);

}
