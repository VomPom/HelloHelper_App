package com.xrone.julis.compous.model;

/**
 * Created by Julis on 17/6/15.
 */

public class UserInformation {
    private String name;
    private String info;
    private String time;
    private String headurl;

    public UserInformation(String name, String info, String time, String headurl){
        setName(name);
        setHeadurl(headurl);
        setInfo(info);
        setTime(time);
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getHeadurl() {
        return headurl;
    }

    public void setHeadurl(String headurl) {
        this.headurl = headurl;
    }



}
