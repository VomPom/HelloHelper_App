package com.xrone.julis.compous.Utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.provider.Settings;

/**
 * Created by Julis on 2017/12/23.
 */

public class GpsStatus {
    private Activity activity;

    public GpsStatus(Activity activity) {
        this.activity = activity;
    }

    /**
     *  判断GPS模块是否开启，如果没有则开启
     */
    public void initGPS() {
        LocationManager locationManager = (LocationManager) activity
                .getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager
                .isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {

            AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
            dialog.setMessage("Positioning needs GPS, please open GPS.");
            dialog.setPositiveButton("OK",
                    (arg0, arg1) -> {
                        // 转到手机设置界面，用户设置GPS
                        Intent intent = new Intent(
                                Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        activity.startActivityForResult(intent, 0); // 设置完成后返回到原来的界面

                    });
            dialog.setNeutralButton("Cancel", (arg0, arg1) -> arg0.dismiss());
            dialog.show();
        }
    }
}
