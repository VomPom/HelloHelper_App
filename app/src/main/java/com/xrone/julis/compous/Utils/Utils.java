package com.xrone.julis.compous.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.EditText;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Julis on 17/6/20.
 */

public class Utils {
    /**
     * 存储信息
     * @param context
     * @param id
     * @param name
     * @param password
     * @param sex
     * @param head_url
     * @return
     */
    public static boolean saveUserInfo(Context context,String id,String name,String password,String sex,String head_url){
        SharedPreferences sp=context.getSharedPreferences("data",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putString("id",id);
        editor.putString("name",name);
        editor.putString("password",password);
        editor.putString("sex",sex);
        editor.putString("head_url",head_url);
        editor.commit();
        return true;
    }

    /**
     * 获取信息
     * @param context
     * @return
     */
    public static Map<String,String >getUserInfo(Context context){
        SharedPreferences sp=context.getSharedPreferences("data",Context.MODE_PRIVATE);
        String name=sp.getString("name",null);
        String password=sp.getString("password",null);
        String sex=sp.getString("sex",null);
        String head_url=sp.getString("head_url",null);
        String id=sp.getString("id",null);
        Map<String,String> userMap=new HashMap<String,String>();
        userMap.put("name",name);
        userMap.put("password",password);
        userMap.put("id",id);
        userMap.put("head_url",head_url);
        userMap.put("sex",sex);
        return userMap;
    }
}






