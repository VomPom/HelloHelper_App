package com.xrone.julis.compous;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Created by Julis on 2017/10/28.
 */

public class WelComeActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);
        welcome();
    }
    public void welcome(){
        new Thread(){
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                    Intent intent=new Intent(WelComeActivity.this,MainActivity.class);
                    startActivity(intent);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

}
