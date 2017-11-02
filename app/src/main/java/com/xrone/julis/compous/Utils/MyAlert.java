package com.xrone.julis.compous.Utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;

import com.xrone.julis.compous.R;
import com.xrone.julis.compous.view.application.map.MapActivity;
import com.xrone.julis.compous.view.application.map.navigation.WalkRouteCalculateActivity;

/**
 * Created by Julis on 2017/10/28.
 */

public abstract class MyAlert {
    private Context context;
    private String title;
    private String content;

    public MyAlert(Context context, String title, String content) {
        this.context = context;
        this.title = title;
        this.content = content;
    }

    public void showMyAlert(){
        AlertDialog isToNavigationAlert = new AlertDialog.Builder(context).create();
        isToNavigationAlert.setTitle(title);
        isToNavigationAlert.setMessage(content);
        isToNavigationAlert.setButton(DialogInterface.BUTTON_NEGATIVE, context.getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onClickNo();
            }
        });
        isToNavigationAlert.setButton(DialogInterface.BUTTON_POSITIVE, context.getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onClickYes();
            }
        });
        isToNavigationAlert.show();
    }
    public abstract void onClickNo();
    public abstract void onClickYes();


    public static void showErroNetworking(Context context){
        AlertDialog isToNavigationAlert = new AlertDialog.Builder(context).create();
        isToNavigationAlert.setTitle("Internet Connection");
        isToNavigationAlert.setMessage("Please check your internet connection");
        isToNavigationAlert.setButton(DialogInterface.BUTTON_POSITIVE, context.getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        isToNavigationAlert.show();
    }

    public static void notFinished(Context context){
        AlertDialog isToNavigationAlert = new AlertDialog.Builder(context).create();
        isToNavigationAlert.setTitle("Developing...");
        isToNavigationAlert.setMessage("Thanks for your corperation.");
        isToNavigationAlert.setButton(DialogInterface.BUTTON_POSITIVE, context.getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        isToNavigationAlert.show();
    }

    public static void showSuccess(Context context){
        AlertDialog isToNavigationAlert = new AlertDialog.Builder(context).create();
        isToNavigationAlert.setTitle("Success");
        isToNavigationAlert.setMessage("Thanks for your help.");
        isToNavigationAlert.setButton(DialogInterface.BUTTON_POSITIVE, context.getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        isToNavigationAlert.show();
    }


}
