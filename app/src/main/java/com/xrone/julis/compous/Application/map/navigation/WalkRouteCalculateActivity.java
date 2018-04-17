package com.xrone.julis.compous.Application.map.navigation;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.TextView;

import com.amap.api.navi.AMapNaviView;
import com.amap.api.navi.enums.NaviType;
import com.amap.api.navi.model.NaviLatLng;
import com.xrone.julis.compous.R;
import com.xrone.julis.compous.Utils.MyAlert;
import com.xrone.julis.compous.Utils.Speaker;
import com.xrone.julis.compous.StringData.StringOfID;
import com.xrone.julis.compous.Utils.MD5;
import com.xrone.julis.compous.Application.translate.util.ReplaceABC;
import com.xrone.julis.compous.Application.translate.util.UrlString;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;


public class WalkRouteCalculateActivity extends BaseActivity {

    private  int SPEED_EMU=580;
    private Speaker speaker;
    public static TextView navigationTextView;
    private String textAfter;

    @Override
    public void onGetNavigationText(String s) {
        //播报类型和播报文字回调
        super.onGetNavigationText(s);

    }
    @Override
    public void onGetNavigationText(int type, String text) {
        transLateText(text);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_navi);
        mAMapNaviView = (AMapNaviView) findViewById(R.id.navi_view);
        mAMapNaviView.onCreate(savedInstanceState);

        initViews();
        init();
    }
    public void init(){
        speaker=new Speaker(this);
        mAMapNaviView.setAMapNaviViewListener(this);
        mAMapNaviView.setNaviMode(AMapNaviView.NORTH_UP_MODE);
        aMapNaviViewOptions= mAMapNaviView.getViewOptions();
        //设置是否自动改变缩放等级
        //aMapNaviViewOptions.setAutoChangeZoom(true);
        //设置导航界面UI是否显示。
        aMapNaviViewOptions.setLayoutVisible(true);
        // 设置是否自动画路
        //aMapNaviViewOptions.setAutoDrawRoute(true);
        //设置路线上的摄像头气泡是否显示
       // aMapNaviViewOptions.setCameraBubbleShow(true);
        //设置摄像头播报是否打开（只适用于驾车导航）。
       // aMapNaviViewOptions.setCameraBubbleShow(true);
        //设置是否显示路口放大图(实景图)
       // aMapNaviViewOptions.setRealCrossDisplayShow(true);

        //添加导航视图设置
        mAMapNaviView.setViewOptions(aMapNaviViewOptions);
    }
    public void initViews(){
        navigationTextView=(TextView)findViewById(R.id.navigation_text);
    }

    @Override
    public void onInitNaviSuccess() {
        super.onInitNaviSuccess();
        //获取传入的当前位置和目标位置参数
        Intent intent=getIntent();
        double myLatitude=intent.getDoubleExtra("myLatitude",0);
        double myLongtitude=intent.getDoubleExtra("myLongitude",0);
        double endLatitude=intent.getDoubleExtra("desLatitude",0);
        double endLongtitude=intent.getDoubleExtra("desLongtitude",0);

        mAMapNavi.calculateWalkRoute(new NaviLatLng(myLatitude, myLongtitude), new NaviLatLng(endLatitude, endLongtitude));

    }

    @Override
    public void onStartNavi(int type) {
        super.onStartNavi(type);
    }

    @Override
    public void onEndEmulatorNavi() {
        new MyAlert(WalkRouteCalculateActivity.this, getString(R.string.map_navi_is_end), getString(R.string.map_navi_is_end_OK)) {
            @Override
            public void onClickNo() {
            }
            @Override
            public void onClickYes() {
                finish();
            }
        }.showMyAlert();
        super.onEndEmulatorNavi();
    }

    @Override
    public void onCalculateRouteSuccess(int[] ids) {
        super.onCalculateRouteSuccess(ids);
        //mAMapNavi.startNavi(NaviType.GPS);
        //模拟导航

        mAMapNavi.startNavi(NaviType.EMULATOR);
        mAMapNavi.setEmulatorNaviSpeed(SPEED_EMU);
    }

    /**
     * 翻译文本
     * @param playText
     */
    public void transLateText(String playText) {
        String s1;
        String s2;
        String s3;
        String mUrl;
        String MD5string;
        String salt = String.valueOf(System.currentTimeMillis());
        s3 = playText;
        if(s3==""||s3.equals("")||s3==null||s3.equals("null")){
            s3="开始";
        }

        try {
            MD5 getMD5 = new MD5();
            s2 = URLEncoder.encode(playText, "utf-8");
            s1 = ReplaceABC.mReplace(s3, "+", " ");
            MD5string = getMD5.GetMD5Code(StringOfID.BAIDU_APP_ID + s1 + salt + StringOfID.BAIDU_KEY);
            mUrl = UrlString.mUrlNOChangeForward + s2
                    + UrlString.mUrlNOChangeBehind + StringOfID.BAIDU_APP_ID  + "&salt=" + salt + "&sign=" + MD5string;
            new AsyncTask<String, Void, String>() {
                @Override
                protected String doInBackground(String... params) {
                    try {
                        URL url = new URL(params[0]);
                        HttpURLConnection connection = (HttpURLConnection) url
                                .openConnection();
                        InputStream is = connection.getInputStream();
                        InputStreamReader isr = new InputStreamReader(is, "utf-8");
                        BufferedReader bf = new BufferedReader(isr);
                        String line;
                        StringBuffer sb = new StringBuffer();
                        while ((line = bf.readLine()) != null) {
                            //System.out.println(line);
                            sb.append(line);
                        }
                        bf.close();
                        isr.close();
                        is.close();
                        JSONObject jsonObject = new JSONObject(sb.toString());
                        JSONArray trans_result = jsonObject
                                .getJSONArray("trans_result");
                        StringBuffer afterText = new StringBuffer();
                        for (int i = 0; i < trans_result.length(); i++) {
                            JSONObject jo = trans_result.optJSONObject(i);
                            afterText.append(jo.getString("dst"));
                        }

                        textAfter=afterText.toString();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return params[0];
                }

                @Override
                protected void onPostExecute(String result) {
                    // TODO Auto-generated method stub
                    if(textAfter==null||textAfter.equals("")){
                        textAfter="Start";
                    }
                    navigationTextView.setText(textAfter);
                    Log.e("English",textAfter);
                    speaker.startSpeaking(textAfter);
                }
            }.execute(mUrl);
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    @Override
    protected void onStop() {
       super.onStop();
       speaker.closeMtts();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) { //按下的如果是BACK，同时没有重复
            // showAlertDialog();
            new MyAlert(WalkRouteCalculateActivity.this, getString(R.string.map_navi_isOutOfNavigation), getString(R.string.map_navi_OutOfNavigation)) {
                @Override
                public void onClickNo() {
                }
                @Override
                public void onClickYes() {
                    finish();
                }
            }.showMyAlert();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}
