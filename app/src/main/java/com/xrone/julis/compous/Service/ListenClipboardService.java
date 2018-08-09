package com.xrone.julis.compous.Service;

import android.app.Service;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.xrone.julis.compous.Utils.TransLaterUtilts;
import com.xrone.julis.compous.Utils.TransLatorCallback;
import com.xrone.julis.compous.Application.translate.TranslateResultModel;
import com.xrone.julis.compous.Application.translate.view.TipViewController;


public final class ListenClipboardService extends Service{
    private static TipViewController mTipViewController;
    private static final String TAG = "ListenClipboardService";


    public static void start(final Context context) {
        Intent serviceIntent = new Intent(context, ListenClipboardService.class);
        context.startService(serviceIntent);

        final ClipboardManager cb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        cb.addPrimaryClipChangedListener(new ClipboardManager.OnPrimaryClipChangedListener() {
            @Override
            public void onPrimaryClipChanged() {
                ClipData data=cb.getPrimaryClip();
                ClipData.Item item = data.getItemAt(0);
                final String text = item.getText().toString().replace("\n","");
                // 具体实现
                Log.e("a","内容改变:"+text);


                mTipViewController = new TipViewController(context);
                mTipViewController.setViewDismissHandler(new TipViewController.ViewDismissHandler() {
                    @Override
                    public void onViewDismiss() {
                        System.out.println("消失了");
                        mTipViewController = null;
                    }
                });

                TransLaterUtilts.getData(context,text,new TransLatorCallback() {
                    @Override
                    public void setPropety(TranslateResultModel resultModel) {
                        mTipViewController.setRestult(resultModel.getDst());
                        String afterString=text;
                        if(afterString.length()>200){
                            byte[]bytes=text.getBytes();
                            afterString=new String(bytes,0,200)+"...";
                        }
                        mTipViewController.setContent(afterString);
                        mTipViewController.show();
                    }
                });

            }
        });


    }



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }


}