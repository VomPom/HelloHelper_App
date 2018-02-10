package com.xrone.julis.compous.Utils;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.xrone.julis.compous.R;
import com.xrone.julis.compous.StringData.NetURL;
import com.xrone.julis.compous.Utils.update.CommonDialog;
import com.xrone.julis.compous.Utils.update.UpdateService;
import com.xrone.julis.compous.model.Hello;
import com.xrone.julis.compous.model.UpdateBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Julis on 2017/11/2.
 */

public class CheckVersion {
    private UpdateBean updateBean=new UpdateBean();
    private static boolean updateFlag=false;//是否弹出更新界面标志
    private Context context;
    public CheckVersion(Context context) {
        this.context = context;
    }

    public  boolean isUpdateFlag() {
        return updateFlag;
    }


    /**
     * 获取服务器更新数据
     */
    public   void checkVersion() throws Exception {
        Handler updateHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                String jsonData = (String) msg.obj;
                try {
                    //获取Json数组里面的值，并加入到Information对象里面去
                    JSONArray jsonArray = new JSONArray(jsonData);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        updateBean.setVersionCode(Integer.parseInt(object.getString("versonCode")));
                        updateBean.setContent(object.getString("content"));
                        updateBean.setUpdateUrl(object.getString("updateUrl"));
                        updateBean.setVersionName(object.getString("versionName"));
                    }
                    compareVersion();//检查版本
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            }
        };

        /**
         * 请求数据库更新请求
         */
        HttpUtils.getNewsJSON(NetURL.CHECKUPDATE_URL,updateHandler);
    }

    /**
     * 对比版本数据
     * @throws PackageManager.NameNotFoundException
     */
    public void compareVersion() throws PackageManager.NameNotFoundException {
        //获取当前程序的版本号
        //获取packagemanager的实例
        PackageManager packageManager = context.getPackageManager();
        //getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
        int versionCode = packInfo.versionCode;//获取当前版本号
        Hello.versionName=packInfo.versionName;
        Hello.versionCode=packInfo.versionCode;
        System.out.println("版本号为："+versionCode);
        System.out.println("服务器版本号为："+updateBean.getVersionCode());

        if(versionCode<updateBean.getVersionCode()) {//版本号对比
            updateMessage();
        }
    }
    /**
     * 弹出升级提示，并执行更行
     */
    public void updateMessage(){

        updateFlag=true;
        CommonDialog.Builder builder = new CommonDialog.Builder(context);
        builder.setTitle(R.string.update_prompt);
        builder.setMessage(updateBean.getContent());
        builder.setPositiveButton(R.string.update_immediately, (dialog, which) -> {
            dialog.dismiss();
            Intent intent = new Intent(context, UpdateService.class);
            intent.putExtra("apkUrl", updateBean.getUpdateUrl());
            Log.e("downloadURL",updateBean.getUpdateUrl());
            context.startService(intent);

        });
        builder.setNegativeButton(R.string.update_nexttime, (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }
}
