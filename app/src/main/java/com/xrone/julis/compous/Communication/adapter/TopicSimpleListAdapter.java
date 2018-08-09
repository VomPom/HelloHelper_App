package com.xrone.julis.compous.Communication.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.TextView;

import com.xrone.julis.compous.R;
import com.xrone.julis.compous.Communication.Model.Topic;
import com.xrone.julis.compous.Communication.util.FormatUtils;
import com.xrone.julis.compous.Communication.util.Navigator;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TopicSimpleListAdapter extends RecyclerView.Adapter<TopicSimpleListAdapter.ViewHolder> {

    private final Activity activity;
    private final LayoutInflater inflater;
    private final List<Topic> topicSimpleList = new ArrayList<>();

    public TopicSimpleListAdapter(@NonNull Activity activity) {
        this.activity = activity;
        inflater = LayoutInflater.from(activity);
    }

    public void setTopicSimpleListWithNotify(@NonNull List<Topic> topicSimpleList) {
        this.topicSimpleList.clear();
        this.topicSimpleList.addAll(topicSimpleList);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return topicSimpleList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.com_item_topic_simple, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.update(topicSimpleList.get(position), position == topicSimpleList.size() - 1);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

//        @BindView(R.id.img_avatar)
//        ImageView imgAvatar;

        @BindView(R.id.com_tv_tab)
        CheckedTextView comTv_tab;
        @BindView(R.id.tv_title)
        TextView tvTitle;

        @BindView(R.id.com_tv_content)
        TextView tvContent;

        @BindView(R.id.tv_last_reply_time)
        TextView tvLastReplyTime;


        private Topic topicSimple;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void update(@NonNull Topic topicSimple, boolean isTheLast) {
            this.topicSimple = topicSimple;

            tvTitle.setText(topicSimple.getTitle());
            comTv_tab.setText(topicSimple.getType());
            tvContent.setText(topicSimple.getContent());
            tvLastReplyTime.setText(FormatUtils.getRelativeTimeSpanString(topicSimple.getLastReplyAt()));

        }

//        @OnClick(R.id.img_avatar)
//        void onBtnAvatarClick() {
//            UserDetailActivity.startWithTransitionAnimation(activity, topicSimple.getAuthor().getLoginName(), imgAvatar, topicSimple.getAuthor().getAvatarUrl());
//        }

        @OnClick(R.id.btn_item)
        void onBtnItemClick() {
           //og.e("topicAdapterID",topicSimple.getId());
            Navigator.TopicWithAutoCompat.start(activity, topicSimple.getId());
          //  Navigator.TopicWithAutoCompat.start(activity, topicSimple);
        }

    }

}
