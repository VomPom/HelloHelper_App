package com.xrone.julis.compous.view.HomeFragment.communication.view;

import android.support.annotation.NonNull;

import com.xrone.julis.compous.view.HomeFragment.communication.Model.Reply;
import com.xrone.julis.compous.view.HomeFragment.communication.Model.entity.TopicWithReply;


public interface ITopicView {

    void onGetTopicOk(@NonNull TopicWithReply topic);

    void onGetTopicFinish();

    void appendReplyAndUpdateViews(@NonNull Reply reply);

}
