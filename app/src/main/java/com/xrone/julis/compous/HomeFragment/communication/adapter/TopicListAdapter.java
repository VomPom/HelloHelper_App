package com.xrone.julis.compous.HomeFragment.communication.adapter;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xrone.julis.compous.R;
import com.xrone.julis.compous.HomeFragment.communication.UserDetailActivity;
import com.xrone.julis.compous.HomeFragment.communication.util.Navigator;
import com.xrone.julis.compous.HomeFragment.communication.Model.Topic;
import com.xrone.julis.compous.HomeFragment.communication.util.FormatUtils;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TopicListAdapter extends RecyclerView.Adapter<TopicListAdapter.ViewHolder> {

    private final Activity activity;
    private final LayoutInflater inflater;
    private final List<Topic> topicList = new ArrayList<>();


    public TopicListAdapter(@NonNull Activity activity) {
        this.activity = activity;
        inflater = LayoutInflater.from(activity);
    }

    public void clearTopicListWithNotify() {
        topicList.clear();
        notifyDataSetChanged();
    }

    public void setTopicListWithNotify(@NonNull List<Topic> topicList) {
        this.topicList.clear();
        this.topicList.addAll(topicList);
        notifyDataSetChanged();
    }

    public void appendTopicListWithNotify(@NonNull List<Topic> topicList) {
        int startPosition = this.topicList.size();
        this.topicList.addAll(topicList);
        notifyItemRangeInserted(startPosition, topicList.size());
    }

    @Override
    public int getItemCount() {
        return topicList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.com_item_topic, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.update(topicList.get(position));
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ctv_tab)
        CheckedTextView ctvTab;

        @BindView(R.id.tv_reply_count)
        TextView tvReplyCount;

        @BindView(R.id.tv_visit_count)
        TextView tvVisitCount;

        @BindView(R.id.tv_last_reply_time)
        TextView tvLastReplyTime;

        @BindView(R.id.tv_title)
        TextView tvTitle;

        @BindView(R.id.tv_summary)
        TextView tvSummary;

        @BindView(R.id.img_avatar)
        ImageView imgAvatar;

        @BindView(R.id.tv_author)
        TextView tvAuthor;

        @BindView(R.id.tv_create_time)
        TextView tvCreateTime;

        @BindView(R.id.icon_good)
        View iconGood;

        private Topic topic;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void update(@NonNull Topic topic) {
            this.topic = topic;

            // ctvTab.setText(topic.isTop() ? R.string.tab_top : topic.getTab().getNameId());
            ctvTab.setText(topic.getType());
            ctvTab.setChecked(topic.isTop());
            tvReplyCount.setText(String.valueOf(topic.getReplyCount()));
            tvVisitCount.setText(String.valueOf(topic.getVisitCount()));
            tvLastReplyTime.setText(FormatUtils.getRelativeTimeSpanString(topic.getLastReplyAt()));
            tvTitle.setText(topic.getTitle());
            tvSummary.setText(topic.getContentSummary());

            Glide.with(activity)
                    .load(topic.getAuthor()
                            .getAvatarUrl())
                    .into(imgAvatar);


            tvAuthor.setText(topic.getAuthor().getLoginName());
            tvCreateTime.setText(activity.getString(R.string.create_at__, topic.getCreateAt().toString("yyyy-MM-dd HH:mm:ss")));
            iconGood.setVisibility(topic.isGood() ? View.VISIBLE : View.GONE);
        }

        @OnClick(R.id.btn_topic)
        void onBtnTopicClick() {

            Navigator.TopicWithAutoCompat.start(activity, topic);
        }



        @OnClick(R.id.btn_user)
        void onBtnUserClick() {
            UserDetailActivity.startWithTransitionAnimation(activity, topic.getAuthor().getLoginName(), imgAvatar, topic.getAuthor().getAvatarUrl());
        }

    }

}
