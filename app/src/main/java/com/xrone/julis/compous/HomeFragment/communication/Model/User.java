package com.xrone.julis.compous.HomeFragment.communication.Model;

import com.google.gson.annotations.SerializedName;
import com.xrone.julis.compous.HomeFragment.communication.util.FormatUtils;


import org.joda.time.DateTime;

import java.util.List;

public class User {

    @SerializedName("loginname")
    private String loginName;

    @SerializedName("avatar_url")
    private String avatarUrl;

    @SerializedName("create_at")
    private DateTime createAt;

    private int score;

    @SerializedName("recent_topics")
    private List<Topic> recentTopicList;

    @SerializedName("recent_replies")
    private List<Topic> recentReplyList;

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getAvatarUrl() { // 修复头像地址的历史遗留问题
        return FormatUtils.getCompatAvatarUrl(avatarUrl);
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public DateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(DateTime createAt) {
        this.createAt = createAt;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public List<Topic> getRecentTopicList() {
        return recentTopicList;
    }

    public void setRecentTopicList(List<Topic> recentTopicList) {
        this.recentTopicList = recentTopicList;
    }

    public List<Topic> getRecentReplyList() {
        return recentReplyList;
    }

    public void setRecentReplyList(List<Topic> recentReplyList) {
        this.recentReplyList = recentReplyList;
    }

}
