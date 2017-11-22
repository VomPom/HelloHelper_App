package com.xrone.julis.compous.view.HomeFragment.communication.HeaderAndFooterRecyclerView;

import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;

/**
 * Created by Julis on 2017/11/21.
 */
final class FixedViewSpanSizeLookup extends GridLayoutManager.SpanSizeLookup {

    private GridLayoutManager gridLayoutManager;
    private ProxyAdapter proxyAdapter;

    void attach(@NonNull GridLayoutManager gridLayoutManager, @NonNull ProxyAdapter proxyAdapter) {
        if (this.gridLayoutManager != null || this.proxyAdapter != null) {
            throw new IllegalStateException("FixedViewSpanSizeLookup can not be shared.");
        }
        this.gridLayoutManager = gridLayoutManager;
        this.proxyAdapter = proxyAdapter;
    }

    void detach() {
        gridLayoutManager = null;
        proxyAdapter = null;
    }

    @Override
    public int getSpanSize(int position) {
        if (gridLayoutManager == null || proxyAdapter == null) {
            throw new IllegalStateException("FixedViewSpanSizeLookup has not been attached yet.");
        }
        int viewType = proxyAdapter.getItemViewType(position);
        if (viewType == FixedViewHolder.VIEW_TYPE_HEADER || viewType == FixedViewHolder.VIEW_TYPE_FOOTER) {
            return gridLayoutManager.getSpanCount();
        } else {
            return 1;
        }
    }

}
