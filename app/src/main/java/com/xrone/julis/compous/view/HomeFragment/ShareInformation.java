package com.xrone.julis.compous.view.HomeFragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.xrone.julis.compous.model.Hello;
import com.xrone.julis.compous.MainActivity;
import com.xrone.julis.compous.R;
import com.xrone.julis.compous.StringData.AppURL;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by Julis on 17/6/14.
 */

public class ShareInformation extends Activity implements View.OnClickListener {
    private EditText infomation;
    private Button submit;
    private Context context;
    private boolean flag = false;

    @BindView(R.id.topic_title)
    EditText title;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_share);
        ButterKnife.bind(this);
        infomation = (EditText) findViewById(R.id.et_share_infomation);
        submit = (Button) findViewById(R.id.share_submit);
        submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

       final String information = infomation.getText().toString();
       final String titleString=title.getText().toString();
        if(information.equals("")){
            Toast.makeText(this,"内容不能为空！\n" +
                    "Content cannot be empty!",Toast.LENGTH_SHORT).show();
        }else{
            RequestQueue queue= Volley.newRequestQueue(this);
            StringRequest request=new StringRequest(Request.Method.POST,AppURL.ADDATOPIC_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String s) {

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {

                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                   Map<String,String> map =new HashMap<>();
                    map.put("title",titleString);
                    map.put("content",information);
                    map.put("author_id",Hello.id);
                    map.put("tab","talking");
                    return map;
                }
            };
            queue.add(request);

            finish();
        }
    }
}







