package com.tonicartos.superslimexample;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import com.tonicartos.superslim.GridSLM;
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

            View visibleChild = getViewSample(recyclerView);

            if (visibleChild != null) {
                LayoutManager layoutManager = (LayoutManager) recyclerView.getLayoutManager();
                int lastFullyVisiblePosition = layoutManager.findLastCompletelyVisibleItemPosition();

                int spanCount = getSpanCount(recyclerView, visibleChild);

                RecyclerView.ViewHolder holder = recyclerView.getChildViewHolder(visibleChild);

                int itemHeight = holder.itemView.getHeight();
                int recyclerHeight = recyclerView.getHeight();
                int itemsInWindow = (recyclerHeight / itemHeight) * spanCount;

                int numItemsInList = recyclerView.getAdapter().getItemCount();
                int numScrollableSectionsInList = numItemsInList - itemsInWindow;
                int indexOfLastFullyVisibleItemInFirstSection = numItemsInList - numScrollableSectionsInList - 1;

                int currentSection = Math.max(0, lastFullyVisiblePosition - indexOfLastFullyVisibleItemInFirstSection);

                float currentPosition = (float) currentSection / numScrollableSectionsInList;

                return currentPosition;
            }

            return 0f;
        }

        private View getViewSample(RecyclerView recyclerView) {

            return recyclerView.getChildAt(0);
        }

        private int getSpanCount(RecyclerView recyclerView, View viewSample) {

            if (viewSample == null) {
                viewSample = getViewSample(recyclerView);
            }

            GridSLM.LayoutParams layoutParams = GridSLM.LayoutParams.from(viewSample.getLayoutParams());

            if (layoutParams.getNumColumns() != -1) {
                return layoutParams.getNumColumns();
            } else {
                return layoutParams.getColumnWidth() == RecyclerView.LayoutParams.MATCH_PARENT ? 1 : recyclerView.getWidth() / layoutParams.getColumnWidth();
            }
        }
    }
}