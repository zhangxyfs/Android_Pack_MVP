package com.z7dream.apm.base.widget.recycler;

import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Z7Dream on 2017/5/6 17:39.
 * Email:zhangxyfs@126.com
 */

public abstract class OnRecyclerItemClickListener implements RecyclerView.OnItemTouchListener {
    private final RecyclerView recyclerView;
    private final GestureDetectorCompat mGestureDetector;

    public OnRecyclerItemClickListener(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        mGestureDetector = new GestureDetectorCompat(recyclerView.getContext(), new ItemTouchHelperGestureListener());
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        mGestureDetector.onTouchEvent(e);
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        mGestureDetector.onTouchEvent(e);
        return false;
    }

    public abstract void onItemClick(RecyclerView.ViewHolder viewHolder, float x, float y);

    public abstract void onItemLongClick(RecyclerView.ViewHolder viewHolder, float x, float y);

    private class ItemTouchHelperGestureListener extends GestureDetector.SimpleOnGestureListener {
        public boolean onSingleTapUp(MotionEvent event) {
            View child = recyclerView.findChildViewUnder(event.getX(), event.getY());
            if (child != null) {
                RecyclerView.ViewHolder viewHolder = recyclerView.getChildViewHolder(child);
                onItemClick(viewHolder, event.getX(), event.getY());
            }
            return true;
        }

        public void onLongPress(MotionEvent event) {
            View child = recyclerView.findChildViewUnder(event.getX(), event.getY());
            if (child != null) {
                RecyclerView.ViewHolder viewHolder = recyclerView.getChildViewHolder(child);
                onItemLongClick(viewHolder, event.getX(), event.getY());
            }
        }
    }
}
