package com.xrone.julis.compous.Utils;

/**
 * Created by Julis on 17/6/13.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;

import com.xrone.julis.compous.R;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.OutputStream;
import java.io.InputStream;
import java.util.Map;
import java.io.IOException;
import java.net.URLEncoder;
import java.io.ByteArrayOutputStream;

public class HttpUtils {

    public static void getNewsJSON(final String url, final Handler handler){
        new Thread(() -> {
            HttpURLConnection conn;
            InputStream is;
            try {
                conn = (HttpURLConnection) new URL(url).openConnection();
                conn.setRequestMethod("GET");
                is=conn.getInputStream();
                BufferedReader reader=new BufferedReader(new InputStreamReader(is));
                String line= "";
                StringBuilder result=new StringBuilder();
                while ((line=reader.readLine())!=null){
                    result.append(line);
                }
                Message msg=new Message();
                msg.obj = result.toString();

                handler.sendMessage(msg);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
}