package com.xrone.julis.compous.view.application.map.navigation;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.amap.api.navi.AMapNaviListener;
import com.amap.api.navi.model.AMapLaneInfo;
import com.amap.api.navi.model.AMapNaviCameraInfo;
import com.amap.api.navi.model.AMapNaviCross;
import com.amap.api.navi.model.AMapNaviInfo;
import com.amap.api.navi.model.AMapNaviLocation;
import com.amap.api.navi.model.AMapNaviTrafficFacilityInfo;
import com.amap.api.navi.model.AMapServiceAreaInfo;
import com.amap.api.navi.model.AimLessModeCongestionInfo;
import com.amap.api.navi.model.AimLessModeStat;
import com.amap.api.navi.model.NaviInfo;
import com.autonavi.tbt.TrafficFacilityInfo;
import com.xrone.julis.compous.StringData.StringOfID;
import com.xrone.julis.compous.Utils.MD5;
import com.xrone.julis.compous.view.application.translate.util.ReplaceABC;
import com.xrone.julis.compous.view.application.translate.util.UrlString;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.LinkedList;

/**
 * 当前DEMO的播报方式是队列模式。其原理就是依次将需要播报的语音放入链表中，播报过程是从头开始依次往后播报。
 * <p>
 * 导航SDK原则上是不提供语音播报模块的，如果您觉得此种播报方式不能满足你的需求，请自行优化或改进。
 */
public class TTSController implements AMapNaviListener, ICallBack {


    private String textAfter;
    @Override
    public void onCompleted(int code) {
        if (handler != null) {
            handler.obtainMessage(1).sendToTarget();
        }
    }

    public static enum TTSType {
        /**
         * 讯飞语音
         */
        IFLYTTS,
        /**
         * 系统语音
         */
        SYSTEMTTS;
    }

    public static TTSController ttsManager;
    private Context mContext;
    private TTS tts = null;
    private SystemTTS systemTTS;
    private IFlyTTS iflyTTS = null;
    private LinkedList<String> wordList = new LinkedList<String>();
    private final int TTS_PLAY = 1;
    private final int CHECK_TTS_PLAY = 2;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case TTS_PLAY:
                    if (tts != null && wordList.size() > 0) {
                        tts.playText(wordList.removeFirst());
                    }
                    break;
                case CHECK_TTS_PLAY:
                    if (!tts.isPlaying()) {
                        handler.obtainMessage(1).sendToTarget();
                    }
                    break;
            }

        }
    };

    public void setTTSType(TTSType type) {
        if (type == TTSType.SYSTEMTTS) {
            tts = systemTTS;
        } else {
            tts = iflyTTS;
        }
        tts.setCallback(this);
    }

    private TTSController(Context context) {
        mContext = context.getApplicationContext();
        systemTTS = SystemTTS.getInstance(mContext);
        iflyTTS = IFlyTTS.getInstance(mContext);
        tts = iflyTTS;
    }

    public void init() {
        if (systemTTS != null) {
            systemTTS.init();
        }
        if (iflyTTS != null) {
            iflyTTS.init();
        }
        tts.setCallback(this);
    }

    public static TTSController getInstance(Context context) {
        if (ttsManager == null) {
            ttsManager = new TTSController(context);
        }
        return ttsManager;
    }

    public void stopSpeaking() {
        if (systemTTS != null) {
            systemTTS.stopSpeak();
        }
        if (iflyTTS != null) {
            iflyTTS.stopSpeak();
        }
        wordList.clear();
    }

    public void destroy() {
        if (systemTTS != null) {
            systemTTS.destroy();
        }
        if (iflyTTS != null) {
            iflyTTS.destroy();
        }
        ttsManager = null;
    }

    /****************************************************************************
     * 以下都是导航相关接口
     ****************************************************************************/


    @Override
    public void onArriveDestination() {
    }

    @Override
    public void onArrivedWayPoint(int arg0) {
    }

    @Override
    public void onCalculateRouteFailure(int arg0) {
        if (wordList != null)
            wordList.addLast("路线规划失败");
    }

    @Override
    public void onEndEmulatorNavi() {
    }

    @Override
    public void onGetNavigationText(int arg0, String arg1) {


    }


    @Override
    public void onInitNaviFailure() {
    }

    @Override
    public void onInitNaviSuccess() {
    }

    @Override
    public void onLocationChange(AMapNaviLocation arg0) {
    }

    @Override
    public void onReCalculateRouteForTrafficJam() {
        if (wordList != null)
            wordList.addLast("前方路线拥堵，路线重新规划");
    }

    @Override
    public void onReCalculateRouteForYaw() {
        if (wordList != null)
            wordList.addLast("路线重新规划");
    }

    @Override
    public void onStartNavi(int arg0) {
    }

    @Override
    public void onTrafficStatusUpdate() {
    }

    @Override
    public void onGpsOpenStatus(boolean enabled) {
    }

    @Override
    public void onNaviInfoUpdate(NaviInfo naviinfo) {

    }

    @Override
    public void onNaviInfoUpdated(AMapNaviInfo aMapNaviInfo) {

    }

    @Override
    public void updateCameraInfo(AMapNaviCameraInfo[] infoArray) {

    }

    @Override
    public void onServiceAreaUpdate(AMapServiceAreaInfo[] infoArray) {

    }

    @Override
    public void showCross(AMapNaviCross aMapNaviCross) {

    }

    @Override
    public void hideCross() {

    }

    @Override
    public void showLaneInfo(AMapLaneInfo[] laneInfos, byte[] laneBackgroundInfo, byte[] laneRecommendedInfo) {

    }


    @Override
    public void hideLaneInfo() {

    }

    @Override
    public void onCalculateRouteSuccess(int[] routeIds) {

    }

    @Override
    public void notifyParallelRoad(int parallelRoadType) {

    }

    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo aMapNaviTrafficFacilityInfo) {

    }

    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo[] infos) {

    }


    @Override
    public void updateAimlessModeStatistics(AimLessModeStat aimLessModeStat) {

    }

    @Override
    public void updateAimlessModeCongestionInfo(AimLessModeCongestionInfo aimLessModeCongestionInfo) {

    }

    @Override
    public void onPlayRing(int type) {

    }


    /**
     * 获取到要播报的文字
     *
     * @param playText
     */
    @Override
    public void onGetNavigationText(String playText) {
        transLateText(playText);
        if (wordList != null) {

        }
        handler.obtainMessage(CHECK_TTS_PLAY).sendToTarget();
    }

    @Override
    @Deprecated
    public void OnUpdateTrafficFacility(TrafficFacilityInfo arg0) {

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
        try {
            MD5 getMD5 = new MD5();
            s2 = URLEncoder.encode(playText, "utf-8");
            s1 = ReplaceABC.mReplace(s3, "+", " ");
            MD5string = getMD5.GetMD5Code(StringOfID.BAIDU_APP_ID + s1 + salt + StringOfID.BAIDU_KEY);
            mUrl = UrlString.mUrlNOChangeForward + s2
                    + UrlString.mUrlNOChangeBehind + StringOfID.BAIDU_APP_ID + "&salt=" + salt + "&sign=" + MD5string;

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
                        textAfter = afterText.toString();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return params[0];
                }

                @Override
                protected void onPostExecute(String result) {
                    // TODO Auto-generated method stub
                    Log.e("er:",textAfter);
                }
            }.execute(mUrl);
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


}
