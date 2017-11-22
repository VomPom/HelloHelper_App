package com.xrone.julis.compous.view.HomeFragment.communication;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;


import com.xrone.julis.compous.R;
import com.xrone.julis.compous.view.HomeFragment.communication.Presenter.MainPresenter;
import com.xrone.julis.compous.view.HomeFragment.communication.adapter.TopicListAdapter;
import com.xrone.julis.compous.view.HomeFragment.communication.listener.FloatingActionButtonBehaviorListener;
import com.xrone.julis.compous.view.HomeFragment.communication.view.IBackToContentTopView;
import com.xrone.julis.compous.view.HomeFragment.communication.Model.entity.Tab;
import com.xrone.julis.compous.view.HomeFragment.communication.Model.entity.Topic;
import com.xrone.julis.compous.view.HomeFragment.communication.listener.IMainPresenter;
import com.xrone.julis.compous.view.HomeFragment.communication.view.IMainView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by Julis on 2017/11/15.
 */


public class CommunicationActivity extends Activity implements IMainView,SwipeRefreshLayout.OnRefreshListener,IBackToContentTopView {
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;
    private TopicListAdapter adapter;
    private IMainPresenter mainPresenter;
    private int page = 0;
    @BindView(R.id.fab_create_topic)
    FloatingActionButton fabCreateTopic;
    //private LoadMoreFooter loadMoreFooter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.com_activity_main);
        ButterKnife.bind(this);

        
        refreshLayout.setColorSchemeResources(R.color.bar_color);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setRefreshing(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TopicListAdapter(this);
        recyclerView.setAdapter(adapter);
        mainPresenter = new MainPresenter(this, this,adapter);

         recyclerView.addOnScrollListener(new FloatingActionButtonBehaviorListener.ForRecyclerView(fabCreateTopic));

        onRefresh();
    }




    @Override
    public void onSwitchTabOk(@NonNull Tab tab) {
        fabCreateTopic.show();
    }

    /**
     * 刷新成功后，获取到新List列表
     * @param topicList
     */
    @Override
    public void onRefreshTopicListOk(@NonNull List<Topic> topicList) {
        page = 1;
        adapter.setTopicListWithNotify(topicList);
        refreshLayout.setRefreshing(false);
        //loadMoreFooter.setState(topicList.isEmpty() ? LoadMoreFooter.STATE_DISABLED : LoadMoreFooter.STATE_ENDLESS);

    }

    @Override
    public void onRefreshTopicListError(@NonNull String message) {

    }

    @Override
    public void onLoadMoreTopicListOk(@NonNull List<Topic> topicList) {

    }

    @Override
    public void onLoadMoreTopicListError(@NonNull String message) {

    }

    @Override
    public void updateUserInfoViews() {

    }

    @Override
    public void updateMessageCountViews(int count) {

    }

    @Override
    public void onRefresh() {
        mainPresenter.refreshTopicListAsyncTask();
    }

    @Override
    public void backToContentTop() {
        recyclerView.scrollToPosition(0);
        fabCreateTopic.show();
    }
}


