package com.xrone.julis.compous.Application.Express;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
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
import com.baidu.mobstat.StatService;
import com.xrone.julis.compous.AppBaseActivity;
import com.xrone.julis.compous.R;
import com.xrone.julis.compous.Utils.GpsStatus;
import com.xrone.julis.compous.Application.Express.Activity.WalkRouteActivity;
import com.xrone.julis.compous.Application.Express.Presenter.ExpressPresenter;
import com.xrone.julis.compous.Application.Express.Presenter.IExpressPresenter;
import com.xrone.julis.compous.Application.Express.View.IExpressView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Julis on 17/6/12.
 */

public class ExpressActivity extends AppBaseActivity implements IExpressView{

    @BindView(R.id.btn_express_commit) TextView btn_submit;
    @BindView(R.id.btn_express_clear)  TextView btn_clear;
    @BindView(R.id.btn_express_paste)  TextView btn_paste;
    @BindView(R.id.et_express_message) EditText ed_message;
    private double deslatitude;
    private double deslongitude;
    private ProgressDialog pDialog;
    private IExpressPresenter expressPresenter;


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
        init();
        ButterKnife.bind(this);
        new GpsStatus(this).initGPS();
    }

    public void init(){
        pDialog=new ProgressDialog(ExpressActivity.this);
        pDialog.setMessage("Searching...");
        expressPresenter=new ExpressPresenter(this,this);
        initTitleBar("Back", getBaseContext().getResources().getString(R.string.express), "", this);

    }

    @OnClick({
            R.id.btn_express_clear,R.id.btn_express_commit,R.id.btn_express_paste
    })
    public void onClick(TextView view){
        switch (view.getId()){
            case R.id.btn_express_clear:
                clearInput();
                break;
            case R.id.btn_express_paste:
                   paste();
                break;
            case R.id.btn_express_commit:
                submit();
                break;
        }
    }




    /**
     * 开始定位
     */
    @Override
    public void startLocation(@NonNull double desLatitude, @NonNull double desLongitude){

        this.deslatitude=desLatitude;
        this.deslongitude=desLongitude;

        pDialog.dismiss();

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

    @Override
    public void clearInput() {
        ed_message.setText("");
    }

    @Override
    public void paste() {
        ClipboardManager clipboardManager= (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        if(clipboardManager.getText()!=null){
            ed_message.setText(clipboardManager.getText().toString());
        }
    }

    @Override
    public void submit() {
        StatService.onEvent(ExpressActivity.this, "expressSubmit", "expressSubmit", 1);
        if(TextUtils.isEmpty(ed_message.getText().toString())){
            Snackbar.make(btn_submit, getString(R.string.app_expreess_tip_noInput), Snackbar.LENGTH_SHORT)
                    .show();
        }else{
            pDialog.show();
            expressPresenter.sendAndgetExpressInformation(ed_message.getText().toString());
        }
    }




}






