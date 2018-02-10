package com.xrone.julis.compous;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.bumptech.glide.Glide;
import com.xrone.julis.compous.Utils.BottomNavigationViewHelper;
import com.xrone.julis.compous.Utils.CheckRequiredPermissionUtils;
import com.xrone.julis.compous.Utils.CheckVersion;
import com.xrone.julis.compous.HomeFragment.NavigationItem;
import com.xrone.julis.compous.HomeFragment.communication.SettingActivity;
import com.xrone.julis.compous.HomeFragment.communication.UserDetailActivity;
import com.xrone.julis.compous.HomeFragment.communication.listener.NavigationOpenClickListener;
import com.xrone.julis.compous.LoginAndRegister.LoginActivity;
import com.xrone.julis.compous.Application.FeedbackActivity;
import com.xrone.julis.compous.Application.about.AboutUsActivity;
import com.xrone.julis.compous.Application.exchangeRate.Data.Global_Data;
import com.xrone.julis.compous.Application.ApplicationFragment;
import com.xrone.julis.compous.HomeFragment.HomeFragment;
import com.xrone.julis.compous.HomeFragment.PersonFragment;
import com.xrone.julis.compous.model.Hello;
import com.xrone.julis.compous.person.view.CircleImageView;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MainActivity extends AppCompatActivity {
    private final int LOGIN_CODE=0;
    private final int PERSONINFO_CODE=1;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    /*
     * 导航部分的个人信息
     */
    @BindView(R.id.img_avatar)
    CircleImageView imgAvatar;
    @BindView(R.id.tv_login_name)
    TextView tvLoginName;
    @BindView(R.id.tv_score)
    TextView tvScore;
    @BindView(R.id.btn_logout)
    View btnLogout;
    @BindView(R.id.btn_theme_dark)
    ImageView imgThemeDark;
    @BindView(R.id.nav_top_background)
    View navTopBackground;
    /*
     * 抽屉导航布局
     */
    @BindView(R.id.main_drawer_layout)
    DrawerLayout drawerLayout;

    private android.app.FragmentTransaction transaction;
    private PersonFragment personFrag=new PersonFragment();
    private HomeFragment homeFrag = new HomeFragment();
    private ApplicationFragment applicationFrag= new ApplicationFragment();
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener;

    private CheckVersion checkVersion=new CheckVersion(this);


    /**
     * 静态代码块，执行底部导航的相关监听
     */
    {
        /**
         * 主控制界面
         */
        mOnNavigationItemSelectedListener = item -> {
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
        };
    }

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initData();
        initView();
        initFunction();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==PERSONINFO_CODE){

            if(data!=null){
                data.getStringExtra("cropImagePath");
                Bitmap bitmap=BitmapFactory.decodeFile(data.getStringExtra("cropImagePath"));
                imgAvatar.setImageBitmap(bitmap);
            }

        }

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
        toolbar.setNavigationIcon(R.drawable.com_home_menu_24dp);
        toolbar.setNavigationOnClickListener(new NavigationOpenClickListener(drawerLayout));
        // toolbar.setOnClickListener(new DoubleClickBackToContentTopListener(this));

        drawerLayout.setDrawerShadow(R.drawable.app_about_us, GravityCompat.START);
        drawerLayout.addDrawerListener(drawerListener);

    }


    @OnClick(R.id.layout_info)
    void onBtnInfoClick() {
        if (LoginActivity.checkLogin(this)) {
          // startActivityForResult(new Intent(this, PersonInfoView.class),PERSONINFO_CODE);
            UserDetailActivity.startWithTransitionAnimation(this, Hello.username, imgAvatar, Hello.head_url);

        }
    }

    @OnClick(R.id.btn_logout)
    void onBtnLogoutClick() {
        if(Hello.isLogin){
            Hello.isLogin=false;
            updateUserInfoViews();
        }
    }
    @OnClick({
            R.id.btn_nav_notification,
            R.id.btn_nav_setting,
            R.id.btn_nav_about,
            R.id.btn_nav_feedback
    })
    void onOtherNavigationItemClick(NavigationItem itemView) {
        Intent intent= null;
        Context context=MainActivity.this;
        switch (itemView.getId()) {
            case R.id.btn_nav_notification:

                break;
            case R.id.btn_nav_feedback:
                //settingAction.startDelayed();
                intent= new Intent(context,FeedbackActivity.class);
                break;
            case R.id.btn_nav_about:
               // aboutAction.startDelayed();
                intent= new Intent(context,AboutUsActivity.class);
                break;
            case R.id.btn_nav_setting:
                // aboutAction.startDelayed();
                intent= new Intent(context,SettingActivity.class);
                break;

        }
        drawerLayout.closeDrawers();
        startActivity(intent);
    }




    /**
     * 初始化一些功能
     */
    public void initFunction(){
        getAPIVerison();
        /**
         * 检查权限
         */
       new CheckRequiredPermissionUtils().checkRequiredPermission(this);
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
        Map<String,String> userInfo= Hello.getUserInfo(this);
        if(userInfo!=null&&userInfo.get("sex")!=null){
            Hello.id=userInfo.get("id");
            Hello.username=userInfo.get("name");
            Hello.sex=userInfo.get("sex");
            Hello.head_url=userInfo.get("head_url");
            Hello.isLogin= userInfo.get("is_login").equals("true")?true:false;
        }
        updateUserInfoViews();
    }

    /**
     * 检查是否登陆更新info
     */
    public void updateUserInfoViews(){

        if(Hello.isLogin==true){
            Glide.with(this)
                    .load(Hello.head_url)
                    .into(imgAvatar);

            tvLoginName.setText(Hello.username);
            btnLogout.setVisibility(View.VISIBLE);
        }else{
            tvLoginName.setText("点击登陆");
            tvScore.setText(null);
            imgAvatar.setImageDrawable(getResources().getDrawable(R.drawable.com_image_placeholder));
            btnLogout.setVisibility(View.GONE);
        }
    }

    /**
     * 替换Fragment
     * @param fragment fragment
     */
    public void replaceFragment(Fragment fragment) {
        transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.content,fragment);
        transaction.commit();
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


    private final DrawerLayout.DrawerListener drawerListener = new DrawerLayout.SimpleDrawerListener() {


        @Override
        public void onDrawerOpened(View drawerView) {
                   updateUserInfoViews();
      //      mainPresenter.getUserAsyncTask();
//            mainPresenter.getMessageCountAsyncTask();
        }

        @Override
        public void onDrawerClosed(View drawerView) {
        }
    };

}










