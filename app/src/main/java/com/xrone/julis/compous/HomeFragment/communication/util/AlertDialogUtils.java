package com.xrone.julis.compous.HomeFragment.communication.util;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;

import com.xrone.julis.compous.R;


public final class AlertDialogUtils {

    private AlertDialogUtils() {}

    public static AlertDialog.Builder createBuilderWithAutoTheme(@NonNull Activity activity) {
        return new AlertDialog.Builder(activity, R.style.AppDialogLight_Alert);
    }

}
