package com.glf.roideladictee.MyView;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.glf.roideladictee.R;

/**
 * Created by 11951 on 2018-04-25.
 * 自定义返回按钮
 */

public class BackButton extends Button {

    public BackButton(Context context) {
        super(context);
        initView();
    }

    public BackButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public BackButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    protected void initView(){
        setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == 0) {
                    v.setAlpha(0.75f);
                }
                if (event.getAction() == 1 || event.getAction() == 3) {
                    v.setAlpha(1f);
                }
                return false;
            }
        });
    }

    protected void finish(){
        Context context = getContext();
        while (!(context instanceof Activity) && context instanceof ContextWrapper) {
            context = (Context) ((ContextWrapper) context).getBaseContext();
        }
        ((Activity)context).finish();
    }
}
