package com.xrone.julis.compous.HomeFragment;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xrone.julis.compous.R;
import com.xrone.julis.compous.Communication.CreateTopicActivity;
import com.xrone.julis.compous.Communication.HeaderAndFooterRecyclerView.HeaderAndFooterRecyclerView;
import com.xrone.julis.compous.Communication.Model.Topic;
import com.xrone.julis.compous.Communication.Presenter.MainPresenter;
import com.xrone.julis.compous.Communication.adapter.TopicListAdapter;
import com.xrone.julis.compous.Communication.listener.FloatingActionButtonBehaviorListener;
import com.xrone.julis.compous.Communication.Presenter.IMainPresenter;
import com.xrone.julis.compous.Communication.util.ToastUtils;
import com.xrone.julis.compous.Communication.view.IBackToContentTopView;
import com.xrone.julis.compous.Communication.view.IMainView;
import com.xrone.julis.compous.Communication.view.LoadMoreFooter;
import com.xrone.julis.compous.LoginAndRegister.LoginActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Julis on 17/6/11.
 * 首页页面
 */
public class HomeFragment extends Fragment  implements LoadMoreFooter.OnLoadMoreListener ,IMainView,SwipeRefreshLayout.OnRefreshListener,IBackToContentTopView {
    @BindView(R.id.recycler_view)
    HeaderAndFooterRecyclerView recyclerView;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;
    private TopicListAdapter adapter;
    private IMainPresenter mainPresenter;
    private int page = 0;

    @BindView(R.id.fab_create_topic)
    FloatingActionButton fabCreateTopic;
    private LoadMoreFooter loadMoreFooter;

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
        loadMoreFooter = new LoadMoreFooter(getActivity(), recyclerView, this);



        onRefresh();
        return view;
    }


    @OnClick(R.id.fab_create_topic)
    void onBtnCreateTopicClick() {
      if (LoginActivity.checkLogin(getActivity())){
          Intent intent = new Intent(getActivity(), CreateTopicActivity.class);
          startActivity(intent);
      }
    }



//    @Override
//    public void onSwitchTabOk(@NonNull Tab tab) {
//        fabCreateTopic.show();
//        loadMoreFooter.setState(LoadMoreFooter.STATE_DISABLED);
//
//    }

    /**
     * 刷新成功后，获取到新List列表
     * @param topicList
     */

    @Override
    public void onRefreshTopicListOk(@NonNull List<Topic> topicList) {

        page = 1;
        adapter.setTopicListWithNotify(topicList);
        refreshLayout.setRefreshing(false);
        loadMoreFooter.setState(topicList.isEmpty() ? LoadMoreFooter.STATE_DISABLED : LoadMoreFooter.STATE_ENDLESS);

    }

    @Override
    public void onRefreshTopicListError(@NonNull String message) {
        ToastUtils.with(getActivity()).show(message);
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void onLoadMoreTopicListOk(@NonNull List<Topic> topicList) {
        Log.e("topickList", String.valueOf(topicList.size()));
        page++;
        adapter.appendTopicListWithNotify(topicList);
        if (topicList.isEmpty()) {
            loadMoreFooter.setState(LoadMoreFooter.STATE_FINISHED);
        } else {
            loadMoreFooter.setState(LoadMoreFooter.STATE_ENDLESS);
        }
    }

    @Override
    public void onLoadMoreTopicListError(@NonNull String message) {
        //  ToastUtils.with(this).show(message);
        loadMoreFooter.setState(LoadMoreFooter.STATE_FAILED);
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

    public void onLoadMore() {
        mainPresenter.loadMoreTopicListAsyncTask(page + 1);
    }
}














