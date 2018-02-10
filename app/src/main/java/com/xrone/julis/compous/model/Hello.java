package com.xrone.julis.compous.model;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Julis on 17/6/16.
 */

public class Hello {

    public static String Token;
    public static String sex = "1";
    public static String id = "null";
    public static String username = "null";
    public static String head_url = "null";


    public static boolean isLogin=false;
    public static String versionName;
    public static int versionCode;


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
    public static boolean saveUserInfo(Context context, String id, String name, String password, String sex, String head_url, String is_login){
        SharedPreferences sp=context.getSharedPreferences("data",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putString("id",id);
        editor.putString("name",name);
        editor.putString("password",password);
        editor.putString("sex",sex);
        editor.putString("head_url",head_url);
        editor.putString("is_login",is_login);
        Hello.sex=sex;
        Hello.isLogin=is_login.equals("true")?true:false;
        Hello.head_url=head_url;
        Hello.id=id;
        Hello.username=name;

        editor.commit();
        return true;
    }

    /**
     * 获取信息
     * @param context
     * @return
     */
    public static Map<String,String > getUserInfo(Context context){
        SharedPreferences sp=context.getSharedPreferences("data",Context.MODE_PRIVATE);
        String name=sp.getString("name",null);
        String password=sp.getString("password",null);
        String sex=sp.getString("sex",null);
        String head_url=sp.getString("head_url",null);
        String id=sp.getString("id",null);
        String is_login=sp.getString("is_loglin","true");
        Map<String,String> userMap=new HashMap<String,String>();
        userMap.put("name",name);
        userMap.put("password",password);
        userMap.put("id",id);
        userMap.put("head_url",head_url);
        userMap.put("sex",sex);
        userMap.put("is_login", is_login);
        return userMap;
    }
}





