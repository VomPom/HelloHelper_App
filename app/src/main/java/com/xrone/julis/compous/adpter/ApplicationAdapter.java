package com.xrone.julis.compous.adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.loopj.android.image.SmartImageView;
import com.xrone.julis.compous.R;
import com.xrone.julis.compous.view.application.ApplicationInformation;

import java.util.List;

/**
 * Created by Julis on 17/6/20.
 */

public class ApplicationAdapter extends BaseAdapter {
    private Context context;

    private List<ApplicationInformation> applicationInformations=null;
    public ApplicationAdapter(Context context, List<ApplicationInformation> informationList){
        this.context=context;
        this.applicationInformations=informationList;
    }
    @Override
    public int getCount() {
        return applicationInformations.size();
    }

    @Override
    public Object getItem(int position) {
        return applicationInformations.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.application_listview_item, null);
        }
        TextView tvtitle = (TextView) convertView.findViewById(R.id.application_title);
        TextView tvfrom = (TextView) convertView.findViewById(R.id.application_from);
        SmartImageView ivpic = (SmartImageView) convertView.findViewById(R.id.application_pic);

        ApplicationInformation app = applicationInformations.get(position);
        tvtitle.setText(app.getTitle());
        tvfrom.setText(app.getFrom());
        ivpic.setImageUrl(app.getPic_url(),R.drawable.head,R.drawable.head);
        return convertView;
    }
}




