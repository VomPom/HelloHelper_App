package com.xrone.julis.compous.model;

/**
 * Created by Julis on 2017/10/27.
 */

public class TranslateResultModel {
    private String from;
    private String to;
    private String src;
    private String dst;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getDst() {
        return dst;
    }

    public void setDst(String dst) {
        this.dst = dst;
    }

    @Override
    public String toString() {
        return "DST:"+this.getDst()+" From:"+this.getFrom()+" To:"+this.getTo()+" Src:"+this.getSrc();
    }
}
