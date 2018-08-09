package com.xrone.julis.compous;

import android.app.Activity;
import android.app.Service;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.xrone.julis.compous.Utils.MyAlert;
import com.xrone.julis.compous.Utils.NetworkStatus;


public class AppBaseActivity extends Activity implements OnClickListener {

//    public FinalBitmap finalImageLoader;
//    public ImageLoader imageLoader;

    public TextView tv_left, tv_title, tv_right;
    public ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            SystemBarTintManager tintManager = new SystemBarTintManager(this);
//            tintManager.setStatusBarTintEnabled(true);
//            tintManager.setStatusBarTintResource(R.color.common_title_bg);//通知栏所需颜色
//        }
        super.onCreate(savedInstanceState);

//        finalImageLoader = FinalBitmap.create(this);
//        finalImageLoader.configDiskCachePath(new File(Environment.getExternalStorageDirectory(), "qrobot/images/cache").getAbsolutePath());
//        imageLoader = ImageLoader.getInstance();

    }

    /**
     * 隐藏软键盘
     * hideSoftInputView
     *
     * @param
     * @return void
     * @throws
     * @Title: hideSoftInputView
     */
    public void hideSoftInputView() {
        InputMethodManager manager = ((InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE));
        if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getCurrentFocus() != null)
                manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 弹出输入法窗口
     */
    public void showSoftInputView(final EditText et) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ((InputMethodManager) et.getContext().getSystemService(Service.INPUT_METHOD_SERVICE)).toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }, 0);
    }

    /**
     * 初始化titlebar，该方法只有在标题栏布局符合此规则时才能调用
     * @param left titlebar左按钮
     * @param title titlebar标题
     * @param right titlebar 右按钮
     * @param onClickListener 左右按钮点击事件
     */
    public void initTitleBar(String left, String title, String right, OnClickListener onClickListener){
        tv_left=(TextView) findViewById(R.id.tv_left);//返回按钮
        tv_title=(TextView) findViewById(R.id.tv_title);//标题
        tv_right=(TextView) findViewById(R.id.tv_right);//更多(右侧)按钮
        pb=(ProgressBar) findViewById(R.id.pb);// 标题栏数据加载ProgressBar
        Drawable drawable= getResources().getDrawable(R.drawable.ic_common_back_arrow_normal);
        /// 这一步必须要做,否则不会显示.
        drawable.setBounds(0, 0, drawable.getMinimumWidth()/2, drawable.getMinimumHeight()/2);
        tv_left.setCompoundDrawables(drawable,null,null,null);

        if(!TextUtils.isEmpty(left)){
            tv_left.setText(left);
            tv_left.setVisibility(View.VISIBLE);
            tv_left.setOnClickListener(onClickListener);
        }

        if(!TextUtils.isEmpty(title)){
            tv_title.setText(title);
        }

        if(!TextUtils.isEmpty(right)){
            tv_right.setText(right);
            tv_right.setVisibility(View.VISIBLE);
            tv_right.setOnClickListener(onClickListener);
        }
    }

    @Override
    public void finish() {
        super.finish();
//        overridePendingTransition(R.anim.push_in_left, R.anim.push_out_right);
    }

    /**
     * 如果子类支持点击左上角返回按钮返回，则在子类的onClick方法中需添加super.onClick(View view);
     */
    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.tv_left:
                finish();
                break;
        }
    }

    /**
     * 标题有
     * @param tv_right
     */
    public void onRightClick(TextView tv_right){
        this.tv_right=tv_right;
    }
    public void checkNetwok(){
        if (!NetworkStatus.isNetworkAvailable(getBaseContext())) {
            MyAlert.showErroNetworking(getBaseContext());
        }
    }

}
