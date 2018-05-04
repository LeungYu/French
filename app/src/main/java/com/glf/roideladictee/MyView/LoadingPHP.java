package com.glf.roideladictee.MyView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.glf.roideladictee.R;

/**
 * Created by 11951 on 2018-05-04.
 */

public class LoadingPHP extends RelativeLayout {
    public LoadingPHP(Context context) {
        super(context);
        initView(context);
    }

    public LoadingPHP(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public LoadingPHP(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        View.inflate(context, R.layout.loading_php, LoadingPHP.this);
    }

}
