package com.xrone.julis.compous.view.HomeFragment.communication.Model.entity;

import com.google.gson.annotations.SerializedName;

import org.joda.time.DateTime;

/**
 * Created by Julis on 2017/11/16.
 */

public class TopicSimple {

    private String id;

    private Author author;

    private String title;

    @SerializedName("last_reply_at")
    private DateTime lastReplyAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public DateTime getLastReplyAt() {
        return lastReplyAt;
    }

    public void setLastReplyAt(DateTime lastReplyAt) {
        this.lastReplyAt = lastReplyAt;
    }

}
