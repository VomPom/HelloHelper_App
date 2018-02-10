package com.xrone.julis.compous.Application.translate;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.AppCompatButton;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.xrone.julis.compous.Application.translate.view.ITranslateView;
import com.xrone.julis.compous.R;
import com.xrone.julis.compous.Utils.MyAlert;
import com.xrone.julis.compous.Utils.NetworkStatus;
import com.xrone.julis.compous.Utils.Speaker;
import com.xrone.julis.compous.Utils.TransLaterUtilts;
import com.xrone.julis.compous.Utils.TransLatorCallback;
import com.xrone.julis.compous.model.TranslateResultModel;
import com.xrone.julis.compous.Application.translate.voice.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

/**
 * Created by Julis on 2017/11/11.
 */

public class TranslateActivity extends Activity implements ITranslateView {


    @BindView(R.id.app_btn_translate)AppCompatButton btnTranslate;
    @BindView(R.id.btn_tran_clear)AppCompatButton btn_Clear;
    @BindView(R.id.et_main_input) EditText tranText;
    @BindView(R.id.btn_voice)ImageView voice;
    @BindView(R.id.btn_share)ImageView btn_share;
    @BindView(R.id.text_view_output)TextView textViewResult;
    @BindView(R.id.progress_bar)ProgressBar progressBar;
    @BindView(R.id.include)View viewResult;

    private Speaker speaker;
    SpeechSynthesizer mTts;

    private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();
    private StringBuilder sb = new StringBuilder();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.translate_main);
        ButterKnife.bind(this);
        initViews();
        lunch(getIntent());

    }
    public void initViews(){
        mTts = SpeechSynthesizer.createSynthesizer(this, null);
        speaker=new Speaker(this);
    }

    @OnClick(R.id.app_btn_translate)
    void OnClickTranslate(){
        if (!NetworkStatus.isNetworkAvailable(TranslateActivity.this)) {
            MyAlert.showErroNetworking(TranslateActivity.this);
        } else if (tranText.getText() == null || tranText.getText().length() == 0) {
            Snackbar.make(btnTranslate, "No input!", Snackbar.LENGTH_SHORT)
                    .show();
        } else {
            translate();
        }
    }
    @OnClick(R.id.btn_tran_clear)
    void OnClickClear() {
        tranText.setText("");
    }


    @OnClick({R.id.btn_share,R.id.btn_voice,R.id.btn_sounder,R.id.btn_copy})
    void OnImageClick(ImageView view){
        switch (view.getId()){
            case R.id.btn_share:
                btn_share();
                break;
            case R.id.btn_voice:
                btn_voice();
                break;
            case R.id.btn_copy:
                btn_copy();
                break;
            case R.id.btn_sounder:
                speaker.startSpeaking(textViewResult.getText().toString());
                break;
        }
    }



    /**
     * 启动筛选
     * @param intent
     */
    public void lunch(Intent intent){//判断text
        String data=intent.getStringExtra("text");
        if(data.equals("order")){
            initDialog();
        }else if(data.equals("sms")){
            String mes=intent.getStringExtra("mes");
            tranText.setText(mes);
            translate();
        }else if(data.equals("home")){
            //Nothing.
        }else{
            tranText.setText(data);
            translate();
        }
    }


    //利用科大讯飞自带界面
    private  void initDialog() {
        sb.delete(0, sb.length());
        RecognizerDialog mDialog = new RecognizerDialog(this, code -> {
           // Log.d(TAG, "SpeechRecognizer init() code = " + code);
            if (code != ErrorCode.SUCCESS) {
                System.out.println("初始化失败，错误码：" + code);
            }
        });

        mDialog.setUILanguage(Locale.US);
       mDialog.setParameter(SpeechConstant.LANGUAGE, "en_us");

        mDialog.setListener(new RecognizerDialogListener() {
            @Override
            public void onResult(com.iflytek.cloud.RecognizerResult recognizerResult, boolean b) {
                printResult(recognizerResult);
            }
            @Override
            public void onError(SpeechError speechError) {
                Log.e("tt","gs");
            }
        });
        mDialog.show();
    }

    /**
     *打印翻译的结果，并翻译文本
     * @param results
     */
    private void printResult(RecognizerResult results) {
        String text = JsonParser.parseIatResult(results.getResultString());
        String sn = null;
        // 读取json结果中的sn字段
        try {
            JSONObject resultJson = new JSONObject(results.getResultString());
            sn = resultJson.optString("sn");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mIatResults.put(sn, text);
        StringBuffer resultBuffer = new StringBuffer();
        for (String key : mIatResults.keySet()) {
            resultBuffer.append(mIatResults.get(key));
        }
        tranText.setText(resultBuffer.toString());
        translate();
    }
    @Override
    protected void onPause() {
        super.onPause();
        if(mTts!=null){
            mTts.destroy();
        }

    }
    @Override
    protected void onStop() {
        super.onStop();
        if(mTts!=null){
            mTts.destroy();
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mTts!=null){
            mTts.destroy();
        }
    }
    @Override
    public  void translate() {
        viewResult.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        TransLaterUtilts.getData(TranslateActivity.this, tranText.getText().toString().replace("\n",""), new TransLatorCallback() {
            @Override
            public void setPropety(TranslateResultModel resultModel) {
                progressBar.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.GONE);
                viewResult.setVisibility(View.VISIBLE);
                if(resultModel.getFrom().toString().equals("en")){
                    speaker.setSpeakLanguage(Speaker.CHINESE_TYPE);
                }else if(resultModel.getFrom().toString().equals("zh")){
                    speaker.setSpeakLanguage(Speaker.ENGLISH_TYPE);
                }
                textViewResult.setText(resultModel.getDst());
            }
        });
    }
    @Override
    public void btn_share() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND).setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT,
                "From:\n"+tranText.getText().toString()+"\n"
                        +"To:\n"+textViewResult.getText().toString()
                        +"\n----From HelloHelper.");
        startActivity(Intent.createChooser(intent,"Choos application to Share."));

    }
    @Override
    public void btn_voice() {
        if (NetworkStatus.isNetworkAvailable(getBaseContext())) {
            initDialog();
        } else {
            MyAlert.showErroNetworking(TranslateActivity.this);
        }
        mIatResults.clear();
    }
    @Override
    public void btn_copy() {
        ClipboardManager manager = (ClipboardManager) getBaseContext().getSystemService(CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("text", textViewResult.getText().toString());
        manager.setPrimaryClip(clipData);
        Snackbar.make(viewResult,"Copy success!" +
                "",Snackbar.LENGTH_SHORT)
                .show();

    }
}
