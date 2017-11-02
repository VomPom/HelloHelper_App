package com.xrone.julis.compous.view.application;

/**
 * Created by Julis on 17/6/20.
 */

public class ApplicationInformation {
    private String title;
    private String pic_url;

    private String content_url;
    private String from;
    public ApplicationInformation(String title,String from,String pic_url,String content_url){
        setTitle(title);
        setFrom(from);
        setPic_url(pic_url);
        setContent_url(content_url);
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPic_url() {
        return pic_url;
    }

    public void setPic_url(String pic_url) {
        this.pic_url = pic_url;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }
    public String getContent_url() {
        return content_url;
    }

    public void setContent_url(String content_url) {
        this.content_url = content_url;
    }

}
