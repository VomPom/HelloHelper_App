package com.xrone.julis.compous.view.application.translate;

import android.app.ActionBar;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.xrone.julis.compous.R;
import com.xrone.julis.compous.Utils.MyAlert;
import com.xrone.julis.compous.Utils.NetworkStatus;
import com.xrone.julis.compous.Utils.Speaker;
import com.xrone.julis.compous.Utils.TransLaterUtilts;
import com.xrone.julis.compous.Utils.TransLatorCallback;
import com.xrone.julis.compous.model.TranslateResultModel;
import com.xrone.julis.compous.view.application.translate.voice.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;

/**
 * Created by Julis on 2017/11/11.
 */

public class Translate extends Activity {

    private EditText editText;
    private AppCompatButton btn_Clear;
    private AppCompatButton button;
    private View viewResult;
    private TextView textViewResult;
    private ProgressBar progressBar;
    private Speaker speaker;

    SpeechSynthesizer mTts;
    private ImageView voice;
    private ImageView btn_share;
    private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();
    private StringBuilder sb = new StringBuilder();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.translate_main);

        initViews();
        events();
        lunch(getIntent());

    }
    public void initViews(){
        mTts = SpeechSynthesizer.createSynthesizer(this, null);
        speaker=new Speaker(this);
        editText = (EditText) findViewById(R.id.et_main_input);
        btn_Clear = (AppCompatButton) findViewById(R.id.btn_tran_clear);
        // 初始化清除按钮，当没有输入时是不可见的
        btn_Clear.setVisibility(View.INVISIBLE);
        progressBar = (ProgressBar)findViewById(R.id.progress_bar);
        voice=(ImageView)findViewById(R.id.btn_voice);
        btn_share=(ImageView)findViewById(R.id.btn_share);
        viewResult = findViewById(R.id.include);
        textViewResult = (TextView)findViewById(R.id.text_view_output);
        button = (AppCompatButton) findViewById(R.id.buttonTranslate);
    }

    public void events(){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!NetworkStatus.isNetworkAvailable(Translate.this)) {
                    MyAlert.showErroNetworking(Translate.this);
                } else if (editText.getText() == null || editText.getText().length() == 0) {
                    Snackbar.make(button, "No input!", Snackbar.LENGTH_SHORT)
                            .show();
                } else {
                    trans();
                }
            }
        });

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (editText.getEditableText().toString().length() != 0){
                    btn_Clear.setVisibility(View.VISIBLE);
                    editText.setSelection(editText.getText().toString().length());
                } else {
                    btn_Clear.setVisibility(View.INVISIBLE);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        btn_Clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText("");
            }
        });
        viewResult.findViewById(R.id.btn_sounder).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                speaker.startSpeaking(textViewResult.getText().toString());
            }
        });


        btn_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND).setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT,
                        "From:\n"+editText.getText().toString()+"\n"
                                +"To:\n"+textViewResult.getText().toString()
                                +"\n----From HelloHelper.");
               startActivity(Intent.createChooser(intent,"Choos application to Share."));
            }
        });




        viewResult.findViewById(R.id.btn_copy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager manager = (ClipboardManager) getBaseContext().getSystemService(CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("text", textViewResult.getText().toString());
                manager.setPrimaryClip(clipData);
                Snackbar.make(button,"Copy success!" +
                        "",Snackbar.LENGTH_SHORT)
                        .show();

            }
        });
        voice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkStatus.isNetworkAvailable(getBaseContext())) {
                    initDialog();
                } else {
                    MyAlert.showErroNetworking(Translate.this);
                }

                mIatResults.clear();
            }
        });
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
            editText.setText(mes);
            trans();
        }else if(data.equals("home")){
            //Nothing.
        }else{
            editText.setText(data);
            trans();
        }
    }

    /**
     * 执行翻译
     */
    public void trans(){
        viewResult.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        TransLaterUtilts.getData(Translate.this, editText.getText().toString().replace("\n",""), new TransLatorCallback() {
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



    //利用科大讯飞自带界面
    private  void initDialog() {
        sb.delete(0, sb.length());
        RecognizerDialog mDialog = new RecognizerDialog(this, new InitListener() {
            @Override
            public void onInit(int code) {
               // Log.d(TAG, "SpeechRecognizer init() code = " + code);
                if (code != ErrorCode.SUCCESS) {
                    System.out.println("初始化失败，错误码：" + code);
                }
            }
        });

        mDialog.setUILanguage(Locale.US);
       // mDialog.setParameter(SpeechConstant.LANGUAGE, "en_us");

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
        editText.setText(resultBuffer.toString());
        trans();
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



}
