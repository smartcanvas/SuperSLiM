package com.tonicartos.superslimexample;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import com.tonicartos.superslim.LayoutManager;

import xyz.danoz.recyclerviewfastscroller.AbsRecyclerViewFastScroller;
import xyz.danoz.recyclerviewfastscroller.RecyclerViewScroller;
import xyz.danoz.recyclerviewfastscroller.calculation.VerticalScrollBoundsProvider;
import xyz.danoz.recyclerviewfastscroller.calculation.position.VerticalScreenPositionCalculator;
import xyz.danoz.recyclerviewfastscroller.calculation.progress.TouchableScrollProgressCalculator;
import xyz.danoz.recyclerviewfastscroller.calculation.progress.VerticalScrollProgressCalculator;

/**
 * Created by Rafael Baboni Dominiquini on 02/05/16.
 */
public class SLiMFastScroller extends AbsRecyclerViewFastScroller implements RecyclerViewScroller {

    @Nullable
    private SLiMLayoutManagerScrollProgressCalculator mScrollProgressCalculator;

    @Nullable
    private VerticalScreenPositionCalculator mScreenPositionCalculator;

    public SLiMFastScroller(Context context) {
        super(context);
    }

    public SLiMFastScroller(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SLiMFastScroller(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void moveHandleToPosition(float scrollProgress) {
        if (mScreenPositionCalculator != null) {
            mHandle.setY(mScreenPositionCalculator.getYPositionFromScrollProgress(scrollProgress));
        }
    }

    @Override
    protected void onCreateScrollProgressCalculator() {
        VerticalScrollBoundsProvider boundsProvider = new VerticalScrollBoundsProvider(mBar.getY(), mBar.getY() + mBar.getHeight() - mHandle.getHeight());

        mScrollProgressCalculator = new SLiMLayoutManagerScrollProgressCalculator(boundsProvider);
        mScreenPositionCalculator = new VerticalScreenPositionCalculator(boundsProvider);
    }

    @Override
    protected int getLayoutResourceId() {
        return xyz.danoz.recyclerviewfastscroller.R.layout.vertical_recycler_fast_scroller_layout;
    }

    @Override
    protected TouchableScrollProgressCalculator getScrollProgressCalculator() {
        return mScrollProgressCalculator;
    }

    public class SLiMLayoutManagerScrollProgressCalculator extends VerticalScrollProgressCalculator {

        public SLiMLayoutManagerScrollProgressCalculator(VerticalScrollBoundsProvider scrollBoundsProvider) {
            super(scrollBoundsProvider);
        }

        @Override
        public float calculateScrollProgress(RecyclerView recyclerView) {
            LayoutManager layoutManager = (LayoutManager) recyclerView.getLayoutManager();
            int lastFullyVisiblePosition = layoutManager.findLastCompletelyVisibleItemPosition();

            View visibleChild = recyclerView.getChildAt(0);
            if (visibleChild == null) {
                return 0;
            }

            RecyclerView.ViewHolder holder = recyclerView.getChildViewHolder(visibleChild);
            int itemHeight = holder.itemView.getHeight();
            int recyclerHeight = recyclerView.getHeight();
            int itemsInWindow = recyclerHeight / itemHeight;

            int numItemsInList = recyclerView.getAdapter().getItemCount();
            int numScrollableSectionsInList = numItemsInList - itemsInWindow;
            int indexOfLastFullyVisibleItemInFirstSection = numItemsInList - numScrollableSectionsInList - 1;

            int currentSection = Math.max(0, lastFullyVisiblePosition - indexOfLastFullyVisibleItemInFirstSection);

            return (float) currentSection / numScrollableSectionsInList;
        }
    }
}