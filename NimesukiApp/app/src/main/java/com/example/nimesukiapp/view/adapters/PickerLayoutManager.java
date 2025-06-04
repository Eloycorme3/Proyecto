package com.example.nimesukiapp.view.adapters;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

public class PickerLayoutManager extends LinearLayoutManager {

    private RecyclerView recyclerView;
    private OnItemSelectedListener callback;

    public PickerLayoutManager(Context context) {
        super(context, HORIZONTAL, false);
    }

    public void setOnItemSelectedListener(OnItemSelectedListener callback) {
        this.callback = callback;
    }

    @Override
    public void onAttachedToWindow(@NonNull RecyclerView view) {
        super.onAttachedToWindow(view);
        recyclerView = view;
        new LinearSnapHelper().attachToRecyclerView(recyclerView);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        super.onLayoutChildren(recycler, state);
        scaleDownChildren();
    }

    @Override
    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
        int scrolled = super.scrollHorizontallyBy(dx, recycler, state);
        scaleDownChildren();
        return scrolled;
    }

    private void scaleDownChildren() {
        int mid = getWidth() / 2;

        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (child == null) continue;

            int childMid = (getDecoratedLeft(child) + getDecoratedRight(child)) / 2;
            float distance = Math.abs(mid - childMid);

            float d = Math.min(distance / (float) mid, 1f);
            float scale = 1f - d * 0.65f;
            scale = Math.max(0.4f, scale);

            child.setScaleX(scale);
            child.setScaleY(scale);
        }
    }

    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);

        if (state == RecyclerView.SCROLL_STATE_IDLE && recyclerView != null && callback != null) {
            int recyclerCenterX = recyclerView.getWidth() / 2;

            int minDistance = recyclerView.getWidth();
            int selectedPosition = -1;

            for (int i = 0; i < recyclerView.getChildCount(); i++) {
                View child = recyclerView.getChildAt(i);
                int childCenterX = (getDecoratedLeft(child) + getDecoratedRight(child)) / 2;
                int distance = Math.abs(childCenterX - recyclerCenterX);
                if (distance < minDistance) {
                    minDistance = distance;
                    selectedPosition = recyclerView.getChildAdapterPosition(child);
                }
            }

            if (selectedPosition != -1) {
                int finalSelectedPosition = selectedPosition;
                recyclerView.post(() -> callback.onItemSelected(finalSelectedPosition));
            }
        }
    }

    public interface OnItemSelectedListener {
        void onItemSelected(int position);
    }
}
