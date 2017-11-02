package com.xrone.julis.compous.view.LoginAndRegister;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.xrone.julis.compous.R;
import com.xrone.julis.compous.model.StringURL;
import com.xrone.julis.compous.Utils.Utils;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Julis on 17/6/12.
 */

public class RegisterActivity extends Activity implements View.OnClickListener {
    private EditText et_nickname;
    private EditText et_password;
    private Button btn_register;
    private EditText et_repeat_password;
    private String username;
    private String password;
    private String sex;
    private RadioButton boyRa;
    private RadioButton girlRa;
    private RadioGroup sex_group;

    Map<String,String> params = new HashMap<String,String>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        et_nickname=(EditText)findViewById(R.id.et_nickname);
        et_password=(EditText)findViewById(R.id.et_password);
        et_repeat_password=(EditText)findViewById(R.id.et_repeat_password);
        btn_register=(Button)findViewById(R.id.btn_register);
        boyRa=(RadioButton)findViewById(R.id.radio_sex_boy);
        girlRa=(RadioButton)findViewById(R.id.radio_sex_girl);
        sex_group = (RadioGroup)findViewById(R.id.radio_boy_girl);

        btn_register.setOnClickListener(this);


    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_register:
                getRegister();
                break;
            default:
                break;
        }
    }

    private void getRegister() {
        username = et_nickname.getText().toString();
        password = et_password.getText().toString();
        int id = sex_group.getCheckedRadioButtonId();
        if (id == boyRa.getId()) {
            sex = "男";
        } else {
            sex = "女";
        }

        if (username.equals("")) {
            Toast.makeText(this, "用户名不能为空！\n" +
                    "User name cannot be empty!", Toast.LENGTH_SHORT).show();
        } else if (password.equals("")) {
            Toast.makeText(this, "密码不能为空！\n" +
                    "Password cannot be empty!", Toast.LENGTH_SHORT).show();
        } else if (et_repeat_password.getText().toString().equals("")) {
            Toast.makeText(this, "请再次输入密码！\n" +
                    "Please enter your password again!", Toast.LENGTH_SHORT).show();
        } else if (et_repeat_password.getText().toString().equals(password) == false) {
            Toast.makeText(this, "两次密码不一致！\n" +
                    "The two password is inconsistent!", Toast.LENGTH_SHORT).show();
        } else if (et_repeat_password.getText().toString().equals(password) == true) {


            AsyncHttpClient client = new AsyncHttpClient();

            RequestParams params = new RequestParams();
            params.add("username", username);
            params.add("password", password);
            params.add("sex", sex);
            client.post(StringURL.REG_URL, params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int i, Header[] headers, byte[] bytes) {
                    String response = new String(bytes);
                    JSONObject object = null;
                    try {
                        object = new JSONObject(response);
                        String status = object.getString("status");
                        if (status.equals("exists")) {
                            Toast.makeText(RegisterActivity.this, "用户名已存在，请更换!\n" +
                                    "The user name already exists. Please replace it!", Toast.LENGTH_LONG).show();
                        } else if (status.equals("error")) {
                            Toast.makeText(RegisterActivity.this, "出现错误，请稍后重试!\n" +
                                    "An error occurred. Please try again later!", Toast.LENGTH_LONG).show();

                        } else if (status.equals("success")) {
                            String token = object.getString("token");
                            Utils.saveUserInfo(RegisterActivity.this,null,username,password,sex,null);

                            Toast.makeText(RegisterActivity.this, "注册成功\n" +
                                    "Login successful！", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(intent);
                            RegisterActivity.this.finish();

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                    Toast.makeText(RegisterActivity.this, "网络错误，请稍后重试！\n" +
                            "Network error. Please try again later!", Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}















