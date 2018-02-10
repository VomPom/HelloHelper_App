package com.xrone.julis.compous.HomeFragment.communication.view;

import android.support.annotation.NonNull;

public interface ICreateReplyPresenter {

    void createReplyAsyncTask(@NonNull String topicId, String content, String targetId);

}
