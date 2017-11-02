package com.xrone.julis.compous.Utils.update;

/**
 * Created by Julis on 2017/10/11.
 */

public interface UpdateDownloadListener {
    public void onStarted();
    public void onProgressChanged(int progress,String downloadUrl);
    public void onFinished(float completeSize,String downloadUrl);
    public void onFailure();

}
