package com.xrone.julis.compous.model;

/**
 * Created by Julis on 2017/10/12.
 */

public class UpdateBean {
    private int versionCode;
    private String content;
    private String updateUrl;
    private String versionName;

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUpdateUrl() {
        return updateUrl;
    }

    public void setUpdateUrl(String updateUrl) {
        this.updateUrl = updateUrl;
    }

    @Override
    public String toString() {
        return this.updateUrl+"code:"+this.getVersionCode()+" "+this.content;

    }
}
