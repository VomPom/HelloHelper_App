package com.xrone.julis.compous.view.application;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.xrone.julis.compous.R;
import com.xrone.julis.compous.Utils.MyAlert;
import com.xrone.julis.compous.model.StringURL;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Julis on 2017/11/1.
 */

public class Feedback_Activity extends Activity {
    private EditText ed_message;
    private Button submit;
    private ProgressDialog dialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_feedback);
        initViews();
    }

    public void initViews(){
        dialog=new ProgressDialog(this);
        dialog.setTitle("Submiting...");


        ed_message=(EditText)findViewById(R.id.et_feedback);
        submit=(Button)findViewById(R.id.feed_submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
                final String text=ed_message.getText().toString().trim();
               StringRequest request=new StringRequest(Request.Method.POST, StringURL.FEEDBACK_URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        dialog.dismiss();
                        MyAlert.showSuccess(getBaseContext());
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.e("err",volleyError.toString());
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> map=new HashMap<>();
                        map.put("message",text);
                        return map;
                    }
                }

                ;
                RequestQueue requestQueue= Volley.newRequestQueue(getBaseContext());
                requestQueue.add(request);
            }

        });
    }

}













