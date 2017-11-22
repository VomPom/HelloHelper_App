package com.xrone.julis.compous.view.HomeFragment.communication.view;

import android.support.annotation.NonNull;


import com.xrone.julis.compous.view.HomeFragment.communication.Model.entity.Tab;
import com.xrone.julis.compous.view.HomeFragment.communication.Model.entity.Topic;

import java.util.List;

public interface IMainView {

    void onSwitchTabOk(@NonNull Tab tab);

    void onRefreshTopicListOk(@NonNull List<Topic> topicList);

    void onRefreshTopicListError(@NonNull String message);

    void onLoadMoreTopicListOk(@NonNull List<Topic> topicList);

    void onLoadMoreTopicListError(@NonNull String message);

    void updateUserInfoViews();

    void updateMessageCountViews(int count);

}
