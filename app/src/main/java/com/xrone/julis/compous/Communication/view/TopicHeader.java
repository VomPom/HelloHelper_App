package com.xrone.julis.compous.Communication.view;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xrone.julis.compous.R;
import com.xrone.julis.compous.Communication.HeaderAndFooterRecyclerView.HeaderAndFooterRecyclerView;
import com.xrone.julis.compous.Communication.Model.Topic;
import com.xrone.julis.compous.Communication.Model.TopicWithReply;
import com.xrone.julis.compous.Communication.Presenter.ITopicHeaderPresenter;
import com.xrone.julis.compous.Communication.Presenter.TopicHeaderPresenter;
import com.xrone.julis.compous.Communication.UserDetailActivity;
import com.xrone.julis.compous.Communication.util.FormatUtils;
import com.xrone.julis.compous.Communication.widget.ContentWebView;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TopicHeader implements ITopicHeaderView {

    @BindView(R.id.layout_content)
    ViewGroup layoutContent;

    @BindView(R.id.icon_good)
    View iconGood;

    @BindView(R.id.tv_title)
    TextView tvTitle;

    @BindView(R.id.img_avatar)
    ImageView imgAvatar;

    @BindView(R.id.ctv_tab)
    CheckedTextView ctvTab;

    @BindView(R.id.tv_login_name)
    TextView tvLoginName;

    @BindView(R.id.tv_create_time)
    TextView tvCreateTime;

    @BindView(R.id.tv_visit_count)
    TextView tvVisitCount;

    @BindView(R.id.btn_favorite)
    ImageView btnFavorite;

    @BindView(R.id.web_content)
    ContentWebView webContent;

    @BindView(R.id.layout_no_reply)
    ViewGroup layoutNoReply;

    @BindView(R.id.layout_reply_count)
    ViewGroup layoutReplyCount;

    @BindView(R.id.tv_reply_count)
    TextView tvReplyCount;

    private final Activity activity;
    private Topic topic;
    private boolean isCollect;

    private final ITopicHeaderPresenter topicHeaderPresenter;

    public TopicHeader(@NonNull Activity activity, @NonNull HeaderAndFooterRecyclerView recyclerView) {
        this.activity = activity;
        View headerView = LayoutInflater.from(activity).inflate(R.layout.com_header_topic,recyclerView.getHeaderContainer() , false);
        recyclerView.addHeaderView(headerView);

        ButterKnife.bind(this, headerView);
        this.topicHeaderPresenter = new TopicHeaderPresenter(activity, this);
    }

    @OnClick(R.id.img_avatar)
    void onBtnAvatarClick() {
        UserDetailActivity.startWithTransitionAnimation(activity, topic.getAuthor().getLoginName(), imgAvatar, topic.getAuthor().getAvatarUrl());
    }

    public void updateViews(@Nullable Topic topic, boolean isCollect, int replyCount) {
        this.topic = topic;
        this.isCollect = isCollect;
        if (topic != null) {
            layoutContent.setVisibility(View.VISIBLE);
            iconGood.setVisibility(topic.isGood() ? View.VISIBLE : View.GONE);

            tvTitle.setText(topic.getTitle());
            Glide.with(activity)
                    .load(topic.getAuthor()
                            .getAvatarUrl())
                    .into(imgAvatar);
          //  ctvTab.setText(topic.isTop() ? R.string.tab_top : topic.getTab().getNameId());
            ctvTab.setText(topic.getType());
            ctvTab.setChecked(topic.isTop());
            tvLoginName.setText(topic.getAuthor().getLoginName());
            tvCreateTime.setText(activity.getString(R.string.__create, FormatUtils.getRelativeTimeSpanString(topic.getCreateAt())));
            tvVisitCount.setText(activity.getString(R.string.__count_visit, topic.getVisitCount()));
            //btnFavorite.setImageResource(isCollect ? R.drawable.ic_favorite_theme_24dp : R.drawable.ic_favorite_outline_grey600_24dp);

            // 这里直接使用WebView，有性能问题
            webContent.loadRenderedContent(topic.getContentHtml());

            updateReplyCount(replyCount);
        } else {
            layoutContent.setVisibility(View.GONE);
            iconGood.setVisibility(View.GONE);
        }
    }

    public void updateViews(@NonNull TopicWithReply topic) {
        updateViews(topic, topic.isCollect(), topic.getReplyList().size());
    }

    public void updateReplyCount(int replyCount) {
        layoutNoReply.setVisibility(replyCount > 0 ? View.GONE : View.VISIBLE);
        layoutReplyCount.setVisibility(replyCount > 0 ? View.VISIBLE : View.GONE);
        tvReplyCount.setText(activity.getString(R.string.__count_reply, replyCount));
    }

    @Override
    public void onCollectTopicOk() {
        isCollect = true;
        btnFavorite.setImageResource(R.drawable.com_arrow_back_white_24dp);
    }

    @Override
    public void onDecollectTopicOk() {
        isCollect = false;
        btnFavorite.setImageResource(R.drawable.com_arrow_back_white_24dp);
    }

    @OnClick(R.id.btn_favorite)
    void onBtnFavorite() {

        Drawable drawable =activity.getResources().getDrawable(R.drawable.com_favorite_theme_24dp);


            btnFavorite.setImageDrawable(drawable);
            Snackbar.make(btnFavorite, "Collect Success!/收藏成功" +
                    "", Snackbar.LENGTH_SHORT).show();


    }
}
