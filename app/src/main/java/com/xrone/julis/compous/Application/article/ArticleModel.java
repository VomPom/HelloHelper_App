package com.xrone.julis.compous.Application.article;

/**
 * Created by Julis on 17/6/20.
 */

public class ArticleModel {
    /**
     * id : 7
     * title : 【老外学中文】扎克伯格秀中文:揭开学好外语的诀窍
     * from : 昊汉国际汉语基地 国际汉语人俱乐部
     * pic_url : http://mmbiz.qpic.cn/mmbiz/Cv3XsbSScRn5OKBhJ3jwAWWX04UB5A5n5KUpCwiaopnpBQmpbcHwoRicyeQGKYm8QfOUVHxOyH0jmANXCrgWRBgA/640?wx_fmt=jpeg&wxfrom=5&wx_lazy=1
     * content_url : http://mp.weixin.qq.com/s?__biz=MzA4NzU1ODQzMw==&mid=401595256&idx=1&sn=98a57ef3df8fb80517d985cc8bf43008&open_source=weibo_search
     */

    private String id;
    private String title;
    private String from;
    private String pic_url;
    private String content_url;
    public ArticleModel(String title, String from, String pic_url, String content_url){
        setTitle(title);
        setFrom(from);
        setPic_url(pic_url);
        setContent_url(content_url);
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getPic_url() {
        return pic_url;
    }

    public void setPic_url(String pic_url) {
        this.pic_url = pic_url;
    }

    public String getContent_url() {
        return content_url;
    }

    public void setContent_url(String content_url) {
        this.content_url = content_url;
    }
//    private String title;
//    private String pic_url;
//    private String content_url;
//    private String from;
//    public ArticleModel(String title, String from, String pic_url, String content_url){
//        setTitle(title);
//        setFrom(from);
//        setPic_url(pic_url);
//        setContent_url(content_url);
//    }
//    public String getTitle() {
//        return title;
//    }
//
//    public void setTitle(String title) {
//        this.title = title;
//    }
//
//    public String getPic_url() {
//        return pic_url;
//    }
//
//    public void setPic_url(String pic_url) {
//        this.pic_url = pic_url;
//    }
//
//    public String getFrom() {
//        return from;
//    }
//
//    public void setFrom(String from) {
//        this.from = from;
//    }
//    public String getContent_url() {
//        return content_url;
//    }
//
//    public void setContent_url(String content_url) {
//        this.content_url = content_url;
//    }

}
