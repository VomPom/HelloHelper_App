package com.xrone.julis.compous.view.application.article;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.xrone.julis.compous.R;
import com.xrone.julis.compous.Utils.Browser;
import com.xrone.julis.compous.Utils.HttpUtils;
import com.xrone.julis.compous.StringData.AppURL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Julis on 2017/11/6.
 */

public class ArticleAcvitity extends Activity implements AdapterView.OnItemClickListener{
    private ListView listView;
    private ArticleAdapter articleAdapter;
    private List<ArticleModel> articleModels = new ArrayList<ArticleModel>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_activity_article);
        initViews();
    }
    public void initViews(){
        listView=(ListView)findViewById(R.id.app_article_lv);
        articleAdapter=new ArticleAdapter(ArticleAcvitity.this,articleModels);
        listView.setAdapter(articleAdapter);
        listView.setOnItemClickListener(this);
        HttpUtils.getNewsJSON(AppURL.APPINFORMATION_URL, getarticle);
    }

    public Handler getarticle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String jsonData = (String) msg.obj;
            try {
                //获取Json数组里面的值，并加入到Information对象里面去
                JSONArray jsonArray = new JSONArray(jsonData);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    String title = object.getString("title");
                    String from = object.getString("from");
                    String pic_url = object.getString("pic_url");
                    String content_url = object.getString("content_url");
                    articleModels.add(new ArticleModel(title,from,pic_url,content_url));
                }
                articleAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ArticleModel information = articleModels.get(position);
        String CLICK_URL=information.getContent_url();
        Intent intent = new Intent(ArticleAcvitity.this, Browser.class);
        intent.putExtra("url",CLICK_URL);
        startActivity(intent);
    }

}
