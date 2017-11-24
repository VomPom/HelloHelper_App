package com.xrone.julis.compous.view.HomeFragment.communication.Model;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.annotations.SerializedName;
import com.xrone.julis.compous.view.HomeFragment.communication.util.FormatUtils;

import org.joda.time.DateTime;
import org.jsoup.nodes.Document;

/**
 * Created by Julis on 2017/11/16.
 */

public class Topic extends TopicSimple {
    @SerializedName("author_id")
    private String authorId;

    private Tab tab;

    private String content;

    private boolean good;

    private boolean top;

    @SerializedName("reply_count")
    private int replyCount;

    @SerializedName("visit_count")
    private int visitCount;

    @SerializedName("create_at")
    private DateTime createAt;

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    @NonNull
    public Tab getTab() {
        return tab == null ? Tab.unknown : tab; // 接口中有些话题没有 Tab 属性，这里保证 Tab 不为空
    }

    public void setTab(Tab tab) {
        this.tab = tab;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
        cleanContentCache();
    }

    public boolean isGood() {
        return good;
    }

    public void setGood(boolean good) {
        this.good = good;
    }

    public boolean isTop() {
        return top;
    }

    public void setTop(boolean top) {
        this.top = top;
    }

    public int getReplyCount() {
        return replyCount;
    }

    public void setReplyCount(int replyCount) {
        this.replyCount = replyCount;
    }

    public int getVisitCount() {
        return visitCount;
    }

    public void setVisitCount(int visitCount) {
        this.visitCount = visitCount;
    }

    public DateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(DateTime createAt) {
        this.createAt = createAt;
    }

    /**
     * Html渲染缓存
     */
//
    @SerializedName("content_html")
    private String contentHtml;

    @SerializedName("content_summary")
    private String contentSummary;

    public void markSureHandleContent() {
        if (contentHtml == null || contentSummary == null) {
            Document document;
            if (ApiDefine.MD_RENDER) {
                document = FormatUtils.handleHtml(content);
            } else {
                Log.e("ttt","调用了DOCument在TOpci 110");
               // document = FormatUtils.handleHtml(FormatUtils.renderMarkdown(content));
            }
            if (contentHtml == null) {
                contentHtml = document.body().html();
            }
            if (contentSummary == null) {
                contentSummary = document.body().text().trim();
            }
        }
    }

    public void cleanContentCache() {
        contentHtml = null;
        contentSummary = null;
    }
//
    public String getContentHtml() {
        markSureHandleContent();
        return contentHtml;
    }

    public String getContentSummary() {
        markSureHandleContent();
        return contentSummary;
    }


}
