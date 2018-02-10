package com.xrone.julis.compous.HomeFragment.communication.HeaderAndFooterRecyclerView;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;


/**
 * Created by Julis on 2017/11/21.
 */

public class HeaderAndFooterRecyclerView extends RecyclerView{
    private final LinearLayout headerContainer;
    private final LinearLayout footerContainer;
    private final ProxyAdapter proxyAdapter;

    public HeaderAndFooterRecyclerView(@NonNull Context context) {
        this(context, null);
    }

    public HeaderAndFooterRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HeaderAndFooterRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyle) {
        super(context, attrs, defStyle);
        inspectLayoutManager(getLayoutManager());
        headerContainer = new LinearLayout(context);
        footerContainer = new LinearLayout(context);
        proxyAdapter = new ProxyAdapter(this);
        super.setAdapter(proxyAdapter);
    }

    @NonNull
    public LinearLayout getHeaderContainer() {
        return headerContainer;
    }

    public int getHeaderViewCount() {
        return headerContainer.getChildCount();
    }

    public void addHeaderView(@NonNull View view) {
        headerContainer.addView(view);
        proxyAdapter.notifyHeaderInserted();
    }

    public void addHeaderView(@NonNull View view, int index) {
        headerContainer.addView(view, index);
        proxyAdapter.notifyHeaderInserted();
    }

    public void removeHeaderView(@NonNull View view) {
        headerContainer.removeView(view);
        proxyAdapter.notifyHeaderRemoved();
    }

    public void removeHeaderView(int index) {
        headerContainer.removeViewAt(index);
        proxyAdapter.notifyHeaderRemoved();
    }

    @NonNull
    public LinearLayout getFooterContainer() {
        return footerContainer;
    }

    public int getFooterViewCount() {
        return footerContainer.getChildCount();
    }

    public void addFooterView(@NonNull View view) {
        footerContainer.addView(view);
        proxyAdapter.notifyFooterInserted();
    }

    public void addFooterView(@NonNull View view, int index) {
        footerContainer.addView(view, index);
        proxyAdapter.notifyFooterInserted();
    }

    public void removeFooterView(@NonNull View view) {
        footerContainer.removeView(view);
        proxyAdapter.notifyFooterRemoved();
    }

    public void removeFooterView(int index) {
        footerContainer.removeViewAt(index);
        proxyAdapter.notifyFooterRemoved();
    }

    public void adjustFixedViewContainerLayoutParamsAndOrientation(@NonNull LinearLayout fixedViewContainer) {
        if (getLayoutManager() instanceof GridLayoutManager) {
            GridLayoutManager gridLayoutManager = (GridLayoutManager) getLayoutManager();
            GridLayoutManager.LayoutParams layoutParams;
            int orientation;
            if (fixedViewContainer.getLayoutParams() instanceof GridLayoutManager.LayoutParams) {
                layoutParams = (GridLayoutManager.LayoutParams) fixedViewContainer.getLayoutParams();
                if (gridLayoutManager.getOrientation() == LinearLayoutManager.VERTICAL) {
                    layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                    layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    orientation = LinearLayout.VERTICAL;
                } else {
                    layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
                    layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
                    orientation = LinearLayout.HORIZONTAL;
                }
            } else {
                if (gridLayoutManager.getOrientation() == LinearLayoutManager.VERTICAL) {
                    layoutParams = new GridLayoutManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    orientation = LinearLayout.VERTICAL;
                } else {
                    layoutParams = new GridLayoutManager.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    orientation = LinearLayout.HORIZONTAL;
                }
            }
            fixedViewContainer.setLayoutParams(layoutParams);
            fixedViewContainer.setOrientation(orientation);
        } else if (getLayoutManager() instanceof LinearLayoutManager) {
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) getLayoutManager();
            RecyclerView.LayoutParams layoutParams;
            int orientation;
            if (fixedViewContainer.getLayoutParams() instanceof RecyclerView.LayoutParams) {
                layoutParams = (RecyclerView.LayoutParams) fixedViewContainer.getLayoutParams();
                if (linearLayoutManager.getOrientation() == LinearLayoutManager.VERTICAL) {
                    layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                    layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    orientation = LinearLayout.VERTICAL;
                } else {
                    layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
                    layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
                    orientation = LinearLayout.HORIZONTAL;
                }
            } else {
                if (linearLayoutManager.getOrientation() == LinearLayoutManager.VERTICAL) {
                    layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    orientation = LinearLayout.VERTICAL;
                } else {
                    layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    orientation = LinearLayout.HORIZONTAL;
                }
            }
            fixedViewContainer.setLayoutParams(layoutParams);
            fixedViewContainer.setOrientation(orientation);
        } else if (getLayoutManager() instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) getLayoutManager();
            StaggeredGridLayoutManager.LayoutParams layoutParams;
            int orientation;
            if (fixedViewContainer.getLayoutParams() instanceof StaggeredGridLayoutManager.LayoutParams) {
                layoutParams = (StaggeredGridLayoutManager.LayoutParams) fixedViewContainer.getLayoutParams();
                if (staggeredGridLayoutManager.getOrientation() == StaggeredGridLayoutManager.VERTICAL) {
                    layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                    layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    orientation = LinearLayout.VERTICAL;
                } else {
                    layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
                    layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
                    orientation = LinearLayout.HORIZONTAL;
                }
            } else {
                if (staggeredGridLayoutManager.getOrientation() == StaggeredGridLayoutManager.VERTICAL) {
                    layoutParams = new StaggeredGridLayoutManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    orientation = LinearLayout.VERTICAL;
                } else {
                    layoutParams = new StaggeredGridLayoutManager.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    orientation = LinearLayout.HORIZONTAL;
                }
            }
            layoutParams.setFullSpan(true);
            fixedViewContainer.setLayoutParams(layoutParams);
            fixedViewContainer.setOrientation(orientation);
        }
    }

    private void inspectLayoutManager(RecyclerView.LayoutManager layoutManager) {
        if (layoutManager instanceof GridLayoutManager) {
            GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            GridLayoutManager.SpanSizeLookup spanSizeLookup = gridLayoutManager.getSpanSizeLookup();
            FixedViewSpanSizeLookup fixedViewSpanSizeLookup = null;
            if (spanSizeLookup == null || spanSizeLookup instanceof GridLayoutManager.DefaultSpanSizeLookup) {
                fixedViewSpanSizeLookup = new FixedViewSpanSizeLookup();
                gridLayoutManager.setSpanSizeLookup(fixedViewSpanSizeLookup);
            } else if (spanSizeLookup instanceof FixedViewSpanSizeLookup) {
                fixedViewSpanSizeLookup = (FixedViewSpanSizeLookup) spanSizeLookup;
            }
            if (fixedViewSpanSizeLookup != null) {
                fixedViewSpanSizeLookup.attach(gridLayoutManager, proxyAdapter);
            }
        }
    }

    private void recoverLayoutManager(RecyclerView.LayoutManager layoutManager) {
        if (layoutManager instanceof GridLayoutManager) {
            GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            GridLayoutManager.SpanSizeLookup spanSizeLookup = gridLayoutManager.getSpanSizeLookup();
            if (spanSizeLookup instanceof FixedViewSpanSizeLookup) {
                gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.DefaultSpanSizeLookup());
                ((FixedViewSpanSizeLookup) spanSizeLookup).detach();
            }
        }
    }

    @Override
    public void setLayoutManager(RecyclerView.LayoutManager layoutManager) {
        RecyclerView.LayoutManager oldLayoutManager = getLayoutManager();
        inspectLayoutManager(layoutManager);
        super.setLayoutManager(layoutManager);
        recoverLayoutManager(oldLayoutManager);
    }

    @NonNull
    public ProxyAdapter getProxyAdapter() {
        return proxyAdapter;
    }

    @Override
    public RecyclerView.Adapter getAdapter() {
        return proxyAdapter.getAdapter();
    }

    @Override
    public void setAdapter(RecyclerView.Adapter adapter) {
        super.setAdapter(null);
        if (adapter != null) {
            proxyAdapter.setHasStableIds(adapter.hasStableIds());
        } else {
            proxyAdapter.setHasStableIds(false);
        }
        proxyAdapter.setAdapter(adapter);
        super.setAdapter(proxyAdapter);
    }

    @Override
    public void swapAdapter(RecyclerView.Adapter adapter, boolean removeAndRecycleExistingViews) {
        super.swapAdapter(null, removeAndRecycleExistingViews);
        if (adapter != null) {
            proxyAdapter.setHasStableIds(adapter.hasStableIds());
        } else {
            proxyAdapter.setHasStableIds(false);
        }
        proxyAdapter.setAdapter(adapter);
        super.swapAdapter(proxyAdapter, removeAndRecycleExistingViews);
    }

}
