package com.xrone.julis.compous.Communication.view;

import android.support.annotation.NonNull;

public interface ICreateTopicView {

    void onTitleError(@NonNull String message);

    void onContentError(@NonNull String message);

    void onCreateTopicOk();

    void onCreateTopicStart();

    void onCreateTopicFinish();

}
