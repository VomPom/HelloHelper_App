package com.xrone.julis.compous.view.application.Express;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.xrone.julis.compous.R;
import com.xrone.julis.compous.Utils.TransLaterUtilts;
import com.xrone.julis.compous.model.StringURL;
import com.xrone.julis.compous.view.application.map.navigation.WalkRouteCalculateActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Julis on 17/6/12.
 */

public class ExpressActivity extends Activity{

    private TextView btn_submit;
    private TextView btn_clear;
    private TextView btn_paste;
    private EditText ed_message;


    private        ProgressDialog pDialog               ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_activity_find_express);
        initViews();

    }
    public void initViews(){
        btn_clear=(TextView) findViewById(R.id.btn_express_clear);
        btn_submit=(TextView)findViewById(R.id.btn_express_commit);
        btn_paste=(TextView)findViewById(R.id.btn_express_paste);
        ed_message=(EditText)findViewById(R.id.et_express_message);
        btn_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ed_message.setText("");
            }
        });
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(TextUtils.isEmpty(ed_message.getText().toString())){
//                    Toast.makeText(getBaseContext(),getString(R.string.app_expreess_tip_noInput),Toast.LENGTH_LONG).show();
//                }
                getJsonString();

            }
        });
        btn_paste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboardManager= (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                if(clipboardManager.getText()!=null){
                    ed_message.setText(clipboardManager.getText().toString());
                }
            }
        });
    }

    public void getJsonString(){
        StringRequest stringRequest =new StringRequest(Request.Method.POST,StringURL.GET_EXPRESS_PLACES_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
               // Toast.makeText(getBaseContext(), s,Toast.LENGTH_LONG).show();
                try {
                    JSONArray jsonArray = new JSONArray(s);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        Log.e("aaa",object.getString("message"));
                        Log.e("altitude",object.getString("altitude"));
                        Log.e("longitude",object.getString("longitude"));
                        Log.e("place",object.getString("place"));
                        ed_message.setText("地点:"+object.getString("place")+"\n关键词:"+object.getString("keyword")+"\n短信:"+object.getString("message"));
                    }
                } catch (JSONException e) {
                    Toast.makeText(getBaseContext(), "Can't find it",Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("tterro",volleyError.toString());
            }
        }) {@Override
        protected Map<String, String> getParams() throws AuthFailureError {
            Map<String, String> map = new HashMap<String, String>();
            map.put("message", ed_message.getText().toString());
            return map;
        }
        };
        RequestQueue mQueue=Volley.newRequestQueue(getBaseContext());
        mQueue.add(stringRequest);

    }

}






