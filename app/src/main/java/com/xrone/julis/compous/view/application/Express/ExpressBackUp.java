package com.xrone.julis.compous.view.application.Express;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.xrone.julis.compous.R;
import com.xrone.julis.compous.model.StringURL;

/**
 * Created by Julis on 17/6/12.
 */

public class ExpressBackUp extends Activity{
    private WebView webView;
    private ProgressDialog progressDialog;

    public void showProgress(String message) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(ExpressBackUp.this, ProgressDialog.STYLE_SPINNER);
            progressDialog.setCancelable(false);//设置点击不消失
        }
        if (progressDialog.isShowing()) {
            progressDialog.setMessage(message);
        } else {
            progressDialog.setMessage(message);
            progressDialog.show();
        }
    }

    /**
     *去除加载动画
     */
    public void removeProgress(){
        if (progressDialog==null){
            return;
        }
        if (progressDialog.isShowing()){
            progressDialog.dismiss();
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.ECLAIR_MR1)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_webview_express);
        webView=(WebView)findViewById(R.id.app_webview);

        webView.getSettings().setJavaScriptEnabled(true);
        /**
         *  WebView自适应全屏
         */
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);


        webView.loadUrl(StringURL.EXPRESS_URL);


        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
            }
        });

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(StringURL.EXPRESS_URL);
                return super.shouldOverrideUrlLoading(view, request);

            }
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                showProgress("Loading...");//开始加载动画
            }
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                removeProgress();//当加载结束时移除动画
            }
        });
    }
}






