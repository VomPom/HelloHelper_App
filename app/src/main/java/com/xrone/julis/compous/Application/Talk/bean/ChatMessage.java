package com.xrone.julis.compous.Application.Talk.bean;

/**
 * Created by Julis on 2018/5/26.
 */

public class ChatMessage {
    private int msgId;
    private int isComing;//0表接收的消息，1表发送的消息
    private String content;//信息内容
    private String date;//时间
    private String tran_content;

    public String getTran_content() {
        return tran_content;
    }

    public void setTran_content(String tran_content) {
        this.tran_content = tran_content;
    }

    public int getIsComing() {
        return isComing;
    }

    public void setIsComing(int isComing) {
        this.isComing = isComing;
    }



    public int getMsgId() {
        return msgId;
    }

    public void setMsgId(int msgId) {
        this.msgId = msgId;
    }





    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


}
