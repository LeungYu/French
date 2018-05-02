package com.glf.roideladictee.MyView;

import android.content.Context;
import android.graphics.Color;
import android.support.percent.PercentRelativeLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.glf.roideladictee.R;

/**
 * Created by 11951 on 2018-05-01.
 */

public class TestInputDialog extends PercentRelativeLayout {
    public Button ok,idontknow;
    public EditText test_input;

    private void initView(Context context) {
        View.inflate(context, R.layout.test_input_dialog, TestInputDialog.this);
        buttonInit();
        editTextInit();
    }

    public TestInputDialog(Context context){
        super(context);
        initView(context);
    }

    private void buttonInit(){
        ok = (Button)findViewById(R.id.ok);
        ok.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == 0) {
                    ok.setBackgroundColor(Color.parseColor("#7a808081"));
                }
                if (event.getAction() == 1 || event.getAction() == 3) {
                    ok.setBackgroundColor(Color.parseColor("#00000000"));
                }
                return false;
            }
        });
        idontknow = (Button)findViewById(R.id.idontknow);
        idontknow.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == 0) {
                    idontknow.setBackgroundColor(Color.parseColor("#7a808081"));
                }
                if (event.getAction() == 1 || event.getAction() == 3) {
                    idontknow.setBackgroundColor(Color.parseColor("#00000000"));
                }
                return false;
            }
        });
    }

    private void editTextInit(){
        test_input = (EditText)findViewById(R.id.test_input);
    }
}
