package com.xrone.julis.compous.view.Home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import com.xrone.julis.compous.R;

/**
 * Created by Julis on 17/6/15.
 */

public class ReFlashLIstView extends ListView {
    View header;
      public ReFlashLIstView(Context context) {
        super(context);
          initView(context);
    }
    private void initView(Context context){
        LayoutInflater inflater = LayoutInflater.from(context);
        header=inflater.inflate(R.layout.header_layout,null);
        this.addHeaderView(header);
    }
}











