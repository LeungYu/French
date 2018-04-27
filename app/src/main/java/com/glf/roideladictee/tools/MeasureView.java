package com.glf.roideladictee.tools;

import android.view.View;
import android.view.ViewTreeObserver;

/**
 * Created by lenovo on 2018/4/25.
 */

public class MeasureView {
    public static int width;
    public static int height;
    public static boolean isHasExecute = false;
    public static View view;
    public static void measure(View view){
        MeasureView.view=view;
        ViewTreeObserver observer = view.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if(isHasExecute == false)
                {
                    getWidthAndHeight();
                }
                isHasExecute = true;
            }
        });
    }
    protected static void getWidthAndHeight()
    {
        int width = view.getWidth();
        int height = view.getHeight();
        System.out.println("Activity的宽: "+width+" 高: "+height);
    }
}
