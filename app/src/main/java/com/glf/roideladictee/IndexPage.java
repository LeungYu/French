package com.glf.roideladictee;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.text.TextPaint;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.glf.roideladictee.MyEnum.VideoMode;
import com.glf.roideladictee.tools.BaseActivity;
import com.glf.roideladictee.tools.LocaleUtils;
import com.glf.roideladictee.tools.MeasureView;

import java.io.File;

import static com.glf.roideladictee.tools.LocaleUtils.LOCALE_CHINESE;

public class IndexPage extends BaseActivity {
    RelativeLayout root;
    FrameLayout.LayoutParams root_lp;
    LinearLayout index_page_user;
    LinearLayout.LayoutParams index_page_user_lp;
    LinearLayout index_page_title_bar;
    RelativeLayout.LayoutParams index_page_title_bar_lp;
    ImageView index_page_user_logo;
    LinearLayout.LayoutParams index_page_user_logo_lp;
    TextView index_page_user_name;
    LinearLayout.LayoutParams index_page_user_name_lp;
    ImageView index_page_logout;
    LinearLayout.LayoutParams index_page_logout_lp;
    ImageView index_page_exit;
    LinearLayout.LayoutParams index_page_exit_lp;
    LinearLayout index_page_practise;
    LinearLayout.LayoutParams index_page_practise_lp;
    ImageView index_page_practise_img;
    LinearLayout.LayoutParams index_page_practise_img_lp;
    TextView index_page_practise_title;
    LinearLayout.LayoutParams index_page_practise_title_lp;
    LinearLayout index_page_contest;
    LinearLayout.LayoutParams index_page_contest_lp;
    ImageView index_page_contest_img;
    LinearLayout.LayoutParams index_page_contest_img_lp;
    TextView index_page_contest_title;
    LinearLayout.LayoutParams index_page_contest_title_lp;
    LinearLayout index_page_join;
    LinearLayout.LayoutParams index_page_join_lp;
    ImageView index_page_join_img;
    LinearLayout.LayoutParams index_page_join_img_lp;
    TextView index_page_join_title;
    LinearLayout.LayoutParams index_page_join_title_lp;
    LinearLayout index_page_dictionary;
    LinearLayout.LayoutParams index_page_dictionary_lp;
    ImageView index_page_dictionary_img;
    LinearLayout.LayoutParams index_page_dictionary_img_lp;
    TextView index_page_dictionary_title;
    LinearLayout.LayoutParams index_page_dictionary_title_lp;
    private String login_user="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (super.language.equals("ZH")) {
            LocaleUtils.updateLocale(IndexPage.this, LOCALE_CHINESE);
        }else{
            LocaleUtils.updateLocale(IndexPage.this, LocaleUtils.LOCALE_FRENCH);
        }
        setContentView(R.layout.activity_index_page);
        root=(RelativeLayout)findViewById(R.id.index_page_root);
        root_lp = (FrameLayout.LayoutParams) root.getLayoutParams();
        //测量宽高
        MeasureView.measure(root);
        initStyle();
    }
    final Handler IndexPageTitleBarHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    String data = (String)msg.obj;
                    if(data==null)
                        return;
                    if(data.equals("exit")) {
                        new AlertDialog.Builder(IndexPage.this).setTitle(getResources().getString(R.string.login_page_dialog_title_1))
                                .setMessage(getResources().getString(R.string.index_page_sign_off_tips))
                                .setPositiveButton(getResources().getString(R.string.login_page_dialog_ok), new DialogInterface.OnClickListener() {//添加确定按钮
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // TODO Auto-generated method stub
                                        isLogin=false;
                                        login_user="";
                                        activityController.onTerminate();
                                    }
                                })
                                .setNegativeButton(getResources().getString(R.string.login_page_dialog_cancel), new DialogInterface.OnClickListener() {//添加取消按钮
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // TODO Auto-generated method stub
                                    }
                                }).show();
                    }
                    else if(data.equals("sign_off")) {
                        new AlertDialog.Builder(IndexPage.this).setTitle(getResources().getString(R.string.login_page_dialog_title_1))
                                .setMessage(getResources().getString(R.string.index_page_exit_tips))
                                .setPositiveButton(getResources().getString(R.string.login_page_dialog_ok), new DialogInterface.OnClickListener() {//添加确定按钮
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // TODO Auto-generated method stub
                                        isLogin=false;
                                        login_user="";
                                        File directory=new File("/data/data/" + getApplicationContext().getPackageName() + "/shared_prefs");
                                        if (directory != null && directory.exists() && directory.isDirectory()) {
                                            for (File item : directory.listFiles()) {
                                                item.delete();
                                            }
                                        }
                                        activityController.finishAll();
                                        Intent intent = new Intent(IndexPage.this, LoginPage.class);
                                        startActivity(intent);
                                    }
                                })
                                .setNegativeButton(getResources().getString(R.string.login_page_dialog_cancel), new DialogInterface.OnClickListener() {//添加取消按钮
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // TODO Auto-generated method stub
                                    }
                                }).show();
                    }
                default:
                    break;
            }
        }
    };
    public void initStyle() {
        Typeface YAHEI = Typeface.createFromAsset(getAssets(), "fonts/YAHEI.ttc");
        Typeface YOUYUAN = Typeface.createFromAsset(getAssets(), "fonts/YOUYUAN.TTF");
        index_page_user = (LinearLayout) findViewById(R.id.index_page_user);
        index_page_user_lp = (LinearLayout.LayoutParams) index_page_user.getLayoutParams();
        index_page_title_bar = (LinearLayout) findViewById(R.id.index_page_title_bar);
        index_page_title_bar_lp = (RelativeLayout.LayoutParams) index_page_title_bar.getLayoutParams();
        index_page_user_logo = (ImageView) findViewById(R.id.index_page_user_logo);
        index_page_user_logo_lp = (LinearLayout.LayoutParams) index_page_user_logo.getLayoutParams();
        index_page_user_name = (TextView) findViewById(R.id.index_page_user_name);
        index_page_user_name.setText(super.login_user);
        TextPaint index_page_user_name_paint = index_page_user_name.getPaint();
        index_page_user_name_paint.setTypeface(YAHEI);
        index_page_user_name_lp = (LinearLayout.LayoutParams) index_page_user_name.getLayoutParams();
        index_page_logout = (ImageView) findViewById(R.id.index_page_logout);
        index_page_logout_lp = (LinearLayout.LayoutParams) index_page_logout.getLayoutParams();
        index_page_exit = (ImageView) findViewById(R.id.index_page_exit);
        index_page_exit_lp = (LinearLayout.LayoutParams) index_page_exit.getLayoutParams();
        index_page_practise = (LinearLayout) findViewById(R.id.index_page_practise);
        index_page_practise_lp = (LinearLayout.LayoutParams) index_page_practise.getLayoutParams();
        index_page_practise_img = (ImageView) findViewById(R.id.index_page_practise_img);
        index_page_practise_img_lp = (LinearLayout.LayoutParams) index_page_practise_img.getLayoutParams();
        index_page_practise_title = (TextView) findViewById(R.id.index_page_practise_title);
        TextPaint index_page_practise_title_paint = index_page_practise_title.getPaint();
        index_page_practise_title_paint.setTypeface(YAHEI);
        index_page_practise_title_lp = (LinearLayout.LayoutParams) index_page_practise_title.getLayoutParams();
        index_page_contest = (LinearLayout) findViewById(R.id.index_page_contest);
        index_page_contest_lp = (LinearLayout.LayoutParams) index_page_contest.getLayoutParams();
        index_page_contest_img = (ImageView) findViewById(R.id.index_page_contest_img);
        index_page_contest_img_lp = (LinearLayout.LayoutParams) index_page_contest_img.getLayoutParams();
        index_page_contest_title = (TextView) findViewById(R.id.index_page_contest_title);
        TextPaint index_page_contest_title_paint = index_page_contest_title.getPaint();
        index_page_contest_title_paint.setTypeface(YAHEI);
        index_page_contest_title_lp = (LinearLayout.LayoutParams) index_page_contest_title.getLayoutParams();
        index_page_join = (LinearLayout) findViewById(R.id.index_page_join);
        index_page_join_lp = (LinearLayout.LayoutParams) index_page_join.getLayoutParams();
        index_page_join_img = (ImageView) findViewById(R.id.index_page_join_img);
        index_page_join_img_lp = (LinearLayout.LayoutParams) index_page_join_img.getLayoutParams();
        index_page_join_title = (TextView) findViewById(R.id.index_page_join_title);
        TextPaint index_page_join_title_paint = index_page_join_title.getPaint();
        index_page_join_title_paint.setTypeface(YAHEI);
        index_page_join_title_lp = (LinearLayout.LayoutParams) index_page_join_title.getLayoutParams();
        index_page_dictionary = (LinearLayout) findViewById(R.id.index_page_dictionary);
        index_page_dictionary_lp = (LinearLayout.LayoutParams) index_page_dictionary.getLayoutParams();
        index_page_dictionary_img = (ImageView) findViewById(R.id.index_page_dictionary_img);
        index_page_dictionary_img_lp = (LinearLayout.LayoutParams) index_page_dictionary_img.getLayoutParams();
        index_page_dictionary_title = (TextView) findViewById(R.id.index_page_dictionary_title);
        TextPaint index_page_dictionary_title_paint = index_page_dictionary_title.getPaint();
        index_page_dictionary_title_paint.setTypeface(YAHEI);
        index_page_dictionary_title_lp = (LinearLayout.LayoutParams) index_page_dictionary_title.getLayoutParams();
        index_page_user.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP){
                    index_page_user_logo.setAlpha(1f);
                    index_page_user_name.setAlpha(1f);
                }
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    index_page_user_logo.setAlpha(0.75f);
                    index_page_user_name.setAlpha(0.75f);
                }
                return false;
            }
        });
        index_page_logout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP){
                    index_page_logout.setAlpha(1f);
                }
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    index_page_logout.setAlpha(0.75f);
                }
                return false;
            }
        });
        index_page_exit.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP){
                    index_page_exit.setAlpha(1f);
                }
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    index_page_exit.setAlpha(0.75f);
                }
                return false;
            }
        });
        index_page_practise.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP){
                    index_page_practise.setBackground(getResources().getDrawable(R.drawable.index_page_button));
                    index_page_practise_img.setImageResource(R.drawable.practise);
                    index_page_practise_title.setTextColor(Color.parseColor("#199000"));
                }
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    index_page_practise.setBackground(getResources().getDrawable(R.drawable.index_page_button_press));
                    index_page_practise_img.setImageResource(R.drawable.practise_press);
                    index_page_practise_title.setTextColor(Color.parseColor("#EAEAEA"));
                }
                return false;
            }
        });
        index_page_contest.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP){
                    index_page_contest.setBackground(getResources().getDrawable(R.drawable.index_page_button));
                    index_page_contest_img.setImageResource(R.drawable.contest);
                    index_page_contest_title.setTextColor(Color.parseColor("#199000"));
                }
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    index_page_contest.setBackground(getResources().getDrawable(R.drawable.index_page_button_press));
                    index_page_contest_img.setImageResource(R.drawable.contest_press);
                    index_page_contest_title.setTextColor(Color.parseColor("#EAEAEA"));
                }
                return false;
            }
        });
        index_page_join.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP){
                    index_page_join.setBackground(getResources().getDrawable(R.drawable.index_page_button));
                    index_page_join_img.setImageResource(R.drawable.join);
                    index_page_join_title.setTextColor(Color.parseColor("#199000"));
                }
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    index_page_join.setBackground(getResources().getDrawable(R.drawable.index_page_button_press));
                    index_page_join_img.setImageResource(R.drawable.join_press);
                    index_page_join_title.setTextColor(Color.parseColor("#EAEAEA"));
                }
                return false;
            }
        });
        index_page_dictionary.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP){
                    index_page_dictionary.setBackground(getResources().getDrawable(R.drawable.index_page_button));
                    index_page_dictionary_img.setImageResource(R.drawable.dictionary);
                    index_page_dictionary_title.setTextColor(Color.parseColor("#199000"));
                }
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    index_page_dictionary.setBackground(getResources().getDrawable(R.drawable.index_page_button_press));
                    index_page_dictionary_img.setImageResource(R.drawable.dictionary_press);
                    index_page_dictionary_title.setTextColor(Color.parseColor("#EAEAEA"));
                }
                return false;
            }
        });
        index_page_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IndexPageTitleBarHandler.sendEmptyMessage(0);
                Message msgerror =new Message();
                msgerror.obj = "sign_off";
                IndexPageTitleBarHandler.sendMessage(msgerror);
            }
        });
        index_page_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IndexPageTitleBarHandler.sendEmptyMessage(0);
                Message msgerror =new Message();
                msgerror.obj = "exit";
                IndexPageTitleBarHandler.sendMessage(msgerror);
            }
        });
        index_page_practise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IndexPage.this, ResourcesPicker.class);
                intent.putExtra("videoMode", VideoMode.ADD);
                startActivity(intent);
            }
        });
        index_page_contest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IndexPage.this, ParticipatedContestList.class);
                startActivity(intent);
            }
        });
        index_page_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IndexPage.this, ParticipateContestList.class);
                startActivity(intent);
            }
        });
        index_page_dictionary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IndexPage.this, WordNoteList.class);
                startActivity(intent);
            }
        });
    }
}
