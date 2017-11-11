package com.xrone.julis.compous.view.application.translate;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.xrone.julis.compous.Utils.MyAlert;
import com.xrone.julis.compous.Utils.Speaker;
import com.xrone.julis.compous.Utils.TransLaterUtilts;
import com.xrone.julis.compous.Utils.TransLateresults;
import com.xrone.julis.compous.model.TranslateResultModel;
import com.xrone.julis.compous.Utils.NetworkStatus;


import org.json.JSONException;
import org.json.JSONObject;
import com.xrone.julis.compous.R;
import com.xrone.julis.compous.view.application.translate.voice.JsonParser;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;

public class BDTranslator extends Activity {
	private StringBuilder sb = new StringBuilder();
	private ImageView voice;
	private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();
	private final String TAG = "Xrone";
	private EditText edTextMword;
	private Button btnFanyi;
	private TextView tvTextAfter;
	private ImageView souder;
	private String mWord;
	private ImageView copy;
	private static String data =null;
	SpeechSynthesizer mTts;
	Speaker speaker;

	@Override
	protected void onPause() {
		super.onPause();
		this.finish();
		if(mTts!=null){
			mTts.destroy();
		}

	}

	@Override
	protected void onStop() {
		super.onStop();
		this.finish();
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
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.app_tanslate);
		initVeiws();
		init();
		lunch(getIntent());
	}
	private void init(){
		mTts = SpeechSynthesizer.createSynthesizer(this, null);
		speaker=new Speaker(this);
		if (NetworkStatus.isNetworkAvailable(this)) {
		} else {
			Toast.makeText(getApplicationContext(), "当前网络不可用！\n" +
					"The current network is unavailable!",Toast.LENGTH_LONG).show();
		}
		SpeechUtility.createUtility(this, "appid=594367c5");
	}
	/**
	 * 初始化界面
	 */
	private void initVeiws() {

		edTextMword = (EditText) findViewById(R.id.ed_text_mword);
		btnFanyi = (Button) findViewById(R.id.btn_fanyi);
		tvTextAfter = (TextView) findViewById(R.id.tv_text_english);
		copy=(ImageView)findViewById(R.id.iv_copy);
		souder=(ImageView)findViewById(R.id.iv_sounder);
		voice = (ImageView) findViewById(R.id.iv_voice);


		//设置按下说话事件
		souder.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (NetworkStatus.isNetworkAvailable(getBaseContext())) {
					speaker.startSpeaking(tvTextAfter.getText().toString());
				} else {
					MyAlert.showErroNetworking(BDTranslator.this);
				}
			}
		});
		//翻译按钮
		btnFanyi.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (NetworkStatus.isNetworkAvailable(getBaseContext())) {
					goToFanyi();

					souder.setVisibility(View.VISIBLE);
				} else {
					MyAlert.showErroNetworking(BDTranslator.this);
				}
			}

		});
		//复制
		copy.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
				cm.setText(tvTextAfter.getText());
				Toast.makeText(getBaseContext(), "复制成功，可以发给朋友们了。\n" +
						"The copy is successful and can be sent to friends.", Toast.LENGTH_LONG).show();
			}
		});

		voice.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (NetworkStatus.isNetworkAvailable(getBaseContext())) {
					initDialog();
				} else {
					MyAlert.showErroNetworking(BDTranslator.this);
				}

				mIatResults.clear();
			}
		});

		edTextMword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				return (event.getKeyCode()==KeyEvent.KEYCODE_ENTER);
			}
		});
		edTextMword.setOnKeyListener(new View.OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				boolean flag=false;
				if(keyCode == KeyEvent.KEYCODE_ENTER){
					if (edTextMword.getText().toString().equals("")) {
						flag=true;
						Toast.makeText(getBaseContext(), "请输入要翻译的内容！\n" +
								"Please enter what you want to translate!", Toast.LENGTH_SHORT).show();
					}else {
						goToFanyi();

					}
				}
				return  flag;
			}
		});

	}

	/**
	 * 点击按钮执行字符串拼接为下一步翻译做准备
	 */
	private void goToFanyi(){
		mWord = edTextMword.getText().toString().trim().replace("\n","");
		if (mWord.equals(null) || mWord.equals("")) {
			Toast.makeText(getApplication(), "请输入要翻译的内容\n" +
					"Please enter what you want to translate", Toast.LENGTH_SHORT).show();
		} else {
			edTextMword.setSelection(edTextMword.getText().toString().length());
			TransLaterUtilts.getData(getBaseContext(), mWord, new TransLateresults() {
				@Override
				public void setPropety(TranslateResultModel resultModel) {
					tvTextAfter.setText(resultModel.getDst());
					if(resultModel.getFrom().toString().equals("en")){
						speaker.setSpeakLanguage(Speaker.CHINESE_TYPE);
					}else if(resultModel.getFrom().toString().equals("zh")){
						speaker.setSpeakLanguage(Speaker.ENGLISH_TYPE);
					}
				}
			});

		}
	}

	/**
	 * 启动筛选
	 * @param intentt
	 */
	public void lunch(Intent intentt){//判断text
		data=intentt.getStringExtra("text");
		if(data.equals("translate")){

		}else if(data.equals("order")){
			initDialog();
		}else if(data.equals("sms")){
			String mes=intentt.getStringExtra("mes");
			edTextMword.setText(mes);
			goToFanyi();
			souder.setVisibility(View.VISIBLE);
		}else{
			edTextMword.setText(data);
			goToFanyi();
			souder.setVisibility(View.VISIBLE);
		}
	}

	//利用科大讯飞自带界面
	private  void initDialog() {
		sb.delete(0, sb.length());
		RecognizerDialog mDialog = new RecognizerDialog(this, new InitListener() {
			@Override
			public void onInit(int code) {
				Log.d(TAG, "SpeechRecognizer init() code = " + code);
				if (code != ErrorCode.SUCCESS) {
					System.out.println("初始化失败，错误码：" + code);
				}
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
		edTextMword.setText(resultBuffer.toString());
		goToFanyi();
		souder.setVisibility(View.VISIBLE);
	}


}