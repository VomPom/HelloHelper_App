package com.xrone.julis.compous.Utils;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.xrone.julis.compous.view.application.translate.util.ReplaceABC;
import com.xrone.julis.compous.view.application.translate.util.UrlString;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by Julis on 2017/10/27.
 */

public class TransLaterUtilts {

    public static String getTranslateURL(String mWord) {
        String mUrl=null;
        String s1;
        String s2;
        String s3;
        String MD5string;
        String salt = String.valueOf(System.currentTimeMillis());
        s3 = mWord;
        try {
            MD5 getMD5 = new MD5();
            s2 = URLEncoder.encode(mWord, "utf-8");
            s1 = ReplaceABC.mReplace(s3, "+", " ");
            MD5string = getMD5.GetMD5Code(StringOfID.BAIDU_APP_ID + s1 + salt +StringOfID.BAIDU_KEY);
            mUrl = UrlString.mUrlNOChangeForward + s2
                    + UrlString.mUrlNOChangeBehind + StringOfID.BAIDU_APP_ID + "&salt=" + salt + "&sign=" + MD5string;
            //System.out.println(mUrl);
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return mUrl;
    }


    public static void getData(Context context, String string,VolleyResponseListener volleyResponseListener){

        final String realRequesUrl=getTranslateURL(string);
        RequestQueue mQueue=Volley.newRequestQueue(context);
        StringRequest stringRequest=new StringRequest(Request.Method.GET, realRequesUrl,volleyResponseListener,new VolleyResponseError());
        mQueue.add(stringRequest);

    }
    static class VolleyResponseError implements Response.ErrorListener{
        @Override
        public void onErrorResponse(VolleyError volleyError) {

        }
    }




}

