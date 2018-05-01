package com.glf.roideladictee;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.glf.roideladictee.tools.BaseActivity;
import com.glf.roideladictee.tools.MeasureView;

public class LaunchScreen extends BaseActivity {
    LinearLayout root;
    RelativeLayout.LayoutParams root_lp;
    ImageView launch_screen_login_logo;
    LinearLayout.LayoutParams launch_screen_login_logo_lp;
    TextView launch_screen_login_title;
    LinearLayout.LayoutParams launch_screen_login_title_lp;
    TextView launch_screen_version;
    LinearLayout.LayoutParams launch_screen_version_lp;
    TextView launch_screen_copyright;
    LinearLayout.LayoutParams launch_screen_copyright_lp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch_screen);
        root=(LinearLayout)findViewById(R.id.launch_screen_root);
        root_lp = (RelativeLayout.LayoutParams) root.getLayoutParams();
        //测量宽高
        MeasureView.measure(root);
        initStyle();
        new Thread(){
            public void run(){

            }
        }.start();
    }
    public void initStyle(){
        Typeface YAHEI =Typeface.createFromAsset(getAssets(), "fonts/YAHEI.ttc");
        Typeface YOUYUAN =Typeface.createFromAsset(getAssets(), "fonts/YOUYUAN.TTF");
        launch_screen_login_logo=(ImageView)findViewById(R.id.launch_screen_login_logo);
        launch_screen_login_logo_lp=(LinearLayout.LayoutParams) launch_screen_login_logo.getLayoutParams();
        launch_screen_login_title=(TextView)findViewById(R.id.launch_screen_login_title);
        launch_screen_login_title_lp=(LinearLayout.LayoutParams) launch_screen_login_title.getLayoutParams();
        TextPaint launch_screen_login_title_paint = launch_screen_login_title.getPaint();
        launch_screen_login_title_paint.setTypeface(YOUYUAN);
        launch_screen_version=(TextView)findViewById(R.id.launch_screen_version);
        launch_screen_version_lp=(LinearLayout.LayoutParams) launch_screen_version.getLayoutParams();
        TextPaint launch_screen_version_paint = launch_screen_version.getPaint();
        launch_screen_version_paint.setTypeface(YAHEI);
        launch_screen_copyright=(TextView)findViewById(R.id.launch_screen_copyright);
        launch_screen_copyright_lp=(LinearLayout.LayoutParams) launch_screen_copyright.getLayoutParams();
        TextPaint launch_screen_copyright_paint = launch_screen_copyright.getPaint();
        launch_screen_copyright_paint.setTypeface(YAHEI);
    }
}
