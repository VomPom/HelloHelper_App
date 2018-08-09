package com.xrone.julis.compous.Application.OCR;


import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.AccessToken;
import com.baidu.ocr.sdk.model.GeneralParams;
import com.baidu.ocr.sdk.model.GeneralResult;
import com.baidu.ocr.sdk.model.Word;

import com.xrone.julis.compous.AppBaseActivity;
import com.xrone.julis.compous.Application.OCR.camera.CameraActivity;
import com.xrone.julis.compous.Application.OCR.util.FileUtil;
import com.xrone.julis.compous.Application.translate.TranslateResultModel;

import com.xrone.julis.compous.R;
import com.xrone.julis.compous.StringData.StringOfID;
import com.xrone.julis.compous.Utils.MyAlert;
import com.xrone.julis.compous.Utils.Speaker;
import com.xrone.julis.compous.Utils.TransLaterUtilts;
import com.xrone.julis.compous.Utils.TransLatorCallback;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OCRActivity extends AppBaseActivity {
    private static final int REQUEST_CODE_PICK_IMAGE = 101;
    private static final int REQUEST_CODE_CAMERA = 102;
    private final String TAG="OCRActivity";
    private Speaker speaker;

    @BindView(R.id.camera_button) Button btn_goto_camera;

    @BindView(R.id.primary_btn_sounder) ImageView primary_btn_sounder;
    @BindView(R.id.primary_btn_share) ImageView primary_btn_share;
    @BindView(R.id.primary_btn_copy) ImageView primary_btn_copy;

    @BindView(R.id.after_btn_share) ImageView after_btn_share;
    @BindView(R.id.after_btn_copy) ImageView after_btn_copy;
    @BindView(R.id.after_btn_sounder) ImageView after_btn_sounder;

    @BindView(R.id.info_text_view) TextView primary_text_view;
    @BindView(R.id.text_view_tran) TextView after_tran_text_view;

    @BindView(R.id.primary_tran_info) LinearLayout view;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_ocr);

        ButterKnife.bind(this);
        init();
        GotoOCR();
    }

    /**
     * 初始化相关
     */
    private void init() {
        speaker=new Speaker(OCRActivity.this);

        // 初始化识别引擎
        initAccessTokenWithAkSk();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int ret = ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
            if (ret == PackageManager.PERMISSION_DENIED) {
                ActivityCompat.requestPermissions(OCRActivity.this,
                        new String[] {Manifest.permission.READ_PHONE_STATE},
                        100);
            }
        }
        initTitleBar(getBaseContext().getResources().getString(R.string.back),
                getBaseContext().getResources().getString(R.string.OCR), "", this);

    }

    /**
     * 点击进入OCR图片识别界面
     */
    private void GotoOCR(){
        Intent intent = new Intent(OCRActivity.this, CameraActivity.class);
        intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
                FileUtil.getSaveFile(getApplication()).getAbsolutePath());
        intent.putExtra(CameraActivity.KEY_CONTENT_TYPE,
                CameraActivity.CONTENT_TYPE_GENERAL);
        startActivityForResult(intent, REQUEST_CODE_CAMERA);
    }

    /**
     * 回调识别
     * @param filePath
     */
    private void recGeneral(String filePath) {
        GeneralParams param = new GeneralParams();
        param.setDetectDirection(true);
        param.setImageFile(new File(filePath));
        OCR.getInstance().recognizeGeneral(param, new OnResultListener<GeneralResult>() {
            @Override
            public void onResult(GeneralResult result) {
                StringBuilder sb = new StringBuilder();
                for (Word word : result.getWordList()) {
                    sb.append(word.getWords());
                    sb.append("\n");
                }


                String primary_text_string=sb.toString().replace("\n","");
                Log.e(TAG, String.valueOf(sb.length()));
                if(sb.length()==0){
                   MyAlert.AlertWithOK(OCRActivity.this, "Tips", "Sorry!Nothing has been found!", (dialog, which) -> {
                   });
                }

                primary_text_view.setText(primary_text_string);
                TransLaterUtilts.getData(OCRActivity.this,primary_text_string , new TransLatorCallback() {
                    @Override
                    public void setPropety(TranslateResultModel resultModel) {
                        after_tran_text_view.setText(resultModel.getDst());

                    }
                });

            }
            @Override
            public void onError(OCRError error) {
                primary_text_view.setText(error.getMessage());
            }
        });
    }



    @OnClick(R.id.camera_button)
    void GoToCamera() {
        GotoOCR();
    }

    @OnClick({R.id.primary_btn_copy,
            R.id.primary_btn_share,
            R.id.primary_btn_sounder,
            R.id.after_btn_copy,
            R.id.after_btn_share,
            R.id.after_btn_sounder})

    void OnOCRInfoClick(ImageView view){
        switch (view.getId()){
            case R.id.primary_btn_copy:
                btn_copy(primary_text_view);break;
            case R.id.primary_btn_share:
                btn_share(primary_text_view);
                break;
            case R.id.primary_btn_sounder:
                btn_voice(primary_text_view,2);
                break;

            case R.id.after_btn_copy:
                btn_copy(after_tran_text_view);
                break;
            case R.id.after_btn_sounder:
                btn_voice(after_tran_text_view,1);
                break;
            case R.id.after_btn_share:
                btn_share(after_tran_text_view);
                break;

        }
    }

    /**
     * @param textViewResult
     */

    public void btn_copy(TextView textViewResult) {
        ClipboardManager manager = (ClipboardManager) getBaseContext().getSystemService(CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("text", textViewResult.getText().toString());
        manager.setPrimaryClip(clipData);
        Snackbar.make(view,"Copy success!" +
                "",Snackbar.LENGTH_SHORT).show();
    }

    /**
     * 分享
     * @param textViewResult
     */

    public void btn_share(TextView textViewResult) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND).setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT,
                textViewResult.getText().toString()+"\n" +"\n----From HelloHelper.");
        startActivity(Intent.createChooser(intent,"Choose an application to Share."));

    }

    public void btn_voice(TextView textViewResult,int LangeUageType) {
        speaker.setSpeakLanguage(LangeUageType);
        speaker.startSpeaking(textViewResult.getText().toString());
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            String filePath = getRealPathFromURI(uri);
            recGeneral(filePath);
        }

        if (requestCode == REQUEST_CODE_CAMERA && resultCode == Activity.RESULT_OK) {
            recGeneral(FileUtil.getSaveFile(getApplicationContext()).getAbsolutePath());
        }
    }

    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }
    private void initAccessTokenWithAkSk() {
        OCR.getInstance().initAccessTokenWithAkSk(new OnResultListener<AccessToken>() {
            @Override
            public void onResult(AccessToken result) {
                String token = result.getAccessToken();
            }

            @Override
            public void onError(OCRError error) {
                error.printStackTrace();
//                alertText("AK，SK方式获取token失败", error.getMessage());
            }
        }, getApplicationContext(), StringOfID.BAIDU_OCR_API_KEY, StringOfID.BAIDU_OCR_SECRET_KEY);
    }

    @Override
    protected void onPause() {
        super.onPause();
        speaker.closeMtts();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 释放内存资源
        OCR.getInstance().release();

    }
}
