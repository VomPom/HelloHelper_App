package com.xrone.julis.compous.Communication.view;

import android.support.annotation.NonNull;

import com.xrone.julis.compous.Communication.Model.Reply;


public interface ICreateReplyView {

    void showWindow();

    void dismissWindow();

    void onAt(@NonNull Reply target, @NonNull Integer targetPosition);

    void onContentError(@NonNull String message);

    void onReplyTopicOk(@NonNull Reply reply);


}
