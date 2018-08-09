package com.xrone.julis.compous.person;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.xrone.julis.compous.AppBaseActivity;
import com.xrone.julis.compous.StringData.NetURL;
import com.xrone.julis.compous.StringData.Hello;
import com.xrone.julis.compous.R;
import com.xrone.julis.compous.person.view.CircleImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Julis on 17/6/20.
 */

public class PersonInfoView extends AppBaseActivity {
    //返回给点击头像图篇
    private final int PERSONINFO_CODE=1;

    //请求相机
    private static final int REQUEST_CAPTURE = 100;
    //请求相册
    private static final int REQUEST_PICK = 101;
    //请求截图
    private static final int REQUEST_CROP_PHOTO = 102;
    //请求访问外部存储
    private static final int READ_EXTERNAL_STORAGE_REQUEST_CODE = 103;
    //请求写入外部存储
    private static final int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 104;

    private ProgressDialog progressDialog;
    private Bitmap bitmap;
    private File tempFile;
    @BindView(R.id.person_chage_username_ed)
    EditText edUsername;
    @BindView(R.id.person_chage_sex)
    TextView sex;
    @BindView(R.id.person_chage_headImage)
    CircleImageView headImage;
    @BindView(R.id.info_change_userHead)
    RelativeLayout chageImageRelatLayout;

    private int type;

    @BindView(R.id.person_chage_username_tv)
    TextView userNameTV;
    @BindView(R.id.person_chage_submit)
    Button submit;
    @BindView(R.id.person_choose_sex)
    RelativeLayout chooseSex;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.person_moreinfor);
        ButterKnife.bind(this);
        initview();

        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Updating...wait...");
        // android 7.0系统解决拍照的问题
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();
        createCameraTempFile(savedInstanceState);
    }
    private void initview(){
        userNameTV.setText(Hello.username);
        edUsername.setText(Hello.username);
        Glide.with(this)
                .load(Hello.head_url)
                .into(headImage);
        initTitleBar(getBaseContext().getResources().getString(R.string.back),
                getBaseContext().getResources().getString(R.string.personal_infomation), "", this);

    }

    @OnClick(R.id.person_chage_username_tv)
    void click(){
        userNameTV.setVisibility(View.INVISIBLE);
        edUsername.setVisibility(View.VISIBLE);
    }
    @OnClick(R.id.person_chage_submit)
    void submit(){
        progressDialog.show();
        RequestQueue requestQueue= Volley.newRequestQueue(this);
        StringRequest request =new StringRequest(StringRequest.Method.POST, NetURL.PERSONALINFOCHANGE_URL, (String s) -> {
            Log.e("TAG", "volley onResponse--->>"+s );
            Hello.username=edUsername.getText().toString();
            SharedPreferences sp=this.getSharedPreferences("data",Context.MODE_PRIVATE);
            SharedPreferences.Editor editor=sp.edit();
            editor.putString("name", Hello.username);

            Glide.get(getBaseContext()).clearMemory();
            new Thread(){
                @Override
                public void run() {
                    Glide.get(getBaseContext()).clearDiskCache();
                }
            }.start();

            progressDialog.dismiss();
            finish();

        }, volleyError -> progressDialog.dismiss()
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map=new HashMap<>();
                map.put("user_id",Hello.id);
                map.put("username",edUsername.getText().toString());
                map.put("sex",Hello.sex);
                if(bitmap!=null){
                    Log.e("bitmap",Bitmap2String(bitmap));
                 map.put("headimage",Bitmap2String(bitmap));
                }
                return map;
            }
        };
        requestQueue.add(request);
    }



    @OnClick(R.id.info_change_userHead)
    void onclick(){
        type = 2;
        uploadHeadImage();
    }





    @OnClick(R.id.person_choose_sex)
    void sexChoose(){
        View view = LayoutInflater.from(this).inflate(R.layout.person_sex_select, null);
        TextView boy = (TextView) view.findViewById(R.id.person_boy);
        TextView girl = (TextView) view.findViewById(R.id.person_girl);
        TextView btnCancel = (TextView) view.findViewById(R.id.btn_cancel);
        final PopupWindow popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(getResources().getDrawable(android.R.color.transparent));
        popupWindow.setOutsideTouchable(true);
        View parent = LayoutInflater.from(this).inflate(R.layout.activity_main, null);
        popupWindow.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
        //popupWindow在弹窗的时候背景半透明
        final WindowManager.LayoutParams params = getWindow().getAttributes();
        params.alpha = 0.5f;
        getWindow().setAttributes(params);
        boy.setOnClickListener(v ->{
                    sex.setText(getString(R.string.sex_boy));
                     popupWindow.dismiss();
                });
        girl.setOnClickListener(v ->{
                    sex.setText(getString(R.string.sex_girl));
                    popupWindow.dismiss();
                });
        btnCancel.setOnClickListener(v ->popupWindow.dismiss());
        popupWindow.setOnDismissListener(() -> {
            params.alpha = 1.0f;
            getWindow().setAttributes(params);
        });
    }

    /**
     * 生成二进制文件
     * @param bm
     * @return
     */
    public String Bitmap2String(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] bytes = baos.toByteArray();
        String img=new String(Base64.encodeToString(bytes,Base64.DEFAULT));
        return img;
    }
    /**
     * 外吧存储权限申请返回
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == WRITE_EXTERNAL_STORAGE_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission Granted
                gotoCarema();
            } else {
                // Permission Denied
            }
        } else if (requestCode == READ_EXTERNAL_STORAGE_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission Granted
                gotoPhoto();
            } else {
                // Permission Denied
            }
        }
    }


    /**
     * 头像选择
     */
    private void uploadHeadImage() {
        View view = LayoutInflater.from(this).inflate(R.layout.layout_popupwindow, null);
        TextView btnCarema = (TextView) view.findViewById(R.id.btn_camera);
        TextView btnPhoto = (TextView) view.findViewById(R.id.btn_photo);
        TextView btnCancel = (TextView) view.findViewById(R.id.btn_cancel);
        final PopupWindow popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(getResources().getDrawable(android.R.color.transparent));
        popupWindow.setOutsideTouchable(true);
        View parent = LayoutInflater.from(this).inflate(R.layout.activity_main, null);
        popupWindow.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
        //popupWindow在弹窗的时候背景半透明
        final WindowManager.LayoutParams params = getWindow().getAttributes();
        params.alpha = 0.5f;
        getWindow().setAttributes(params);
        popupWindow.setOnDismissListener(() -> {
            params.alpha = 1.0f;
            getWindow().setAttributes(params);
        });

        btnCarema.setOnClickListener(v -> {
            //权限判断
            if (ContextCompat.checkSelfPermission(PersonInfoView.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                //申请WRITE_EXTERNAL_STORAGE权限
                ActivityCompat.requestPermissions(PersonInfoView.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
            } else {
                //跳转到调用系统相机
                gotoCarema();
            }
            popupWindow.dismiss();
        });
        btnPhoto.setOnClickListener(v -> {
            //权限判断
            if (ContextCompat.checkSelfPermission(PersonInfoView.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                //申请READ_EXTERNAL_STORAGE权限
                ActivityCompat.requestPermissions(PersonInfoView.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        READ_EXTERNAL_STORAGE_REQUEST_CODE);
            } else {
                //跳转到调用系统图库
                gotoPhoto();
            }
            popupWindow.dismiss();
        });
        btnCancel.setOnClickListener(v -> popupWindow.dismiss());
    }

    /**
     * 跳转到相册
     */
    private void gotoPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(intent, "请选择图片"), REQUEST_PICK);
    }


    /**
     * 跳转到照相机
     */
    private void gotoCarema() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
        startActivityForResult(intent, REQUEST_CAPTURE);
    }

    /**
     * 创建调用系统照相机待存储的临时文件
     *
     * @param savedInstanceState
     */
    private void createCameraTempFile(Bundle savedInstanceState) {
        if (savedInstanceState != null && savedInstanceState.containsKey("tempFile")) {
            tempFile = (File) savedInstanceState.getSerializable("tempFile");
        } else {
            tempFile = new File(checkDirPath(Environment.getExternalStorageDirectory().getPath() + "/image/"),
                    System.currentTimeMillis() + ".jpg");
        }
    }

    /**
     * 检查文件是否存在
     */
    private static String checkDirPath(String dirPath) {
        if (TextUtils.isEmpty(dirPath)) {
            return "";
        }
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dirPath;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("tempFile", tempFile);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {


        switch (requestCode) {
            case REQUEST_CAPTURE: //调用系统相机返回
                if (resultCode == RESULT_OK) {
                    gotoClipActivity(Uri.fromFile(tempFile));

                }
                break;
            case REQUEST_PICK:  //调用系统相册返回
                if (resultCode == RESULT_OK) {
                    Uri uri = intent.getData();
                    gotoClipActivity(uri);
                }
                break;
            case REQUEST_CROP_PHOTO:  //剪切图片返回
                if (resultCode == RESULT_OK) {
                    final Uri uri = intent.getData();
                    if (uri == null) {
                        return;
                    }
                    String cropImagePath = getRealFilePathFromUri(getApplicationContext(), uri);
                    bitmap = BitmapFactory.decodeFile(cropImagePath);
                    if (type == 1) {
                       // headImage1.setImageBitmap(bitMap);
                    } else {
                        headImage.setImageBitmap(bitmap);
                    }
                    Intent intentBitmap=new Intent();
                    intentBitmap.putExtra("cropImagePath",cropImagePath);
                    setResult(PERSONINFO_CODE,intentBitmap);
                    //此处后面可以将bitMap转为二进制上传后台网络
                    //......

                }
                break;
        }
    }


    /**
     * 打开截图界面
     *
     * @param uri
     */
    public void gotoClipActivity(Uri uri) {
        if (uri == null) {
            return;
        }
        Intent intent = new Intent();
        intent.setClass(this, ClipImageActivity.class);
        intent.putExtra("type", type);
        intent.setData(uri);
        startActivityForResult(intent, REQUEST_CROP_PHOTO);
    }


    /**
     * 根据Uri返回文件绝对路径
     * 兼容了file:///开头的 和 content://开头的情况
     *
     * @param context
     * @param uri
     * @return the file path or null
     */
    public static String getRealFilePathFromUri(final Context context, final Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }


}






