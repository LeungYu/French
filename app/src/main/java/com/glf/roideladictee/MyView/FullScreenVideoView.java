package com.glf.roideladictee.MyView;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.VideoView;

/**
 * Created by 11951 on 2018-04-25.
 */

public class FullScreenVideoView extends VideoView {
    public FullScreenVideoView(Context context) {
        super(context);
    }

    public FullScreenVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FullScreenVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width;
        int height;
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
        width = widthSpecSize;
        height = heightSpecSize;
        setMeasuredDimension(width, height);
    }

    @Override
    public void start() {
        super.start();
        if(mOnStatusChangeListener != null)mOnStatusChangeListener.onPlay();
    }

    @Override
    public void pause() {
        super.pause();
        if(mOnStatusChangeListener != null)mOnStatusChangeListener.onPause();
    }

    public interface OnStatusChangeListener {
        void onPause();
        void onPlay();
    }

    private OnStatusChangeListener mOnStatusChangeListener;

    public void setOnStatusChangeListener(OnStatusChangeListener onStatusChangeListener) {
        mOnStatusChangeListener = onStatusChangeListener;
    }
}
