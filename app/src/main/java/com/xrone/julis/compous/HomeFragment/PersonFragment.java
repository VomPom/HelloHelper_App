package com.xrone.julis.compous.HomeFragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.xrone.julis.compous.StringData.NetURL;
import com.xrone.julis.compous.Utils.Browser;
import com.xrone.julis.compous.Utils.DataOfJsonFactory;
import com.xrone.julis.compous.R;
import com.xrone.julis.compous.Application.article.ArticleAdapter;
import com.xrone.julis.compous.Application.article.ArticleModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.android.volley.VolleyLog.TAG;

/**
 * Created by Julis on 17/6/10.
 */

public class PersonFragment extends Fragment implements AdapterView.OnItemClickListener{

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.app_activity_article,container,false);
        initViews(view);
        return view;
    }
    private ListView listView;
    private ArticleAdapter articleAdapter;
    private List<ArticleModel> articleModels=new ArrayList<>();


    public void initViews(View view){
        listView=(ListView)view.findViewById(R.id.app_article_lv);
        articleAdapter=new ArticleAdapter(getActivity(),articleModels);
        listView.setAdapter(articleAdapter);
        listView.setOnItemClickListener(this);
        getData();
        //   HttpUtils.getNewsJSON(NetURL.APPINFORMATION_URL,getarticle);
    }

    private void getData() {

        RequestQueue queue= Volley.newRequestQueue(getActivity());
        StringRequest stringRequest=new StringRequest(StringRequest.Method.POST, NetURL.APPINFORMATION_URL, s -> {

            articleModels.addAll(DataOfJsonFactory.jsonToArrayList(s,ArticleModel.class));
            articleAdapter.notifyDataSetChanged();
        }, volleyError -> {
            Log.e(TAG, String.valueOf(volleyError));
        });
        queue.add(stringRequest);
    }


    public Handler getarticle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String jsonData = (String) msg.obj;
            Log.e("tt", jsonData);



            //获取Json数组里面的值，并加入到Information对象里面去
            JSONArray jsonArray = null;
            try {
                jsonArray = new JSONArray(jsonData);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    String title = object.getString("title");
                    String from = object.getString("from");
                    String pic_url = object.getString("pic_url");
                    String content_url = object.getString("content_url");
                    articleModels.add(new ArticleModel(title,from,pic_url,content_url));

                    articleAdapter.notifyDataSetChanged();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }};

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ArticleModel information = articleModels.get(position);
        String CLICK_URL=information.getContent_url();
        Intent intent = new Intent(getActivity(), Browser.class);
        intent.putExtra("url",CLICK_URL);
        startActivity(intent);
    }

}






