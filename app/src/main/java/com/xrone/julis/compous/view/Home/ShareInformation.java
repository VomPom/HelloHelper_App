package com.xrone.julis.compous.view.Home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.xrone.julis.compous.model.Hello;
import com.xrone.julis.compous.MainActivity;
import com.xrone.julis.compous.R;
import com.xrone.julis.compous.model.StringURL;

import org.json.JSONObject;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Created by Julis on 17/6/14.
 */

public class ShareInformation extends Activity implements View.OnClickListener {
    private EditText infomation;
    private Button submit;
    private Context context;
    private boolean flag = false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_share);
        infomation = (EditText) findViewById(R.id.et_share_infomation);
        submit = (Button) findViewById(R.id.share_submit);
        submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

       final String information = infomation.getText().toString();
        if(information.equals("")){
            Toast.makeText(this,"内容不能为空！\n" +
                    "Content cannot be empty!",Toast.LENGTH_SHORT).show();
        }else{

        new Thread(new Runnable() {
            @Override
            public void run() {
                // 写子线程中的操作
                try {

                    JSONObject obj = new JSONObject();
                    obj.put("info", information);
                    obj.put("name", Hello.username);
                    // 创建url资源
                    URL url = new URL(StringURL.SHARE_URL);
                    // 建立http连接
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    // 设置允许输出
                    conn.setDoOutput(true);

                    conn.setDoInput(true);

                    // 设置不用缓存
                    conn.setUseCaches(false);
                    // 设置传递方式
                    conn.setRequestMethod("POST");
                    // 设置维持长连接
                    conn.setRequestProperty("Connection", "Keep-Alive");
                    // 设置文件字符集:
                    conn.setRequestProperty("Charset", "UTF-8");
                    //转换为字节数组
                    byte[] data = (obj.toString()).getBytes();
                    // 设置文件长度
                    conn.setRequestProperty("Content-Length", String.valueOf(data.length));

                    // 设置文件类型:
                    conn.setRequestProperty("contentType", "application/json");
                    // 开始连接请求
                    OutputStream out = conn.getOutputStream();
                    // 写入请求的字符串
                    out.write((obj.toString()).getBytes());
                    out.flush();
                    conn.connect();
                    out.close();

                    System.out.println("conn"+conn.getResponseCode());
                    // 请求返回的状态
                    if (conn.getResponseCode() == 200) {
                        System.out.println("连接成功");
                        // 请求返回的数据
                        InputStream in = conn.getInputStream();
                        String a = null;
                        try {
                            byte[] data1 = new byte[in.available()];
                            in.read(data1);
                            // 转成字符串
                            a = new String(data1);
                            System.out.println("php传回来的数据:"+a+":end");
                            Looper.prepare();
                            Toast.makeText(ShareInformation.this,"发布成功！\n" +
                                    "Publish successfully!",Toast.LENGTH_SHORT).show();
                            Looper.loop();


                        } catch (Exception e1) {
                            // TODO Auto-generated catch block
                            e1.printStackTrace();
                        }
                    } else {

                        Looper.prepare();
                        Toast.makeText(ShareInformation.this,"请求超时！\n" +
                                "Request timeout!",Toast.LENGTH_SHORT).show();
                        Looper.prepare();
                    }

                } catch (Exception e) {
                    e.printStackTrace();

                }

            }
        }).start();

             Intent i = new Intent(ShareInformation.this,MainActivity.class);
             startActivity(i);
             finish();
        }
    }
}







