package com.glf.roideladictee;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.annotation.IdRes;
import android.support.v7.app.AlertDialog;
import android.text.TextPaint;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.glf.roideladictee.Json.Check_Verify_Code_Json;
import com.glf.roideladictee.Json.Get_Phone_Num_Time_Json;
import com.glf.roideladictee.metroloading.MetroLoadingView;
import com.glf.roideladictee.tools.BaseActivity;
import com.glf.roideladictee.tools.LocaleUtils;
import com.glf.roideladictee.tools.MeasureView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.glf.roideladictee.tools.LocaleUtils.LOCALE_CHINESE;
import static java.util.Locale.CHINESE;
import static java.util.Locale.FRENCH;


public class LoginPage extends BaseActivity {
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    LinearLayout root;
    RelativeLayout.LayoutParams root_lp;
    ImageView login_page_login_logo;
    LinearLayout.LayoutParams login_page_login_logo_lp;
    TextView login_page_login_title;
    LinearLayout.LayoutParams login_page_login_title_lp;
    TextView login_page_phone_title;
    LinearLayout.LayoutParams login_page_phone_title_lp;
    EditText login_page_phone;
    LinearLayout.LayoutParams login_page_phone_lp;
    TextView login_page_verify_title;
    LinearLayout.LayoutParams login_page_verify_title_lp;
    EditText login_page_verify;
    LinearLayout.LayoutParams login_page_verify_lp;
    Button login_page_send_code;
    LinearLayout.LayoutParams login_page_send_code_lp;
    RadioGroup login_page_language;
    LinearLayout.LayoutParams login_page_language_lp;
    TextView login_page_language_title;
    LinearLayout.LayoutParams login_page_language_title_lp;
    RadioButton login_page_language_zh;
    LinearLayout.LayoutParams login_page_language_zh_lp;
    RadioButton login_page_language_fr;
    LinearLayout.LayoutParams login_page_language_fr_lp;
    Button login_page_login;
    LinearLayout.LayoutParams login_page_login_lp;
    TextView login_page_error;
    LinearLayout.LayoutParams login_page_error_lp;
    LinearLayout login_page_loading_bar_container;
    RelativeLayout.LayoutParams login_page_loading_bar_container_lp;
    MetroLoadingView login_page_loading_bar;
    LinearLayout.LayoutParams login_page_loading_bar_lp;
    private boolean network=true;
    private boolean AlertDialogOn=false;
    private String error_content="";
    private String login_user="";
    private boolean is_new_user=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        root=(LinearLayout)findViewById(R.id.login_page_root);
        root_lp = (RelativeLayout.LayoutParams) root.getLayoutParams();
        //测量宽高
        MeasureView.measure(root);
        initStyle();
    }
    final Handler LoginPageErrorHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    String data = (String)msg.obj;
                    if(data==null)
                        return;
                    if(data.equals("phone_num_null")){
                        login_page_error.setText(getResources().getString(R.string.login_page_error_1));
                        login_page_error.setVisibility(View.VISIBLE);
                        return;
                    }
                    if(data.equals("phone_num_oversend")){
                        login_page_error.setText(getResources().getString(R.string.login_page_error_2));
                        login_page_error.setVisibility(View.VISIBLE);
                        return;
                    }
                    if(data.equals("phone_num_or_code_null")){
                        login_page_error.setText(getResources().getString(R.string.login_page_error_3));
                        login_page_error.setVisibility(View.VISIBLE);
                        return;
                    }
                    if(data.equals("phone_num_mistake")){
                        login_page_error.setText(getResources().getString(R.string.login_page_error_4));
                        login_page_error.setVisibility(View.VISIBLE);
                        return;
                    }
                    if(data.equals("verify_code_error")){
                        login_page_error.setText(getResources().getString(R.string.login_page_error_5));
                        login_page_error.setVisibility(View.VISIBLE);
                        return;
                    }
                    if(data.equals("verify_code_over_time")){
                        login_page_error.setText(getResources().getString(R.string.login_page_error_6));
                        login_page_error.setVisibility(View.VISIBLE);
                        return;
                    }
                    if(data.equals("get_verify_code_fail")){
                        login_page_error.setText(getResources().getString(R.string.login_page_error_7));
                        login_page_error.setVisibility(View.VISIBLE);
                        return;
                    }
                    if(data.equals("server_expection")){
                        login_page_error.setText(getResources().getString(R.string.login_page_error_8));
                        login_page_error.setVisibility(View.VISIBLE);
                        return;
                    }
                    if(data.equals("login_fail")){
                        login_page_error.setText(getResources().getString(R.string.login_page_error_9));
                        login_page_error.setVisibility(View.VISIBLE);
                        return;
                    }
                    if(data.equals("access_fail")){
                        login_page_error.setText(getResources().getString(R.string.login_page_error_10));
                        login_page_error.setVisibility(View.VISIBLE);
                        return;
                    }
                    if(data.equals("tips")){
                        new AlertDialog.Builder(LoginPage.this).setTitle(getResources().getString(R.string.login_page_dialog_title_1))
                                .setMessage(error_content)
                                .setPositiveButton(getResources().getString(R.string.login_page_dialog_ok), new DialogInterface.OnClickListener() {//添加确定按钮
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // TODO Auto-generated method stub
                                        AlertDialogOn=false;
                                    }
                                }).show();
                    }
                    if(data.equals("error")){
                        new AlertDialog.Builder(LoginPage.this).setTitle(getResources().getString(R.string.login_page_dialog_title_2))
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
    final Handler LoginPageVerifyCodeSendHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    String data = (String)msg.obj;
                    if(data==null)
                        return;
                    if(data.equals("lock")) {
                        login_page_error.setVisibility(View.INVISIBLE);
                        login_page_send_code.setClickable(false);
                        login_page_send_code.setAlpha(0.65f);

                    }
                    else if(data.equals("unlock")) {
                        login_page_send_code.setClickable(true);
                        login_page_send_code.setAlpha(1f);
                        login_page_send_code.setText(getResources().getString(R.string.login_page_send_code));
                    }
                    else if(data.matches("\\d+"))
                        if(LocaleUtils.getUserLocale(LoginPage.this).equals(Locale.CHINESE))
                            login_page_send_code.setText(getResources().getString(R.string.login_page_send_code_wait_1) + data + getResources().getString(R.string.login_page_send_code_wait_2));
                        else
                            login_page_send_code.setText("(" + data + getResources().getString(R.string.login_page_send_code_wait_2));
                    break;
                default:
                    break;
            }
        }
    };
    final Handler LoginPageLoaderHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    String data = (String)msg.obj;
                    if(data==null)
                        return;
                    if(data.equals("start_load")) {
                        login_page_phone.setEnabled(false);
                        login_page_verify.setEnabled(false);
                        login_page_login.setEnabled(false);
                        login_page_send_code.setEnabled(false);
                        login_page_loading_bar.start();
                        login_page_loading_bar_container.setAlpha(0.5f);
                    }
                    else if(data.equals("stop_load")) {
                        login_page_loading_bar_container.setAlpha(0f);
                        login_page_loading_bar.stop();
                        login_page_phone.setEnabled(true);
                        login_page_verify.setEnabled(true);
                        login_page_login.setEnabled(true);
                        login_page_send_code.setEnabled(true);
                    }
                    else if(data.equals("start_send")) {
                        login_page_phone.setEnabled(false);
                        login_page_verify.setEnabled(false);
                        login_page_login.setEnabled(false);
                        login_page_send_code.setEnabled(false);
                        login_page_send_code.setText(getResources().getString(R.string.login_page_send_code_ing));
                    }
                    else if(data.equals("end_send")) {
                        login_page_phone.setEnabled(true);
                        login_page_verify.setEnabled(true);
                        login_page_login.setEnabled(true);
                        login_page_send_code.setEnabled(true);
                        login_page_send_code.setText(getResources().getString(R.string.login_page_send_code));
                    }
                default:
                    break;
            }
        }
    };
    public void initStyle() {
        Typeface YAHEI = Typeface.createFromAsset(getAssets(), "fonts/YAHEI.ttc");
        Typeface YOUYUAN = Typeface.createFromAsset(getAssets(), "fonts/YOUYUAN.TTF");
        login_page_login_logo = (ImageView) findViewById(R.id.login_page_login_logo);
        login_page_login_logo_lp = (LinearLayout.LayoutParams) login_page_login_logo.getLayoutParams();
        login_page_login_title = (TextView) findViewById(R.id.login_page_login_title);
        TextPaint login_page_login_title_paint = login_page_login_title.getPaint();
        if(LocaleUtils.getUserLocale(this).equals(Locale.FRENCH))
            login_page_login_title_paint.setTypeface(YAHEI);
        else
            login_page_login_title_paint.setTypeface(YOUYUAN);
        login_page_login_title_lp = (LinearLayout.LayoutParams) login_page_login_title.getLayoutParams();
        login_page_phone_title = (TextView) findViewById(R.id.login_page_phone_title);
        TextPaint login_page_phone_title_paint = login_page_phone_title.getPaint();
        login_page_phone_title_paint.setTypeface(YAHEI);
        login_page_phone_title_lp = (LinearLayout.LayoutParams) login_page_phone_title.getLayoutParams();
        login_page_phone = (EditText) findViewById(R.id.login_page_phone);
        TextPaint login_page_phone_paint = login_page_phone.getPaint();
        login_page_phone_paint.setTypeface(YAHEI);
        login_page_phone_lp = (LinearLayout.LayoutParams) login_page_phone.getLayoutParams();
        login_page_verify_title = (TextView) findViewById(R.id.login_page_verify_title);
        TextPaint login_page_verify_title_paint = login_page_verify_title.getPaint();
        login_page_verify_title_paint.setTypeface(YAHEI);
        login_page_verify_title_lp = (LinearLayout.LayoutParams) login_page_verify_title.getLayoutParams();
        login_page_verify = (EditText) findViewById(R.id.login_page_verify);
        TextPaint login_page_verify_paint = login_page_verify.getPaint();
        login_page_verify_paint.setTypeface(YAHEI);
        login_page_verify_lp = (LinearLayout.LayoutParams) login_page_verify.getLayoutParams();
        login_page_language = (RadioGroup) findViewById(R.id.login_page_language);
        login_page_language_lp = (LinearLayout.LayoutParams) login_page_language.getLayoutParams();
        login_page_language_title = (TextView) findViewById(R.id.login_page_language_title);
        TextPaint login_page_language_title_paint = login_page_language_title.getPaint();
        login_page_language_title_paint.setTypeface(YAHEI);
        login_page_language_title_lp = (LinearLayout.LayoutParams) login_page_language_title.getLayoutParams();
        login_page_language_zh = (RadioButton) findViewById(R.id.login_page_language_zh);
        TextPaint login_page_language_zh_paint = login_page_language_zh.getPaint();
        login_page_language_zh_paint.setTypeface(YAHEI);
        login_page_language_zh_lp = (LinearLayout.LayoutParams) login_page_language_zh.getLayoutParams();
        login_page_language_fr = (RadioButton) findViewById(R.id.login_page_language_fr);
        TextPaint login_page_language_fr_paint = login_page_language_fr.getPaint();
        login_page_language_fr_paint.setTypeface(YAHEI);
        login_page_language_fr_lp = (LinearLayout.LayoutParams) login_page_language_fr.getLayoutParams();
        login_page_send_code = (Button) findViewById(R.id.login_page_send_code);
        TextPaint login_page_send_code_paint = login_page_send_code.getPaint();
        login_page_send_code_paint.setTypeface(YAHEI);
        login_page_send_code_lp = (LinearLayout.LayoutParams) login_page_send_code.getLayoutParams();
        login_page_login = (Button) findViewById(R.id.login_page_login);
        TextPaint login_page_login_paint = login_page_login.getPaint();
        login_page_login_paint.setTypeface(YAHEI);
        login_page_login_lp = (LinearLayout.LayoutParams) login_page_login.getLayoutParams();
        login_page_error = (TextView) findViewById(R.id.login_page_error);
        TextPaint login_page_error_paint = login_page_error.getPaint();
        login_page_error_paint.setTypeface(YAHEI);
        login_page_error_lp = (LinearLayout.LayoutParams) login_page_error.getLayoutParams();
        login_page_loading_bar_container = (LinearLayout) findViewById(R.id.login_page_loading_bar_container);
        login_page_loading_bar_container_lp = (RelativeLayout.LayoutParams) login_page_loading_bar_container.getLayoutParams();
        login_page_loading_bar = (MetroLoadingView) findViewById(R.id.login_page_loading_bar);
        login_page_loading_bar_lp = (LinearLayout.LayoutParams) login_page_loading_bar.getLayoutParams();
        //辨别语言
        Locale loc = LocaleUtils.getUserLocale(this);
        System.out.println("当前语言："+loc);
        if (loc.equals(CHINESE)){
            System.out.println("设置中文按钮");
            login_page_language_fr.setChecked(false);
            login_page_language_zh.setChecked(true);
        }
        else if(loc.equals(FRENCH)){
            System.out.println("设置法语按钮");
            login_page_language_fr.setChecked(true);
            login_page_language_zh.setChecked(false);
        }
        //绑定按钮
        login_page_phone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                login_page_error.setVisibility(View.INVISIBLE);
                if (hasFocus) {
                    login_page_phone.setAlpha(1);
                } else {
                    login_page_phone.setAlpha((float)0.65);
                }
            }
        });
        login_page_verify.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                login_page_error.setVisibility(View.INVISIBLE);
                if (hasFocus) {
                    login_page_verify.setAlpha(1);
                } else {
                    login_page_verify.setAlpha((float)0.65);
                }
            }
        });
        login_page_language.setOnCheckedChangeListener(new OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.login_page_language_zh:
                        if (LocaleUtils.needUpdateLocale(LoginPage.this, LOCALE_CHINESE)) {
                            LocaleUtils.updateLocale(LoginPage.this, LOCALE_CHINESE);
                            LoginPage.this.restartAct();
                        }
                        break;
                    case R.id.login_page_language_fr:
                        if (LocaleUtils.needUpdateLocale(LoginPage.this, LocaleUtils.LOCALE_FRENCH)) {
                            LocaleUtils.updateLocale(LoginPage.this, LocaleUtils.LOCALE_FRENCH);
                            LoginPage.this.restartAct();
                        }
                        break;
                    default:break;
                }
            }
        });
        login_page_send_code.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP){
                    login_page_send_code.setAlpha(1f);
                }
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    login_page_send_code.setAlpha(0.75f);
                }
                return false;
            }
        });
        final View.OnTouchListener login_page_send_code_listener=new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP){
                    login_page_send_code.setAlpha(1);
                }
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    login_page_send_code.setAlpha(0.65f);
                }

                return false;
            }
        };

        login_page_send_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(){
                    public void run(){
                        String phone_num=login_page_phone.getText().toString();
                        if(phone_num.length()==0){
                            LoginPageErrorHandler.sendEmptyMessage(0);
                            Message msgerror =new Message();
                            msgerror.obj = "phone_num_null";
                            LoginPageErrorHandler.sendMessage(msgerror);
                            return;
                        }
                        else if(!isMobileNum(login_page_phone.getText().toString())){
                            LoginPageErrorHandler.sendEmptyMessage(0);
                            Message msgerror =new Message();
                            msgerror.obj = "phone_num_mistake";
                            LoginPageErrorHandler.sendMessage(msgerror);
                            return;
                        }
                        LoginPageLoaderHandler.sendEmptyMessage(0);
                        Message loader_start = new Message();
                        loader_start.obj = "start_send";
                        LoginPageLoaderHandler.sendMessage(loader_start);
                        try{
                            int time=100;
                            final OkHttpClient okHttpClient = new OkHttpClient();
                            FormBody body = new FormBody.Builder()
                                    .add("phone_num",phone_num)
                                    .build();
                            final Request request_Get_Phone_Num_Time = new Request.Builder()
                                    .url("http://fr.xsinweb.com/fr/service/Get_Phone_Num_Time.php")
                                    .post(body)
                                    .build();
                            int first_code=-1;
                            try {
                                Response response = null;
                                response = okHttpClient.newCall(request_Get_Phone_Num_Time).execute();
                                String getInfo = response.body().string();
                                first_code=response.code();
                                if(first_code==200) {
                                    network=true;
                                    Gson gson = new Gson();
                                    java.lang.reflect.Type type = new TypeToken<Get_Phone_Num_Time_Json>() {}.getType();
                                    Get_Phone_Num_Time_Json jsonBean = gson.fromJson(getInfo, type);
                                    time=jsonBean.time;
                                    Log.e("hahaha","今天该号码获取次数："+Integer.toString(time));
                                }
                                else{
                                    Log.e("hahaha","错误码："+Integer.toString(first_code));
                                    network=false;
                                    LoginPageErrorHandler.sendEmptyMessage(0);
                                    Message msgerror = new Message();
                                    msgerror.obj = "get_verify_code_fail";
                                    LoginPageErrorHandler.sendMessage(msgerror);
                                    LoginPageLoaderHandler.sendEmptyMessage(0);
                                    Message loader_end = new Message();
                                    loader_end.obj = "end_send";
                                    LoginPageLoaderHandler.sendMessage(loader_end);
                                    return;
                                }
                            } catch (Exception e) {
                                network=false;
                                Log.e("hahaha","错误码："+e.toString());
                                LoginPageErrorHandler.sendEmptyMessage(0);
                                Message msgerror = new Message();
                                msgerror.obj = "get_verify_code_fail";
                                LoginPageErrorHandler.sendMessage(msgerror);
                                LoginPageLoaderHandler.sendEmptyMessage(0);
                                Message loader_end = new Message();
                                loader_end.obj = "end_send";
                                LoginPageLoaderHandler.sendMessage(loader_end);

                                return;
                            }
                            if(time>=5){
                                LoginPageLoaderHandler.sendEmptyMessage(0);
                                Message loader_end = new Message();
                                loader_end.obj = "end_send";
                                LoginPageLoaderHandler.sendMessage(loader_end);
                                LoginPageErrorHandler.sendEmptyMessage(0);
                                Message msgerror =new Message();
                                msgerror.obj = "phone_num_oversend";
                                LoginPageErrorHandler.sendMessage(msgerror);
                                return;
                            }
                            body = new FormBody.Builder()
                                    .add("phone_num",phone_num)
                                    .build();
                            final Request request_Get_VerifyCode = new Request.Builder()
                                    .url("http://fr.xsinweb.com/fr/service/Get_VerifyCode.php")
                                    .post(body)
                                    .build();
                            try {
                                Response response = null;
                                response = okHttpClient.newCall(request_Get_VerifyCode).execute();
                                String getInfo = response.body().string();
                                Log.e("hahaha","返回的消息："+getInfo);
                                if(response.code()==200) {
                                    network=true;
                                    Log.e("hahaha","获取成功");
                                    login_page_send_code.setOnTouchListener(null);
                                    LoginPageLoaderHandler.sendEmptyMessage(0);
                                    Message loader_end = new Message();
                                    loader_end.obj = "end_send";
                                    LoginPageLoaderHandler.sendMessage(loader_end);
                                    LoginPageVerifyCodeSendHandler.sendEmptyMessage(0);
                                    Message msgstart = new Message();
                                    msgstart.obj = "lock";
                                    LoginPageVerifyCodeSendHandler.sendMessage(msgstart);
                                }
                                else {
                                    network = false;
                                    error_content="无法获取验证码，错误：" + response.code();
                                    LoginPageLoaderHandler.sendEmptyMessage(0);
                                    Message loader_end = new Message();
                                    loader_end.obj = "end_send";
                                    LoginPageLoaderHandler.sendMessage(loader_end);
                                    LoginPageErrorHandler.sendEmptyMessage(0);
                                    Message msgerror = new Message();
                                    msgerror.obj = "error";
                                    LoginPageErrorHandler.sendMessage(msgerror);
                                }
                            } catch (Exception e) {
                                LoginPageLoaderHandler.sendEmptyMessage(0);
                                Message loader_end = new Message();
                                loader_end.obj = "end_send";
                                LoginPageLoaderHandler.sendMessage(loader_end);
                                e.printStackTrace();
                            }

                            for(int i=30;i>=0;i--) {
                                sleep(1000);
                                if(!network)
                                    break;
                                LoginPageErrorHandler.sendEmptyMessage(0);
                                Message msgin =new Message();
                                msgin.obj = new Integer(i).toString();
                                LoginPageVerifyCodeSendHandler.sendMessage(msgin);
                            }
                            LoginPageErrorHandler.sendEmptyMessage(0);
                            Message msgend =new Message();
                            msgend.obj = "unlock";
                            LoginPageVerifyCodeSendHandler.sendMessage(msgend);
                            login_page_send_code.setOnTouchListener(login_page_send_code_listener);
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                }.start();


            }
        });
        login_page_login.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP){
                    login_page_login.setAlpha(1f);
                }
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    login_page_login.setAlpha(0.75f);
                }
                return false;
            }
        });
        login_page_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread() {
                    public void run() {
                        network=false;
                        LoginPageLoaderHandler.sendEmptyMessage(0);
                        Message loader_start = new Message();
                        loader_start.obj = "start_load";
                        LoginPageLoaderHandler.sendMessage(loader_start);
                        String phone_num = login_page_phone.getText().toString();
                        String verify_code = login_page_verify.getText().toString();
                        if (login_page_phone.getText().toString().length() == 0 || login_page_verify.getText().toString().length() == 0) {
                            LoginPageLoaderHandler.sendEmptyMessage(0);
                            Message loader_stop = new Message();
                            loader_stop.obj = "stop_load";
                            LoginPageLoaderHandler.sendMessage(loader_stop);
                            LoginPageErrorHandler.sendEmptyMessage(0);
                            Message msgerror = new Message();
                            msgerror.obj = "phone_num_or_code_null";
                            LoginPageErrorHandler.sendMessage(msgerror);
                            return;
                        } else if (!isMobileNum(login_page_phone.getText().toString())) {
                            LoginPageLoaderHandler.sendEmptyMessage(0);
                            Message loader_stop = new Message();
                            loader_stop.obj = "stop_load";
                            LoginPageLoaderHandler.sendMessage(loader_stop);
                            LoginPageErrorHandler.sendEmptyMessage(0);
                            Message msgerror = new Message();
                            msgerror.obj = "phone_num_mistake";
                            LoginPageErrorHandler.sendMessage(msgerror);
                            return;
                        }
                        boolean verify_code_exist = false;
                        boolean verify_code_available = false;
                        final OkHttpClient okHttpClient = new OkHttpClient();
                        FormBody body = new FormBody.Builder()
                                .add("phone_num", phone_num)
                                .add("verify_code", verify_code)
                                .build();
                        final Request request_Get_Phone_Num_Time = new Request.Builder()
                                .url("http://fr.xsinweb.com/fr/service/Check_Verify_Code.php")
                                .post(body)
                                .build();
                        try {
                            Response response = null;
                            response = okHttpClient.newCall(request_Get_Phone_Num_Time).execute();
                            String getInfo = response.body().string();
                            if (response.code() == 200) {
                                network = true;
                                Gson gson = new Gson();
                                Type type = new TypeToken<Check_Verify_Code_Json>() {
                                }.getType();
                                Check_Verify_Code_Json jsonBean = gson.fromJson(getInfo, type);
                                verify_code_exist = jsonBean.verify_code_exist;
                                verify_code_available = jsonBean.verify_code_available;
                                System.out.println("verify_code_exist:" + verify_code_exist + "verify_code_available:" + verify_code_available);
                                if (verify_code_exist && verify_code_available){
                                    login_user = phone_num;
                                    body = new FormBody.Builder()
                                            .add("phone_num", phone_num)
                                            .build();
                                    final Request request_Login_Check = new Request.Builder()
                                            .url("http://fr.xsinweb.com/fr/service/Login_Check.php")
                                            .post(body)
                                            .build();
                                    Response login_response = null;
                                    login_response = okHttpClient.newCall(request_Login_Check).execute();
                                    String login_getInfo = login_response.body().string();
                                    network=false;
                                    if (login_response.code() == 200) {
                                        System.out.println("登陆检查："+login_response.body().toString());
                                        network=true;
                                        login_user = phone_num;
                                        System.out.println(login_getInfo);
                                        sharedpreferences= PreferenceManager.getDefaultSharedPreferences(LoginPage.this);
                                        editor=sharedpreferences.edit();
                                        editor.clear();
                                        editor.putString("CurrentAccount",phone_num);
                                        editor.apply();
                                        if (login_getInfo.equals("1")) {
                                            System.out.println("goto IndexPage");
                                            is_new_user = false;
                                        } else {
                                            System.out.println("goto NewLoginPage");
                                            is_new_user = true;
                                        }
                                        LoginPageLoaderHandler.sendEmptyMessage(0);
                                        Message loader_stop = new Message();
                                        loader_stop.obj = "stop_load";
                                        LoginPageLoaderHandler.sendMessage(loader_stop);
                                    } else {
                                        System.out.println("登陆检查失败");
                                        network = false;
                                        LoginPageErrorHandler.sendEmptyMessage(0);
                                        Message msgerror = new Message();
                                        msgerror.obj = "server_expection";
                                        LoginPageErrorHandler.sendMessage(msgerror);
                                        LoginPageLoaderHandler.sendEmptyMessage(0);
                                        Message loader_stop = new Message();
                                        loader_stop.obj = "stop_load";
                                        LoginPageLoaderHandler.sendMessage(loader_stop);
                                        return;
                                    }
                                }
                            } else {
                                System.out.println("http://fr.xsinweb.com/fr/service/Check_Verify_Code.php的code");
                                network=false;
                                LoginPageErrorHandler.sendEmptyMessage(0);
                                Message msgerror = new Message();
                                msgerror.obj = "access_fail";
                                LoginPageErrorHandler.sendMessage(msgerror);
                                LoginPageLoaderHandler.sendEmptyMessage(0);
                                Message loader_stop = new Message();
                                loader_stop.obj = "stop_load";
                                LoginPageLoaderHandler.sendMessage(loader_stop);
                                return;
                            }
                        } catch (Exception e) {
                            System.out.println("捕获到异常");
                            Log.e("hahaha", e.toString());
                            LoginPageErrorHandler.sendEmptyMessage(0);
                            Message msgerror = new Message();
                            msgerror.obj = "access_fail";
                            LoginPageErrorHandler.sendMessage(msgerror);
                            LoginPageLoaderHandler.sendEmptyMessage(0);
                            Message loader_stop = new Message();
                            loader_stop.obj = "stop_load";
                            LoginPageLoaderHandler.sendMessage(loader_stop);
                            return;
                        }
                        LoginPageLoaderHandler.sendEmptyMessage(0);
                        Message loader_stop = new Message();
                        loader_stop.obj = "stop_load";
                        LoginPageLoaderHandler.sendMessage(loader_stop);
                        if (!verify_code_exist) {
                            LoginPageErrorHandler.sendEmptyMessage(0);
                            Message msgerror = new Message();
                            msgerror.obj = "verify_code_error";
                            LoginPageErrorHandler.sendMessage(msgerror);
                        } else if (!verify_code_available) {
                            LoginPageErrorHandler.sendEmptyMessage(0);
                            Message msgerror = new Message();
                            msgerror.obj = "verify_code_over_time";
                            LoginPageErrorHandler.sendMessage(msgerror);
                        } else {/*后台：此处补充登陆操作*/
                            if (network) {
                                AlertDialogOn=true;
                                error_content=getResources().getString(R.string.login_page_login_success);
                                LoginPageErrorHandler.sendEmptyMessage(0);
                                Message msgerror = new Message();
                                msgerror.obj = "tips";
                                LoginPageErrorHandler.sendMessage(msgerror);
                                isLogin=true;
                                if(is_new_user){
                                    Log.e("hahaha","goto NewLoginPage");
                                    while (AlertDialogOn);
                                    Intent Newer_Login_Page_Activity = new Intent(LoginPage.this, NewLoginPage.class);
                                    LoginPage.super.login_user=login_user;
                                    Newer_Login_Page_Activity.putExtra("login_user", login_user);
                                    startActivity(Newer_Login_Page_Activity);
                                    LoginPage.this.finish();
                                }
                                else{
                                    Log.e("hahaha","goto IndexPage");
                                    while (AlertDialogOn);
                                    Intent Index_Page_Activity = new Intent(LoginPage.this, IndexPage.class);
                                    LoginPage.super.login_user=login_user;
                                    Index_Page_Activity.putExtra("login_user", login_user);
                                    if(login_page_language.getCheckedRadioButtonId()==R.id.login_page_language_zh){
                                        language="ZH";
                                    }else{
                                        language="FR";
                                    }
                                    startActivity(Index_Page_Activity);
                                    LoginPage.this.finish();
                                }

                                body = new FormBody.Builder()
                                        .add("phone_num", phone_num)
                                        .build();
                                final Request request_Login_Check = new Request.Builder()
                                        .url("http://fr.xsinweb.com/fr/service/Clean_Logined_Code.php")
                                        .post(body)
                                        .build();
                                Response login_response = null;
                                try {
                                    login_response = okHttpClient.newCall(request_Login_Check).execute();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                LoginPage.this.finish();
                            } else {
                                LoginPageErrorHandler.sendEmptyMessage(0);
                                Message msgerror = new Message();
                                msgerror.obj = "login_fail";
                                LoginPageErrorHandler.sendMessage(msgerror);
                            }
                        }
                    }
                }.start();
            }
        });
    }
    public static boolean isMobileNum(String Num) {
        Pattern p = Pattern.compile("^134[0-8]\\d{7}$|^13[^4]\\d{8}$|^14[5-9]\\d{8}$|^15[^4]\\d{8}$|^16[6]\\d{8}$|^17[0-8]\\d{8}$|^18[\\d]{9}$|^19[8,9]\\d{8}$");
        Matcher m = p.matcher(Num);
        return m.matches();
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && event.getAction() == KeyEvent.ACTION_DOWN) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 重启当前Activity
     */
    private void restartAct() {
        finish();
        Intent _Intent = new Intent(this, LoginPage.class);
        startActivity(_Intent);
        //清除Activity退出和进入的动画
        overridePendingTransition(0, 0);
    }
}
