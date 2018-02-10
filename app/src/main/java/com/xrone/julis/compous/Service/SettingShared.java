package com.xrone.julis.compous.Service;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.Log;

import com.xrone.julis.compous.MainActivity;

import java.util.Locale;

/**
 * Created by Julis on 2017/12/1.
 */

public class SettingShared {

    private static final String TAG = "SettingShared";

    /**
     * 改变语言
     * @param context
     */
    public static boolean changeLanguage(Context context) {
        boolean flag;
        String localeString = Locale.getDefault().toString();
        Log.e(TAG,localeString);
        String language = null;
        String country = null;
        if(localeString.equals("en_US")){
            flag=true;
            language = "zh";
            country = "CN";
        }else{
            flag=false;
            language = "en";
            country = "US";
        }
//        switch (locale) {
//            case "s":
//                language = "zh";
//                country = "CN";
//                break;
//            default:
//                language = "en";
//                country = "US";
//                break;
//        }
        Resources resources = context.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        dm=context.getResources().getDisplayMetrics();
        Locale locale = new Locale(language, country);
        Configuration configuration = context.getResources().getConfiguration();
        configuration.locale = locale;
        resources.updateConfiguration(configuration, dm);
        return flag;
    }
}
