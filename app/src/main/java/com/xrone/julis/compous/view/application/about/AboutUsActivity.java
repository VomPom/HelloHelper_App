package com.xrone.julis.compous.view.application.about;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Window;
import android.widget.TextView;

import com.xrone.julis.compous.R;
import com.xrone.julis.compous.model.Hello;
import com.xrone.julis.compous.view.application.exchangeRate.Data.Global_Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Julis on 2017/10/21.
 */

public class AboutUsActivity extends Activity {
    CollapsingToolbarLayout collapsingToolbar;
    private TextView textView;
    private TextView version;

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
        textView=(TextView)findViewById(R.id.about_us_text);
        version=(TextView)findViewById(R.id.app_about_version);
        version.setText("Version:"+ Hello.versionName);
        textView.setText("Dear Friend:"+"\n"+"We are a team from Wenzhou University," +
                " in order to help foreign students in China better life and learning, " +
                "on the APP provides the express point query, exchange community, " +
                "text translation, exchange rate conversion and other functions." +
                " When the user registers an account, " +
                "it automatically jumps to the map navigation interface by inputting" +
                " the text message, and adds English pronunciation and " +
                "synchronized English subtitles when using the map related function. " +
                "At the same time, Chinese and foreign users can publish some of their " +
                "own articles or ideas in the communication community, at the same time," +
                " comments and replies, and promote exchanges between China and foreign countries.\n" +
                "Although we have tried to improve, " +
                "but there are many defects and deficiencies, in order to give you more help," +
                " if you find any problems in the process of use, please give us app_feedback," +
                " we will be the first time to make corrections, only in this way," +
                " we can make progress together, grow together."+"\n"+
                "Contact us: "+"\n"+"WeChat：julis617669559"+"\n"+
                "Email:1607637473@qq.com");

    }


}
