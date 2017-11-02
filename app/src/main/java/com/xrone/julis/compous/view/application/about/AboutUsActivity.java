package com.xrone.julis.compous.view.application.about;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Window;

import com.xrone.julis.compous.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Julis on 2017/10/21.
 */

public class AboutUsActivity extends Activity {
    CollapsingToolbarLayout collapsingToolbar;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_about);
        initView();
    }
    public void initView(){
        collapsingToolbar=(CollapsingToolbarLayout)findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle("COME ON WITH US!");


    }


}
