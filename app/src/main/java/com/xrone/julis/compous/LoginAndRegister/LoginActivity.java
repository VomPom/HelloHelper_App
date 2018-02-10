package com.xrone.julis.compous.LoginAndRegister;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.xrone.julis.compous.StringData.NetURL;
import com.xrone.julis.compous.Utils.MyAlert;
import com.xrone.julis.compous.model.Hello;
import com.xrone.julis.compous.R;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;


/**
 * Created by Julis on 17/6/13.
 */

public class LoginActivity extends Activity implements View.OnClickListener {
    private Button btn_qtlogin;
    private Button btn_login;
    private EditText et_login_nickname;
    private EditText et_login_password;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();
    }

    public void initViews(){
        btn_qtlogin=(Button)findViewById(R.id.btn_qtlogin);
        btn_login=(Button)findViewById(R.id.btn_login);

        et_login_nickname=(EditText)findViewById(R.id.et_login_nickname);
        et_login_password=(EditText)findViewById(R.id.et_login_password);

        btn_qtlogin.setOnClickListener(this);
        btn_login.setOnClickListener(this);
        Map<String,String> userInfo= Hello.getUserInfo(this);
        if(userInfo!=null){
            et_login_nickname.setText(userInfo.get("name"));
            et_login_password.setText(userInfo.get("password"));
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                Login();
                break;
            case R.id.btn_qtlogin:
                Intent intent = new Intent(getBaseContext(),RegisterActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
    public static boolean checkLogin(@NonNull final Activity activity) {

        if (Hello.isLogin){
            return true;
        }else{

            MyAlert myAlert=new MyAlert(activity, "Tip", activity.getString(R.string.need_login_tip)){
                @Override
                public void onClickNo() {
                }
                @Override
                public void onClickYes() {
                    activity.startActivityForResult(new Intent(activity, LoginActivity.class),0);
                }
            };
            myAlert.showMyAlert();
            return false;
        }

    }
    /**
     * 登陆模块
     */
    private void Login() {

        final String username=et_login_nickname.getText().toString();
        final String password=et_login_password.getText().toString();
        if(username.equals("")){
            Toast.makeText(this,"用户名不能为空！\n" +
                    "User name cannot be empty!",Toast.LENGTH_SHORT).show();
        }else if(password.equals("")){
            Toast.makeText(this,"密码不能为空！\n" +
                    "Password cannot be empty!",Toast.LENGTH_SHORT).show();
        }else{
            /**
             *
             */
            AsyncHttpClient client = new AsyncHttpClient();
            RequestParams params = new RequestParams();
            params.add("username", username);
            params.add("password", password);
            client.post(NetURL.LOGIN_URL, params, new AsyncHttpResponseHandler() {

                @Override
                public void onSuccess(int i, Header[] headers, byte[] bytes) {
                    String response = new String(bytes);
                    Log.e("debug", response);
                    JSONObject object = null;
                    try {
                        object = new JSONObject(response);
                        String status = object.getString("status");
                        if (status.equals("success")) {
                            String token = object.getString("token");
                            String sex = object.getString("sex");
                            String heade_url = object.getString("head_url");
                            String id = object.getString("id");
                            Hello.isLogin = true;
                            Hello.username = username;
                            Hello.Token = token;
                            Hello.sex=sex;
                            Hello.head_url= NetURL.WEBSITE+ NetURL.DIRECTIONARY+ NetURL.USER_HEAD_DIR+heade_url;
                            Log.e("head_url",heade_url);
                            Hello.id=id;
                            Hello.saveUserInfo(LoginActivity.this,Hello.id,username,password,Hello.sex,Hello.head_url,"true");
                            Log.e("LoginActionv", String.valueOf(Hello.isLogin));
                            Toast.makeText(LoginActivity.this,"登录成功！\n" +
                                    "Login successful!",Toast.LENGTH_SHORT).show();
//
                            LoginActivity.this.finish();
                        }
                        else {
                            Toast.makeText(LoginActivity.this, "用户名或密码错误，请重试\n" +
                                    "User name or password error, please try again", Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                    Toast.makeText(LoginActivity.this, "网络错误，请稍后重试!\n" +
                            "Network error. Please try again later!", Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}









