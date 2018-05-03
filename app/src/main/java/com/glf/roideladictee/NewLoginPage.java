package com.glf.roideladictee;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.text.TextPaint;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.glf.roideladictee.metroloading.MetroLoadingView;
import com.glf.roideladictee.tools.BaseActivity;
import com.glf.roideladictee.tools.MeasureView;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NewLoginPage extends BaseActivity {
    RelativeLayout root;
    FrameLayout.LayoutParams root_lp;
    TextView new_login_page_title;
    RelativeLayout.LayoutParams new_login_page_title_lp;
    TextView new_login_page_tips;
    LinearLayout.LayoutParams new_login_page_tips_lp;
    TextView new_login_page_schoool_name_title;
    LinearLayout.LayoutParams new_login_page_schoool_name_title_lp;
    EditText new_login_page_schoool_name;
    LinearLayout.LayoutParams new_login_page_schoool_name_lp;
    TextView new_login_page_real_name_title;
    LinearLayout.LayoutParams new_login_page_real_name_title_lp;
    EditText new_login_page_real_name;
    LinearLayout.LayoutParams new_login_page_real_name_lp;
    Button new_login_page_clear_content;
    LinearLayout.LayoutParams new_login_page_clear_content_lp;
    Button new_login_page_submit;
    LinearLayout.LayoutParams new_login_page_submit_lp;
    TextView new_login_page_error;
    LinearLayout.LayoutParams new_login_page_error_lp;
    LinearLayout new_login_page_loading_bar_container;
    RelativeLayout.LayoutParams new_login_page_loading_bar_container_lp;
    MetroLoadingView new_login_page_loading_bar;
    LinearLayout.LayoutParams new_login_page_loading_bar_lp;
    private boolean network=true;
    private String error_content="";
    private String login_user="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_login_page);
        root=(RelativeLayout)findViewById(R.id.new_login_page_root);
        root_lp = (FrameLayout.LayoutParams) root.getLayoutParams();
        //测量宽高
        MeasureView.measure(root);
        initStyle();
    }
    final Handler NewLoginPageClearHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    String data = (String)msg.obj;
                    if(data==null)
                        return;
                    if(data.equals("clear")) {
                        new_login_page_schoool_name.setText("");
                        new_login_page_real_name.setText("");
                    }
                default:
                    break;
            }
        }
    };
    final Handler NewLoginPageErrorHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    String data = (String)msg.obj;
                    if(data==null)
                        return;
                    if(data.equals("server_expection")){
                        new_login_page_error.setText(getResources().getString(R.string.new_login_page_error_1));
                        new_login_page_error.setVisibility(View.VISIBLE);
                        return;
                    }
                    if(data.equals("access_fail")){
                        new_login_page_error.setText(getResources().getString(R.string.new_login_page_error_2));
                        new_login_page_error.setVisibility(View.VISIBLE);
                        return;
                    }
                    if(data.equals("content_null")){
                        new_login_page_error.setText(getResources().getString(R.string.new_login_page_error_0));
                        new_login_page_error.setVisibility(View.VISIBLE);
                        return;
                    }
                    if(data.equals("error")){
                        new AlertDialog.Builder(NewLoginPage.this).setTitle(getResources().getString(R.string.login_page_dialog_title_2))
                                .setMessage(error_content)
                                .setPositiveButton(getResources().getString(R.string.login_page_dialog_ok), new DialogInterface.OnClickListener() {//添加确定按钮
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // TODO Auto-generated method stub
                                    }
                                }).show();
                    }
                    break;
                default:
                    break;
            }
        }
    };
    final Handler NewLoginPageLoaderHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    String data = (String)msg.obj;
                    if(data==null)
                        return;
                    if(data.equals("start_load")) {
                        new_login_page_schoool_name.setEnabled(false);
                        new_login_page_real_name.setEnabled(false);
                        new_login_page_clear_content.setEnabled(false);
                        new_login_page_submit.setEnabled(false);
                        new_login_page_loading_bar.start();
                        new_login_page_loading_bar_container.setAlpha(0.5f);
                    }
                    else if(data.equals("stop_load")) {
                        new_login_page_loading_bar_container.setAlpha(0f);
                        new_login_page_loading_bar.stop();
                        new_login_page_schoool_name.setEnabled(true);
                        new_login_page_real_name.setEnabled(true);
                        new_login_page_clear_content.setEnabled(true);
                        new_login_page_submit.setEnabled(true);
                    }
                default:
                    break;
            }
        }
    };
    public void initStyle() {
        Typeface YAHEI = Typeface.createFromAsset(getAssets(), "fonts/YAHEI.ttc");
        Typeface YOUYUAN = Typeface.createFromAsset(getAssets(), "fonts/YOUYUAN.TTF");
        new_login_page_title = (TextView) findViewById(R.id.new_login_page_title);
        TextPaint new_login_page_title_paint = new_login_page_title.getPaint();
        new_login_page_title_paint.setTypeface(YAHEI);
        new_login_page_title_lp = (RelativeLayout.LayoutParams) new_login_page_title.getLayoutParams();
        new_login_page_tips = (TextView) findViewById(R.id.new_login_page_tips);
        TextPaint new_login_page_tips_paint = new_login_page_tips.getPaint();
        new_login_page_tips_paint.setTypeface(YAHEI);
        new_login_page_tips_lp = (LinearLayout.LayoutParams) new_login_page_tips.getLayoutParams();
        new_login_page_schoool_name_title = (TextView) findViewById(R.id.new_login_page_schoool_name_title);
        TextPaint new_login_page_schoool_name_title_paint = new_login_page_schoool_name_title.getPaint();
        new_login_page_schoool_name_title_paint.setTypeface(YAHEI);
        new_login_page_schoool_name_title_lp = (LinearLayout.LayoutParams) new_login_page_schoool_name_title.getLayoutParams();
        new_login_page_schoool_name = (EditText) findViewById(R.id.new_login_page_schoool_name);
        TextPaint new_login_page_schoool_name_paint = new_login_page_schoool_name.getPaint();
        new_login_page_schoool_name_paint.setTypeface(YAHEI);
        new_login_page_schoool_name_lp = (LinearLayout.LayoutParams) new_login_page_schoool_name.getLayoutParams();
        new_login_page_real_name_title = (TextView) findViewById(R.id.new_login_page_real_name_title);
        TextPaint new_login_page_real_name_title_paint = new_login_page_real_name_title.getPaint();
        new_login_page_real_name_title_paint.setTypeface(YAHEI);
        new_login_page_real_name_title_lp = (LinearLayout.LayoutParams) new_login_page_real_name_title.getLayoutParams();
        new_login_page_real_name = (EditText) findViewById(R.id.new_login_page_real_name);
        TextPaint new_login_page_real_name_paint = new_login_page_real_name.getPaint();
        new_login_page_real_name_paint.setTypeface(YAHEI);
        new_login_page_real_name_lp = (LinearLayout.LayoutParams) new_login_page_real_name.getLayoutParams();
        new_login_page_clear_content = (Button) findViewById(R.id.new_login_page_clear_content);
        TextPaint new_login_page_clear_content_paint = new_login_page_clear_content.getPaint();
        new_login_page_clear_content_paint.setTypeface(YAHEI);
        new_login_page_clear_content_lp = (LinearLayout.LayoutParams) new_login_page_clear_content.getLayoutParams();
        new_login_page_submit = (Button) findViewById(R.id.new_login_page_submit);
        TextPaint new_login_page_submit_paint = new_login_page_submit.getPaint();
        new_login_page_submit_paint.setTypeface(YAHEI);
        new_login_page_submit_lp = (LinearLayout.LayoutParams) new_login_page_submit.getLayoutParams();
        new_login_page_error = (TextView) findViewById(R.id.new_login_page_error);
        TextPaint new_login_page_error_paint = new_login_page_error.getPaint();
        new_login_page_error_paint.setTypeface(YAHEI);
        new_login_page_error_lp = (LinearLayout.LayoutParams) new_login_page_error.getLayoutParams();
        new_login_page_loading_bar_container = (LinearLayout) findViewById(R.id.new_login_page_loading_bar_container);
        new_login_page_loading_bar_container_lp = (RelativeLayout.LayoutParams) new_login_page_loading_bar_container.getLayoutParams();
        new_login_page_loading_bar = (MetroLoadingView) findViewById(R.id.new_login_page_loading_bar);
        new_login_page_loading_bar_lp = (LinearLayout.LayoutParams) new_login_page_loading_bar.getLayoutParams();
        //绑定按钮
        new_login_page_schoool_name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                new_login_page_error.setVisibility(View.INVISIBLE);
                if (hasFocus) {
                    new_login_page_schoool_name.setAlpha(1);
                } else {
                    new_login_page_schoool_name.setAlpha((float)0.65);
                }
            }
        });
        new_login_page_real_name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                new_login_page_error.setVisibility(View.INVISIBLE);
                if (hasFocus) {
                    new_login_page_real_name.setAlpha(1);
                } else {
                    new_login_page_real_name.setAlpha((float)0.65);
                }
            }
        });
        new_login_page_clear_content.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP){
                    new_login_page_clear_content.setAlpha(1f);
                }
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    new_login_page_clear_content.setAlpha(0.75f);
                }
                return false;
            }
        });
        new_login_page_submit.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP){
                    new_login_page_submit.setAlpha(1f);
                }
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    new_login_page_submit.setAlpha(0.75f);
                }
                return false;
            }
        });
        new_login_page_clear_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewLoginPageClearHandler.sendEmptyMessage(0);
                Message msgerror =new Message();
                msgerror.obj = "clear";
                NewLoginPageClearHandler.sendMessage(msgerror);
            }
        });
        new_login_page_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread() {
                    public void run() {
                        network=false;
                        NewLoginPageLoaderHandler.sendEmptyMessage(0);
                        Message loader_start = new Message();
                        loader_start.obj = "start_load";
                        NewLoginPageLoaderHandler.sendMessage(loader_start);
                        String schoool_name = new_login_page_schoool_name.getText().toString();
                        String real_name = new_login_page_real_name.getText().toString();
                        if (schoool_name.length() == 0 || real_name.length() == 0) {
                            NewLoginPageLoaderHandler.sendEmptyMessage(0);
                            Message loader_stop = new Message();
                            loader_stop.obj = "stop_load";
                            NewLoginPageLoaderHandler.sendMessage(loader_stop);
                            NewLoginPageErrorHandler.sendEmptyMessage(0);
                            Message msgerror = new Message();
                            msgerror.obj = "content_null";
                            NewLoginPageErrorHandler.sendMessage(msgerror);
                            return;
                        }
                        final OkHttpClient okHttpClient = new OkHttpClient();
                        FormBody body = new FormBody.Builder()
                                .add("phone_num", NewLoginPage.super.login_user)
                                .add("school_name", schoool_name)
                                .add("real_name", real_name)
                                .build();
                        final Request request_New_User_Input_Infomation = new Request.Builder()
                                .url("http://fr.xsinweb.com/fr/service/New_User_Input_Infomation.php")
                                .post(body)
                                .build();
                        try {
                            Response response = null;
                            response = okHttpClient.newCall(request_New_User_Input_Infomation).execute();
                            String getInfo = response.body().string();
                            if (response.code() == 200) {
                                System.out.println("正常状态码："+getInfo);
                                network = true;
                                NewLoginPageLoaderHandler.sendEmptyMessage(0);
                                Message loader_stop = new Message();
                                loader_stop.obj = "stop_load";
                                NewLoginPageLoaderHandler.sendMessage(loader_stop);
                                if(getInfo.equals("1")){
                                    Intent IndexPage_Activity = new Intent(NewLoginPage.this, IndexPage.class);
                                    IndexPage_Activity.putExtra("login_user", login_user);
                                    startActivity(IndexPage_Activity);
                                }
                                else{
                                    NewLoginPageErrorHandler.sendEmptyMessage(0);
                                    Message msgerror = new Message();
                                    msgerror.obj = "server_expection";
                                    NewLoginPageErrorHandler.sendMessage(msgerror);
                                }
                            }
                            else{
                                System.out.println("捕捉到错误状态码："+getInfo);
                                NewLoginPageLoaderHandler.sendEmptyMessage(0);
                                Message loader_stop = new Message();
                                loader_stop.obj = "stop_load";
                                NewLoginPageLoaderHandler.sendMessage(loader_stop);
                                NewLoginPageErrorHandler.sendEmptyMessage(0);
                                Message msgerror = new Message();
                                msgerror.obj = "access_fail";
                                NewLoginPageErrorHandler.sendMessage(msgerror);
                            }
                        }
                        catch (Exception e){
                            System.out.println("捕捉到异常："+e.toString());
                            NewLoginPageLoaderHandler.sendEmptyMessage(0);
                            Message loader_stop = new Message();
                            loader_stop.obj = "stop_load";
                            NewLoginPageLoaderHandler.sendMessage(loader_stop);
                            NewLoginPageErrorHandler.sendEmptyMessage(0);
                            Message msgerror = new Message();
                            msgerror.obj = "server_expection";
                            NewLoginPageErrorHandler.sendMessage(msgerror);
                        }
                    }
                }.start();
            }
        });
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && event.getAction() == KeyEvent.ACTION_DOWN) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
