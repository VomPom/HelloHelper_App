package com.xrone.julis.compous;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.baidu.mobstat.SendStrategyEnum;
import com.baidu.mobstat.StatService;
import com.xrone.julis.compous.Utils.BottomNavigationViewHelper;
import com.xrone.julis.compous.Utils.CheckVersion;
import com.xrone.julis.compous.Utils.Utils;
import com.xrone.julis.compous.Service.SmsObserver;
import com.xrone.julis.compous.view.application.exchangeRate.Data.Global_Data;
import com.xrone.julis.compous.view.application.ApplicationFragment;
import com.xrone.julis.compous.view.HomeFragment.HomeFragment;
import com.xrone.julis.compous.view.HomeFragment.banner.PersonFragment;
import com.xrone.julis.compous.model.Hello;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
    private android.app.FragmentTransaction transaction;
    private PersonFragment personFrag=new PersonFragment();
    private HomeFragment homeFrag = new HomeFragment();
    private ApplicationFragment applicationFrag= new ApplicationFragment();
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener;

    private CheckVersion checkVersion=new CheckVersion(this);



    /**
     * 检查权限列表
     */
    private static final String[] permissionsArray = new String[]{
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.CHANGE_NETWORK_STATE,
            Manifest.permission.CHANGE_WIFI_STATE,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_SETTINGS,
            Manifest.permission.INTERNET,
            Manifest.permission.RECEIVE_SMS,
            Manifest.permission.READ_SMS,
            Manifest.permission.ACCESS_NETWORK_STATE
    };
    //还需申请的权限列表
    private List<String> permissionsList = new ArrayList<String>();
    //申请权限后的返回码
    private static final int REQUEST_CODE_ASK_PERMISSIONS = 1;
    /**
     * 静态代码块，执行底部导航的相关监听
     */
    {
        /**
         * 主控制界面
         */

        mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        replaceFragment(homeFrag);
                        return true;
                    case R.id.navigation_application:
                        replaceFragment(applicationFrag);
                        return true;
                    case R.id.navigation_personal:
                        replaceFragment(personFrag);
                        return true;
                }
                return false;
            }

        };
    }

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        initView();
        initFunction();
    }

    /**
     * 初始化界面
     */
    public void initView(){
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setSelectedItemId(R.id.navigation_application);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        replaceFragment(applicationFrag);
        BottomNavigationViewHelper.disableShiftMode(navigation);
    }

    /**
     * 初始化一些功能
     */
    public void initFunction(){
        getAPIVerison();
        /**
         * 检查权限
         */
        checkRequiredPermission(this);
        /*
         *检查更新状态
         */
        try {
            if(checkVersion.isUpdateFlag()==false){
                checkVersion.checkVersion();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        /**
         * 百度数据统计
         */
        StatService.start(this);

        /**
         * 监听短信
         */
//        ContentResolver resolver=getContentResolver();
//        Uri uri=Uri.parse("content://sms/");
//        resolver.registerContentObserver(uri,true,new SmsObserver(this,new Handler()));
//

        // ListenClipboardService.start(this);

    }
    /**
     * 初始化数据
     */
    public void initData(){
        Map<String,String> userInfo= Utils.getUserInfo(this);
        if(userInfo!=null&&userInfo.get("sex")!=null){
            Hello.username=userInfo.get("name");
            Hello.sex=userInfo.get("sex");
            Hello.head_url=userInfo.get("head_url");
        }
    }




    /**
     * 替换Fragment
     * @param fragment f
     */
    public void replaceFragment(Fragment fragment) {
        transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.content,fragment);
        transaction.commit();
    }





    /**
     * 检查权限
     * @param activity
     */
    private void checkRequiredPermission(final Activity activity){
        for (String permission : permissionsArray) {
            if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsList.add(permission);
            }
        }
        ActivityCompat.requestPermissions(activity, permissionsList.toArray(new String[permissionsList.size()]), REQUEST_CODE_ASK_PERMISSIONS);
    }

    /**
     * 获取安卓的版本
     * @return
     */

    public static float getAPIVerison() {
        Float f = null;
        try {
            StringBuilder strBuild = new StringBuilder();
            strBuild.append(android.os.Build.VERSION.RELEASE.substring(0, 2));
            f = new Float(strBuild.toString());
        } catch (NumberFormatException e) {
            Log.e("", "error retriving api version" + e.getMessage());
        }

        Global_Data.android_version=f.floatValue();

        return f.floatValue();
    }

}










