package com.glf.roideladictee;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.text.TextPaint;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.glf.roideladictee.tools.BaseActivity;
import com.glf.roideladictee.tools.MeasureView;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LaunchScreen extends BaseActivity {
    SharedPreferences sharedpreferences;
    LinearLayout root;
    String phone_num="";
    RelativeLayout.LayoutParams root_lp;
    ImageView launch_screen_login_logo;
    LinearLayout.LayoutParams launch_screen_login_logo_lp;
    TextView launch_screen_login_title;
    LinearLayout.LayoutParams launch_screen_login_title_lp;
    TextView launch_screen_version;
    LinearLayout.LayoutParams launch_screen_version_lp;
    TextView launch_screen_copyright;
    LinearLayout.LayoutParams launch_screen_copyright_lp;
    private boolean network=true;
    private String error_content="";
    private boolean AlertDialogOn=false;
    private boolean is_new_user=true;
    final Handler LaunchScreenErrorHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    String data = (String)msg.obj;
                    if(data==null)
                        return;
                    if(data.equals("tips")){
                        new AlertDialog.Builder(LaunchScreen.this).setTitle(getResources().getString(R.string.login_page_dialog_title_1))
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
                        new AlertDialog.Builder(LaunchScreen.this).setTitle(getResources().getString(R.string.login_page_dialog_title_2))
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
                sharedpreferences= PreferenceManager.getDefaultSharedPreferences(LaunchScreen.this);
                phone_num=sharedpreferences.getString("CurrentAccount",null);
                if(phone_num==null){
                    try {
                        sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Intent Login_Page_Activity = new Intent(LaunchScreen.this, LoginPage.class);
                    LaunchScreen.super.login_user=login_user;
                    Login_Page_Activity.putExtra("login_user", login_user);
                    startActivity(Login_Page_Activity);
                }
                else{
                    final OkHttpClient okHttpClient = new OkHttpClient();
                    FormBody body = new FormBody.Builder()
                            .add("phone_num", phone_num)
                            .build();
                    final Request request_Login_Check = new Request.Builder()
                            .url("http://fr.xsinweb.com/fr/service/Login_Check.php")
                            .post(body)
                            .build();
                    Response login_response = null;
                    try {
                        login_response = okHttpClient.newCall(request_Login_Check).execute();
                        String login_getInfo = login_response.body().string();
                        network=false;
                        if (login_response.code() == 200) {
                            System.out.println("登陆检查："+login_response.body().toString());
                            network=true;
                            login_user = phone_num;
                            System.out.println(login_getInfo);
                            if (login_getInfo.equals("1")) {
                                System.out.println("goto IndexPage");
                                is_new_user = false;
                            } else {
                                System.out.println("goto NewLaunchScreen");
                                is_new_user = true;
                            }
                        } else {
                            System.out.println("登陆检查失败");
                            network = false;
                            LaunchScreenErrorHandler.sendEmptyMessage(0);
                            error_content=getResources().getString(R.string.login_page_error_8);
                            Message msgerror = new Message();
                            msgerror.obj = "error";
                            LaunchScreenErrorHandler.sendMessage(msgerror);
                            return;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (network) {
                        AlertDialogOn=true;
                        error_content=getResources().getString(R.string.login_page_login_success);
                        LaunchScreenErrorHandler.sendEmptyMessage(0);
                        Message msgerror = new Message();
                        msgerror.obj = "tips";
                        LaunchScreenErrorHandler.sendMessage(msgerror);
                        isLogin=true;
                        if(is_new_user){
                            Log.e("hahaha","goto NewLaunchScreen");
                            while (AlertDialogOn);
                            Intent Newer_Login_Page_Activity = new Intent(LaunchScreen.this, NewLoginPage.class);
                            LaunchScreen.super.login_user=login_user;
                            Newer_Login_Page_Activity.putExtra("login_user", login_user);
                            startActivity(Newer_Login_Page_Activity);
                        }
                        else{
                            Log.e("hahaha","goto IndexPage");
                            while (AlertDialogOn);
                            Intent Index_Page_Activity = new Intent(LaunchScreen.this, IndexPage.class);
                            LaunchScreen.super.login_user=login_user;
                            Index_Page_Activity.putExtra("login_user", login_user);
                            startActivity(Index_Page_Activity);
                        }
                    } else {
                        LaunchScreenErrorHandler.sendEmptyMessage(0);
                        error_content=getResources().getString(R.string.login_page_error_9);
                        Message msgerror = new Message();
                        msgerror.obj = "error";
                        LaunchScreenErrorHandler.sendMessage(msgerror);
                    }
                }
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
