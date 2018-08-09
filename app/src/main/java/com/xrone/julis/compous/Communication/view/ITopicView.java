package com.xrone.julis.compous.Communication.view;

import android.support.annotation.NonNull;

import com.xrone.julis.compous.Communication.Model.Reply;
import com.xrone.julis.compous.Communication.Model.TopicWithReply;


public interface ITopicView {

    void onGetTopicOk(@NonNull TopicWithReply topic);

    void onGetTopicFinish();

    void appendReplyAndUpdateViews(@NonNull Reply reply);

}
