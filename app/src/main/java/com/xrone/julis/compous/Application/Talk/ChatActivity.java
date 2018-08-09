package com.xrone.julis.compous.Application.Talk;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.xrone.julis.compous.AppBaseActivity;
import com.xrone.julis.compous.Application.OCR.OCRActivity;
import com.xrone.julis.compous.Application.Talk.adapter.ChatAdapter;
import com.xrone.julis.compous.Application.Talk.bean.ChatMessage;
import com.xrone.julis.compous.Application.Talk.dp.ChatMsgDao;
import com.xrone.julis.compous.Application.Talk.view.DropdownListView;
import com.xrone.julis.compous.Application.translate.TranslateActivity;
import com.xrone.julis.compous.Application.translate.TranslateResultModel;
import com.xrone.julis.compous.Utils.MyAlert;
import com.xrone.julis.compous.Utils.NetworkStatus;
import com.xrone.julis.compous.Utils.Speaker;
import com.xrone.julis.compous.Utils.ToastUtil;
import com.xrone.julis.compous.Application.translate.voice.JsonParser;
import com.xrone.julis.compous.R;
import com.xrone.julis.compous.StringData.StringOfID;
import com.xrone.julis.compous.Utils.TransLaterUtilts;
import com.xrone.julis.compous.Utils.TransLatorCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChatActivity  extends AppBaseActivity implements DropdownListView.OnRefreshListenerHeader,  ChatAdapter.OnClickMsgListener{
    private final String LANGUAGE_ENGLISH_TYPE="en_us";
    private final String LANGUAGE_CHINESE_TYPE="zh_cn";
    private int offset;
    private final int SENT_TYPE=1;
    private final int RECEVIEVE_TYPE=0;
    private Speaker speaker;
    @BindView(R.id.chat_english) Button chat_english;
    @BindView(R.id.chat_chinese) Button chat_chinese;
    private final String TAG="ChatActivity";
    RecognizerDialog mDialog;

    private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();
    //消息
    private List<ChatMessage> listMsg=new ArrayList<ChatMessage>();
    private DropdownListView mListView;
    private ChatAdapter mLvAdapter;
    private ChatMsgDao msgDao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.app_chat);
        ButterKnife.bind(this);
        msgDao = new ChatMsgDao(this);
        //初始化语音听写及合成部分
        initViews(this);
        //初始化数据
        initData();


    }

    private void initViews(Context context){
        SpeechUtility.createUtility(context, "appid="+ StringOfID.XUNFEI_APP_ID);
        SpeechSynthesizer.createSynthesizer(context, null);
        mDialog = new RecognizerDialog(ChatActivity.this, code -> {
            if (code != ErrorCode.SUCCESS) {
               Log.e(TAG,"初始化失败，错误码：" + code);
            }
        });
        mListView = (DropdownListView) findViewById(R.id.message_chat_listview);

        initTitleBar("Back", "Chatting", "Clear", this);

        /**
         * 设置标题右边监听事件
         */
        super.tv_right.setOnClickListener(v -> {
            msgDao.deleteTableData();
            mLvAdapter.notifyDataSetChanged();
            onRefresh();
        });


    }

    @OnClick({R.id.chat_chinese,R.id.chat_english})
    void voice_and_sendMessage(View view){
        switch (view.getId()){
            case R.id.chat_chinese:
                startToSpeak(LANGUAGE_CHINESE_TYPE);
                break;
            case R.id.chat_english:
                startToSpeak(LANGUAGE_ENGLISH_TYPE);
                break;
        }
    }
    /**
     * 开始讲话
     */
    private  void startToSpeak(String type) {
        mDialog.setUILanguage(type.equals(LANGUAGE_CHINESE_TYPE)?Locale.CHINA:Locale.US);
        mDialog.setParameter(SpeechConstant.LANGUAGE, type);
        mDialog.setListener(new RecognizerDialogListener() {
            @Override
            public void onResult(com.iflytek.cloud.RecognizerResult recognizerResult, boolean b) {
                String recognizerResultString=getListenResults(recognizerResult);
                Log.e(TAG,recognizerResultString);  if(b==true){
                    switch (type){
                        case LANGUAGE_CHINESE_TYPE:
                            chat_left_of_Chinese(recognizerResultString);
                            break;
                        case LANGUAGE_ENGLISH_TYPE:
                            chat_right_of_English(recognizerResultString);
                            break;
                    }

                }
            }
            @Override
            public void onError(SpeechError speechError) {
                Log.e("tt","gs");
            }
        });
        mDialog.show();
    }

    /**
     * 英语
     */
    private void chat_right_of_English(String text) {
        sendMsgText(text,SENT_TYPE);
    }

    /**
     * 中文
     */
    private void chat_left_of_Chinese(String text) {
        sendMsgText(text,RECEVIEVE_TYPE);
    }



    /**
     * 获取识别后的文字结果
     * @param results
     * @return
     */

    private String getListenResults(RecognizerResult results) {
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
        return resultBuffer.toString();
    }

    void sendMsgText(String content,int type) {
        if (content.endsWith("##")) {
            ToastUtil.show(this, "输入有误");
            return;
        }
        ChatMessage chatMessage = getChatInfoTo(content,type);


        TransLaterUtilts.getData(getBaseContext(), content, new TransLatorCallback() {
            @Override
            public void setPropety(TranslateResultModel resultModel) {
            chatMessage.setTran_content(resultModel.getDst());
            listMsg.add(chatMessage);
            /**
             * 插入数据
             */
            chatMessage.setMsgId(msgDao.insert(chatMessage));
            mLvAdapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * 发送的信息
     * from为收到的消息，to为自己发送的消息
     *
     * @return
     */
    private ChatMessage getChatInfoTo(String message,int type) {
        String time = new SimpleDateFormat("MM-dd HH:mm:ss E").format(new Date());
        ChatMessage msg = new ChatMessage();
        msg.setContent(message);
        msg.setDate(time);
        msg.setIsComing(type==SENT_TYPE?1:0);
        return msg;
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    public void initData() {
        offset = 0;
        listMsg = msgDao.queryMsg(offset);
        offset = listMsg.size();
        speaker=new Speaker(ChatActivity.this);
        mLvAdapter = new ChatAdapter(this, listMsg, this);
        mListView.setAdapter(mLvAdapter);
        mListView.setSelection(listMsg.size());

    }


    @Override
    public void click(int position) {
        ChatMessage msg = listMsg.get(position);
        speaker.setSpeakLanguage(msg.getIsComing()==0?1:2);
        speaker.startSpeaking(msg.getTran_content());

    }

    @Override
    public void longClick(int position) {

    }
    @Override
    protected void onPause() {
        super.onPause();
        speaker.closeMtts();
    }


    @Override
    public void onRefresh() {
        Log.e(TAG,"刷新");
        List<ChatMessage> list = msgDao.queryMsg(offset);
        if (list.size() <= 0) {
            mListView.setSelection(0);
            mListView.onRefreshCompleteHeader();
            return;
        }
        listMsg.addAll(0, list);
        offset = listMsg.size();
        mListView.onRefreshCompleteHeader();
        mLvAdapter.notifyDataSetChanged();
        mListView.setSelection(list.size());
    }
}

