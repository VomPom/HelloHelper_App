package com.xrone.julis.compous.view.HomeFragment;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xrone.julis.compous.R;
import com.xrone.julis.compous.model.Hello;
import com.xrone.julis.compous.view.HomeFragment.communication.Model.Tab;
import com.xrone.julis.compous.view.HomeFragment.communication.Model.Topic;
import com.xrone.julis.compous.view.HomeFragment.communication.Presenter.MainPresenter;
import com.xrone.julis.compous.view.HomeFragment.communication.adapter.TopicListAdapter;
import com.xrone.julis.compous.view.HomeFragment.communication.listener.FloatingActionButtonBehaviorListener;
import com.xrone.julis.compous.view.HomeFragment.communication.listener.IMainPresenter;
import com.xrone.julis.compous.view.HomeFragment.communication.view.IBackToContentTopView;
import com.xrone.julis.compous.view.HomeFragment.communication.view.IMainView;
import com.xrone.julis.compous.view.LoginAndRegister.LoginActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Julis on 17/6/11.
 * 首页页面
 */
public class HomeFragment extends Fragment  implements IMainView,SwipeRefreshLayout.OnRefreshListener,IBackToContentTopView {
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
    public void onDestroyView() {
        super.onDestroyView();

    }
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.com_activity_main,container,false);

        ButterKnife.bind(this, view);

        refreshLayout.setColorSchemeResources(R.color.bar_color);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setRefreshing(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new TopicListAdapter(getActivity());
        recyclerView.setAdapter(adapter);
        mainPresenter = new MainPresenter(getActivity(), this,adapter);

        recyclerView.addOnScrollListener(new FloatingActionButtonBehaviorListener.ForRecyclerView(fabCreateTopic));

        onRefresh();
        return view;
    }


    @OnClick(R.id.fab_create_topic)
    void onBtnCreateTopicClick() {
      if (LoginActivity.checkLogin(getActivity())){
            Intent intent = new Intent(getActivity(), ShareInformation.class);
            startActivity(intent);
      }
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














