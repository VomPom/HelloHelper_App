package com.xrone.julis.compous.Application.Talk.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.xrone.julis.compous.Application.Talk.bean.ChatMessage;
import com.xrone.julis.compous.R;

import java.util.List;


/**
 * 聊天适配器
 *
 * @author baiyuliang
 * @ClassName: MessageChatAdapter
 */
public class ChatAdapter extends BaseListAdapter<ChatMessage> {

    //文本
    private final int TYPE_RECEIVER_TXT = 0;
    private final int TYPE_SEND_TXT = 1;
    //图片
    private final int TYPE_SEND_IMAGE = 2;
    private final int TYPE_RECEIVER_IMAGE = 3;
    //位置
    private final int TYPE_SEND_LOCATION = 4;
    private final int TYPE_RECEIVER_LOCATION = 5;
    //语音
    private final int TYPE_SEND_VOICE = 6;
    private final int TYPE_RECEIVER_VOICE = 7;
    //音乐
    private final int TYPE_RECEIVER_MUSIC = 8;
    //新闻，菜谱等列表类信息
    private final int TYPE_RECEIVER_LIST = 9;


    OnClickMsgListener onClickMsgListener;

    public ChatAdapter(Context context, List<ChatMessage> msgList, OnClickMsgListener onClickMsgListener) {
        super(context, msgList);
        mContext = context;

        this.onClickMsgListener = onClickMsgListener;
    }

    //获取item类型
    @Override
    public int getItemViewType(int position) {
        ChatMessage msg = list.get(position);
        return msg.getIsComing() == 0 ? TYPE_RECEIVER_TXT : TYPE_SEND_TXT;

    }

    @Override
    public int getViewTypeCount() {
        return 10;
    }

    /**
     * 根据消息类型，使用对应布局
     *
     * @param msg
     * @param type
     * @return
     */
    private View createViewByType(ChatMessage msg, int type) {
        Log.e("TEST",msg.toString());
        Log.e("position", String.valueOf(type));
       return getItemViewType(type) == TYPE_RECEIVER_TXT ? createView(R.layout.app_chat_item_text_rece) : createView(R.layout.app_chat_item_text_sent);

    }

    private View createView(int id) {
        return mInflater.inflate(id, null);
    }

    @Override
    public View bindView(final int position, View convertView, ViewGroup parent) {
        final ChatMessage msg = list.get(position);

        if (convertView == null) {
            convertView = createViewByType(msg, position);
        }

        TextView chat_time = ViewHolder.get(convertView, R.id.chat_time);//时间
        TextView tv_text = ViewHolder.get(convertView, R.id.tv_text);//文本
        TextView tv_tran_text = ViewHolder.get(convertView, R.id.tv_text_tran);//文本
        LinearLayout talk_rece_sounder=ViewHolder.get(convertView,R.id.talk_rece_sounder);
        talk_rece_sounder.setOnClickListener(new onClick(position));

        chat_time.setText(msg.getDate());//时间
        tv_text.setText(msg.getContent());
        tv_tran_text.setText(msg.getTran_content());
//      tv_text.setText(ExpressionUtil.prase(mContext, tv_text, msg.getContent()));
        Linkify.addLinks(tv_text, Linkify.ALL);
        tv_text.setOnClickListener(new onClick(position));
        tv_text.setOnLongClickListener(new onLongCilck(position));


        return convertView;
    }



    /**
     * 屏蔽listitem的所有事件
     */
    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }




    /**
     * 点击监听
     *
     * @author 白玉梁
     */
    class onClick implements View.OnClickListener {
        int position;

        public onClick(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View arg0) {
            onClickMsgListener.click(position);
        }

    }

    /**
     * 长按监听
     *
     * @author 白玉梁
     */
    class onLongCilck implements View.OnLongClickListener {
        int position;

        public onLongCilck(int position) {
            this.position = position;
        }

        @Override
        public boolean onLongClick(View arg0) {
            onClickMsgListener.longClick(position);
            return true;
        }
    }



    public interface OnClickMsgListener {
        void click(int position);

        void longClick(int position);
    }

}
