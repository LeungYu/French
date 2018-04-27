package com.glf.roideladictee;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

import com.glf.roideladictee.TranslateWindow.TranslatorFrame;
import com.glf.roideladictee.tools.BaseActivity;
import com.glf.roideladictee.tools.MeasureView;

public class LaunchScreen extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch_screen);
        LinearLayout root=(LinearLayout)findViewById(R.id.root);
        //测量宽高
        MeasureView.measure(root);
        Button button=(Button)findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent TranslatorFrameActivity = new Intent(LaunchScreen.this, TranslatorFrame.class);
                TranslatorFrameActivity.putExtra("target", "Dictée");
                startActivity(TranslatorFrameActivity);
            }
        });
    }
}
