package com.xrone.julis.compous.Service;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

import com.xrone.julis.compous.Utils.StringOfID;
import com.xrone.julis.compous.view.application.translate.TipViewController;
import com.xrone.julis.compous.Utils.MD5;
import com.xrone.julis.compous.view.application.translate.util.ReplaceABC;
import com.xrone.julis.compous.view.application.translate.util.UrlString;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Julis on 2017/10/22.
 */

public class SmsObserver extends ContentObserver  {
    private static TipViewController mTipViewController;
    private Context context;
    /**
     * Creates a content observer.
     *
     * @param handler The handler to run {@link #onChange} on, or null if none.
     */
    public SmsObserver(Context context,Handler handler) {
        super(handler);
        this.context=context;
    }

    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);

        Uri uri=Uri.parse("content://sms/");
        ContentResolver resolver=context.getContentResolver();
        Cursor cursor=resolver.query(uri,new String[]{"address","date","type","body"},null,null,null);
        cursor.moveToFirst();

        String address=cursor.getString(0);
        String body=cursor.getString(3);
        String date=cursor.getString(1);
        int type=Integer.parseInt(cursor.getString(2));
//        Log.e("address",address);
//        Log.e("body",body);
//        Log.e("date",date);
        Log.e("type", String.valueOf(type));
        Log.w("bodyT",body);
        boolean flag=fileTer(body);
        Log.w("flag", String.valueOf(flag));

        if(flag==true&&type==1){
            /*Log.w("intent","11111");
            Intent intent=new Intent(context.getApplicationContext(), BDTranslator.class);
            Log.w("intent","22222");
            intent.putExtra("text","sms");
            intent.putExtra("mes",body);
            Log.w("intent","3333");
            context.startActivity(intent);
            Log.w("intent","44444");*/
         //   mTipViewController = new TipViewController(context);
          //  getTranslaterString(body);
        }else{
           Log.w("no","没有执行");
           Log.w("fileTer(body)", String.valueOf(fileTer(body)));
        }

    }
    public boolean fileTer(String smsString){

        Log.e("text",smsString);
        String regex = "[\u4e00-\u9fa5]";
        Matcher m = Pattern.compile(regex).matcher(smsString);
        Log.e("find", String.valueOf(m.find()));
        if(m.find()){
              return true;
        }
        return false;

    }


    public static void getTranslaterString(String mWord) {
        String mUrl=null;
        String s1;
        String s2;
        String s3;
        String MD5string;
        String salt = String.valueOf(System.currentTimeMillis());
        s3 = mWord;
        try {
            MD5 getMD5 = new MD5();
            s2 = URLEncoder.encode(mWord, "utf-8");
            s1 = ReplaceABC.mReplace(s3, "+", " ");
            MD5string = getMD5.GetMD5Code(StringOfID.BAIDU_APP_ID + s1 + salt +StringOfID.BAIDU_KEY);
            mUrl = UrlString.mUrlNOChangeForward + s2
                    + UrlString.mUrlNOChangeBehind + StringOfID.BAIDU_APP_ID + "&salt=" + salt + "&sign=" + MD5string;
            //System.out.println(mUrl);
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        fanYi(mUrl);
    }
    private  static void fanYi(final String str) {

        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... params) {
                try {
                    URL url = new URL(params[0]);
                    HttpURLConnection connection = (HttpURLConnection) url
                            .openConnection();
                    InputStream is = connection.getInputStream();
                    InputStreamReader isr = new InputStreamReader(is, "utf-8");
                    BufferedReader bf = new BufferedReader(isr);
                    String line;
                    StringBuffer sb = new StringBuffer();
                    while ((line = bf.readLine()) != null) {
                        //System.out.println(line);
                        sb.append(line);
                    }
                    bf.close();
                    isr.close();
                    is.close();
                    JSONObject jsonObject = new JSONObject(sb.toString());
                    JSONArray trans_result = jsonObject
                            .getJSONArray("trans_result");
                    StringBuffer afterText = new StringBuffer();
                    for (int i = 0; i < trans_result.length(); i++) {
                        JSONObject jo = trans_result.optJSONObject(i);
                        afterText.append(jo.getString("dst"));
                    }
                    System.out.println("翻译结果："+afterText.toString());
                    mTipViewController.setRestult(afterText.toString());

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return params[0];
            }

            @Override
            protected void onPostExecute(String result) {
                // TODO Auto-generated method stub
                System.out.println("onPostExecute："+result);
                mTipViewController.show();
//				super.onPostExecute(result);
            }
        }.execute(str);
    }
}
