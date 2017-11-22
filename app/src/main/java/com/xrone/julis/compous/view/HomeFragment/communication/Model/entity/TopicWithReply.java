package com.xrone.julis.compous.view.HomeFragment.communication.Model.entity;

import com.google.gson.annotations.SerializedName;
import com.xrone.julis.compous.view.HomeFragment.communication.Model.Reply;

import java.util.List;

public class TopicWithReply extends Topic {

    @SerializedName("is_collect")
    private boolean isCollect; // 是否收藏该话题，只有调用时带有 accesstoken 时，这个字段才有可能为 true

    @SerializedName("replies")
    private List<Reply> replyList;

    public boolean isCollect() {
        return isCollect;
    }

    public void setCollect(boolean collect) {
        isCollect = collect;
    }

    public List<Reply> getReplyList() {
        return replyList;
    }

    public void setReplyList(List<Reply> replyList) {
        this.replyList = replyList;
    }

}
