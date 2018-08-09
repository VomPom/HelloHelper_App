package com.xrone.julis.compous.HomeFragment;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.xrone.julis.compous.MainActivity;
import com.xrone.julis.compous.R;
import com.xrone.julis.compous.Utils.Browser;


/**
 * Created by Julis on 17/6/10.
 */

public class ShopFragment extends Fragment{
    private WebView webView;
    private ProgressDialog progressDialog;
    public void showProgress(String message,View view) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(view.getContext(), ProgressDialog.STYLE_SPINNER);
            progressDialog.setCancelable(false);//设置点击不消失
        }
        if (progressDialog.isShowing()) {
            progressDialog.setMessage(message);
        } else {
            progressDialog.setMessage(message);
            progressDialog.show();
        }
    }
    public void removeProgress(){
        if (progressDialog==null){
            return;
        }
        if (progressDialog.isShowing()){
            progressDialog.dismiss();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.application_browerinformation,container,false);
        initViews(view);
        return view;
    }

    public void initViews(View view){
//        Intent intent = new Intent(view.getContext(), Browser.class);
//        intent.putExtra("url","http://www.xrone.cn/mobile/category/index");
//        startActivity(intent);
        webView=(WebView)view.findViewById(R.id.application_browerInformation);
        webView.loadUrl("http://www.xrone.cn/mobile/category/index");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
            }
        });
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
//                view.loadUrl(NetURL.EXPRESS_URL);
                return super.shouldOverrideUrlLoading(view, request);
            }
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                showProgress("Loading...",view);//开始加载动画
            }
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                removeProgress();//当加载结束时移除动画
            }
        });
    }


}






