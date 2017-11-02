package com.xrone.julis.compous.adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.loopj.android.image.SmartImageView;
import com.xrone.julis.compous.model.UserInformation;
import com.xrone.julis.compous.R;

import java.util.List;

/**
 * Created by Julis on 17/6/15.
 */

public class ShareAdapter extends BaseAdapter {
    private Context context;
    private List<UserInformation> informationList=null;
    public ShareAdapter(Context context, List<UserInformation> informationList){
        this.context=context;
        this.informationList=informationList;
    }
    @Override
    public int getCount() {
        return informationList.size();
    }
    @Override
    public Object getItem(int position) {
        return informationList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        if(position==0&&convertView == null){
//            convertView = LayoutInflater.from(context).inflate(R.layout.share_item_first, null);
//        }else {
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.share_item, null);
            }

            TextView tvName = (TextView) convertView.findViewById(R.id.share_name);
            TextView tvInfor = (TextView) convertView.findViewById(R.id.share_information);
            TextView tvTime = (TextView) convertView.findViewById(R.id.share_time);
            SmartImageView ivHead = (SmartImageView) convertView.findViewById(R.id.img_head);

            UserInformation list = informationList.get(position);
            tvName.setText(list.getName());
            tvTime.setText(list.getTime());
            tvInfor.setText(list.getInfo());
           ivHead.setImageUrl(list.getHeadurl(),R.drawable.head,R.drawable.head);
     //  }
        //HttpUtils.setPicBitmap(ivHead,list.getHeadurl());
        return convertView;
    }
}













