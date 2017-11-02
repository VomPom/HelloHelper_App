package com.xrone.julis.compous.Utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.SynthesizerListener;
import com.xrone.julis.compous.R;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by Julis on 2017/10/20.
 */

public class Speaker {
    public static final int ENGLISH_TYPE=1;
    public static final int CHINESE_TYPE=2;
    private SpeechSynthesizer mTts;
    Context context;
    public Speaker(Context context) {
        this.context=context;
        SpeechUtility.createUtility(context, "appid=594367c5");
        init(context);
    }

    public void closeMtts(){
        mTts.destroy();
    }

    public void setSpeakLanguage(int type){
        switch (type){
            case ENGLISH_TYPE:
                mTts.setParameter(SpeechConstant.LANGUAGE,"en");
                mTts.setParameter(SpeechConstant.VOICE_NAME, "catherine");//设置发音人
                break;
            case CHINESE_TYPE:
                mTts.setParameter(SpeechConstant.LANGUAGE,"ch");
                mTts.setParameter(SpeechConstant.VOICE_NAME, "xiaoyan");//设置发音人
                break;
        }
    }
    /***********语音合成*************/
    public void init(Context context) {
        //1.创建SpeechSynthesizer对象, 第二个参数：本地合成时传InitListener
        mTts = SpeechSynthesizer.createSynthesizer(context, null);
        //2.合成参数设置，详见《科大讯飞MSC API手册(Android)》SpeechSynthesizer 类
        mTts.setParameter(SpeechConstant.LANGUAGE,"en");
        mTts.setParameter(SpeechConstant.VOICE_NAME, "catherine");//设置发音人
        mTts.setParameter(SpeechConstant.SPEED, "50");//设置语速
        mTts.setParameter(SpeechConstant.VOLUME, "80");//设置音量，范围0~100
        mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD); //设置云端
        //设置合成音频保存位置（可自定义保存位置），保存在“./sdcard/iflytek.pcm”
        //保存在SD卡需要在AndroidManifest.xml添加写SD卡权限
        //如果不需要保存合成音频，注释该行代码
    }
    public void startSpeaking(String speechText){
        mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, "./sdcard/iflytek.pcm");
        //3.开始合成
        mTts.startSpeaking("" + speechText, new com.iflytek.cloud.SynthesizerListener() {
            //开始播放
            @Override
            public void onSpeakBegin() {

            }

            //缓冲进度回调
            //percent为缓冲进度0~100，beginPos为缓冲音频在文本中开始位置，endPos表示缓冲音频在文本中结束位置，info为附加信息。
            @Override
            public void onBufferProgress(int i, int i1, int i2, String s) {
            }

            //暂停播放
            @Override
            public void onSpeakPaused() {
            }

            //恢复播放回调接口
            @Override
            public void onSpeakResumed() {
            }

            //播放进度回调
            //percent为播放进度0~100,beginPos为播放音频在文本中开始位置，endPos表示播放音频在文本中结束位置.
            @Override
            public void onSpeakProgress(int i, int i1, int i2) {
            }

            //会话结束回调接口，没有错误时，error为null
            @Override
            public void onCompleted(SpeechError speechError) {
            }

            //会话事件回调接口
            @Override
            public void onEvent(int i, int i1, int i2, Bundle bundle) {
            }
        });
    }
}
