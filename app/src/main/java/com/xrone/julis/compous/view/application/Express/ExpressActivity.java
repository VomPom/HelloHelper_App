package com.xrone.julis.compous.view.application.Express;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.baidu.mobstat.StatService;
import com.xrone.julis.compous.R;
import com.xrone.julis.compous.Utils.MyAlert;
import com.xrone.julis.compous.model.StringURL;

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


    private double deslatitude;
    private double deslongitude;
    private ProgressDialog pDialog;


    /**
     * 地图
     */
    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = null;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_activity_find_express);
        StatService.onEvent(ExpressActivity.this, "express", "express", 1);
        initViews();
        initGPS();
    }
    public void initViews(){
        btn_clear=(TextView) findViewById(R.id.btn_express_clear);
        btn_submit=(TextView)findViewById(R.id.btn_express_commit);
        btn_paste=(TextView)findViewById(R.id.btn_express_paste);
        ed_message=(EditText)findViewById(R.id.et_express_message);
        pDialog=new ProgressDialog(ExpressActivity.this);
        btn_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ed_message.setText("");
            }
        });
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StatService.onEvent(ExpressActivity.this, "expressSubmit", "expressSubmit", 1);
                if(TextUtils.isEmpty(ed_message.getText().toString())){
                    Toast.makeText(getBaseContext(),getString(R.string.app_expreess_tip_noInput),Toast.LENGTH_LONG).show();
                }else{
                    sendAndgetExpressInformation();
                }

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

    /**
     *  判断GPS模块是否开启，如果没有则开启
     */
    private void initGPS() {
        LocationManager locationManager = (LocationManager) this
                .getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager
                .isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {

            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setMessage("Positioning needs GPS, please open GPS.");
            dialog.setPositiveButton("OK",
                    new android.content.DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            // 转到手机设置界面，用户设置GPS
                            Intent intent = new Intent(
                                    Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivityForResult(intent, 0); // 设置完成后返回到原来的界面

                        }
                    });
            dialog.setNeutralButton("Cancel", new android.content.DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    arg0.dismiss();
                }
            } );
            dialog.show();
        }
    }




    /**
     * 获取相关数据
     */
    public void sendAndgetExpressInformation(){
        pDialog.setMessage("Finding...");
        pDialog.show();
        StringRequest findRequest =new StringRequest(Request.Method.POST,StringURL.GET_EXPRESS_INFO_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONArray jsonArray = new JSONArray(s);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        deslatitude= Double.parseDouble(object.getString("altitude"));
                        deslongitude=Double.parseDouble(object.getString("longitude"));
                        startLocation();
                        pDialog.dismiss();
                    }
                } catch (JSONException e) {
                    pDialog.dismiss();
                    MyAlert.showErrorMessage(ExpressActivity.this,"Sorry！Not Found！","Please check whether the message is the Express notification information\n" +
                            "If it is, we will complete it.\nThanks for your cooperation.");
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                pDialog.dismiss();
                MyAlert.showErrorMessage(ExpressActivity.this,"Sorry！Not Found！","Something wrong.");

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("message", ed_message.getText().toString());
                return map;
            }
        };

        StringRequest insertQueryInfo = new StringRequest(Request.Method.POST, StringURL.GET_EXPRESS_INFO_URL, new Response.Listener<String>() {
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
                return super.getParams();
            }
        };




        RequestQueue mQueue=Volley.newRequestQueue(getBaseContext());
        mQueue.add(findRequest);
        mQueue.add(insertQueryInfo);

    }


    /**
     * 开始定位
     *
     */
    private void startLocation(){

        if (null == locationOption) {
            locationOption = new AMapLocationClientOption();locationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
        }
        //初始化client
        locationClient = new AMapLocationClient(this.getApplicationContext());
        locationOption = getDefaultOption();
        //设置定位参数
        locationClient.setLocationOption(locationOption);
        // 设置定位监听
        locationClient.setLocationListener(locationListener);
        // 设置定位参数
        locationClient.setLocationOption(locationOption);
        // 启动定位
        locationClient.startLocation();
    }


    /**
     * 默认的定位参数
     * @since 2.8.0
     * @author hongming.wang
     *
     */
    private AMapLocationClientOption getDefaultOption(){
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.setGpsFirst(false);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.setHttpTimeOut(30000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mOption.setInterval(2000);//可选，设置定位间隔。默认为2秒
        mOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是true
        mOption.setOnceLocation(false);//可选，设置是否单次定位。默认是false
        mOption.setOnceLocationLatest(false);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        mOption.setSensorEnable(false);//可选，设置是否使用传感器。默认是false
        mOption.setWifiScan(true); //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
        mOption.setLocationCacheEnable(true); //可选，设置是否使用缓存定位，默认为true
        return mOption;
    }
    /**
     * 定位监听
     */
    AMapLocationListener locationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation location) {
            if (null != location) {
                //errCode等于0代表定位成功，其他的为定位失败，具体的可以参照官网定位错误码说明
                if(location.getErrorCode() == 0){
                    Log.e("debut","解析定位结果");
                    Log.e("debut", String.valueOf(location.getLatitude()));
                    //解析定位结果，
                    Intent intent=new Intent( ExpressActivity.this,WalkRouteActivity.class);
                    intent.putExtra("deslatitude",deslatitude);
                    intent.putExtra("deslongitude",deslongitude);
                    intent.putExtra("mylatitude",location.getLatitude());
                    intent.putExtra("mylongitude",location.getLongitude());
                    ExpressActivity.this.startActivity(intent);
                    destroyLocation();
                 } else {
                    //定位失败
                     Log.e("debut","定位失败");
                    Toast.makeText(getBaseContext(),"The location failed.",Toast.LENGTH_LONG).show();
                }
            }
        }
    };
    /**
     * 销毁定位
     * @since 2.8.0
     * @author hongming.wang
     *
     */
    private void destroyLocation(){
        if (null != locationClient) {
            /**
             * 如果AMapLocationClient是在当前Activity实例化的，
             * 在Activity的onDestroy中一定要执行AMapLocationClient的onDestroy
             */
            locationClient.onDestroy();
            locationClient = null;
            locationOption = null;
        }
    }


}






