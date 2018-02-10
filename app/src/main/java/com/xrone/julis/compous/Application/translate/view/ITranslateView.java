package com.xrone.julis.compous.Application.translate.view;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.support.design.widget.Snackbar;

import com.xrone.julis.compous.Application.translate.TranslateActivity;
import com.xrone.julis.compous.Utils.MyAlert;
import com.xrone.julis.compous.Utils.NetworkStatus;

/**
 * Created by Julis on 2017/12/23.
 */

public interface ITranslateView {
     void translate();
     void btn_share();
     void btn_voice();
     void btn_copy();
}
