package com.xrone.julis.compous.dao;

/**
 * Created by Julis on 17/6/13.
 */

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.xrone.julis.compous.model.ExpressPlaceBean;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class DBoperation{
    private static  JSONArray jsonArray;

    public static  void getJsonData(String url){
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
       // params.add("username", username);
       // params.add("password", password);
        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String response = new String(bytes);
                Log.e("debug", response);
                try {
                    jsonArray=new JSONArray(response);
                    System.out.println(jsonArray);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                System.out.println("shibai");
            }
        });
    }

    public static  void addExpressPlace(ExpressPlaceBean expressPlaceBean){
        String url="";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
//         params.add("username", );
//         params.add("password", );
        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String response = new String(bytes);
                Log.e("debug", response);
                try {
                    jsonArray=new JSONArray(response);
                    System.out.println(jsonArray);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                System.out.println("shibai");
            }
        });
    }
    /**
     * 获取定位点
     * @param url
     * @return
     */
    public static  ArrayList<ExpressPlaceBean> getPlaces(String url){
        ArrayList<ExpressPlaceBean> expressPlaceBeens=new ArrayList<>();
            getJsonData(url);
              System.out.println(jsonArray);
//            try {
//                for(int i=0;i<jsonArray.length();i++){
//                    JSONObject object=jsonArray.getJSONObject(i);
//                    System.out.println("object:"+object.toString());
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
            System.out.println();
        return expressPlaceBeens;
    }


    class myHandler extends AsyncHttpResponseHandler {
        @Override
        public void onSuccess(int i, Header[] headers, byte[] bytes) {
        }
        @Override
        public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {

        }
    }


}