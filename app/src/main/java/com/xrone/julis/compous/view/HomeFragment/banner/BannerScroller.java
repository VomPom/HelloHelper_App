package com.xrone.julis.compous.view.HomeFragment.banner;

import android.content.Context;
import android.widget.Scroller;

public class BannerScroller extends Scroller {
    private int mDuration = 800;

    public BannerScroller(Context context) {
        super(context);
    }



    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        super.startScroll(startX, startY, dx, dy, mDuration);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy) {
        super.startScroll(startX, startY, dx, dy, mDuration);
    }

    public void setDuration(int time) {
        mDuration = time;
    }

}
