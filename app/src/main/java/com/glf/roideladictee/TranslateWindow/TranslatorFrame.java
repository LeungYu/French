package com.glf.roideladictee.TranslateWindow;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextPaint;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.glf.roideladictee.R;
import com.glf.roideladictee.tools.MD5;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URLEncoder;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by lenovo on 2018/4/25.
 */

public class TranslatorFrame extends AppCompatActivity {
    protected String target="";
    LinearLayout translate_frame_border;
    FrameLayout.LayoutParams translate_frame_border_lp;
    LinearLayout translate_frame_title_border;
    RelativeLayout.LayoutParams translate_frame_title_border_lp;
    TextView translate_frame_title;
    LinearLayout.LayoutParams translate_frame_title_lp;
    LinearLayout translate_frame_exit_border;
    RelativeLayout.LayoutParams translate_frame_exit_border_lp;
    ImageView translate_frame_exit;
    LinearLayout.LayoutParams translate_frame_exit_lp;
    LinearLayout translate_frame_blank;
    RelativeLayout.LayoutParams translate_frame_blank_lp;
    ScrollView translate_frame_main_border;
    RelativeLayout.LayoutParams translate_frame_main_border_lp;
    TextView translate_frame_main_fr;
    LinearLayout.LayoutParams translate_frame_main_fr_lp;
    TextView translate_frame_main_zh;
    LinearLayout.LayoutParams translate_frame_main_zh_lp;
    TextView translate_frame_main_example_title;
    LinearLayout.LayoutParams translate_frame_main_example_title_lp;
    TextView translate_frame_main_example;
    LinearLayout.LayoutParams translate_frame_main_example_lp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.translate_frame);
        target=getIntent().getStringExtra("target");
        System.out.println("单词："+target);
        initStyle();
        initTarget();
        initTranslation();
        initExample();
    }
    public void initStyle(){
        Typeface YAHEI =Typeface.createFromAsset(getAssets(), "fonts/YAHEI.ttc");
        Typeface SEGOEUI =Typeface.createFromAsset(getAssets(), "fonts/SEGOEUI.ttf");
        translate_frame_border=(LinearLayout)findViewById(R.id.translate_frame_border);
        translate_frame_border_lp = (FrameLayout.LayoutParams) translate_frame_border.getLayoutParams();
        translate_frame_title_border=(LinearLayout)findViewById(R.id.translate_frame_title_border);
        translate_frame_title_border_lp = (RelativeLayout.LayoutParams) translate_frame_title_border.getLayoutParams();
        translate_frame_title=(TextView)findViewById(R.id.translate_frame_title);
        TextPaint translate_frame_title_paint = translate_frame_title.getPaint();
        translate_frame_title_paint.setTypeface(YAHEI);
        translate_frame_title_lp = (LinearLayout.LayoutParams) translate_frame_title.getLayoutParams();
        translate_frame_exit_border=(LinearLayout)findViewById(R.id.translate_frame_exit_border);
        translate_frame_exit_border_lp = (RelativeLayout.LayoutParams) translate_frame_exit_border.getLayoutParams();
        translate_frame_exit=(ImageView)findViewById(R.id.translate_frame_exit);
        translate_frame_exit_lp = (LinearLayout.LayoutParams) translate_frame_exit.getLayoutParams();
        translate_frame_blank=(LinearLayout)findViewById(R.id.translate_frame_blank);
        translate_frame_blank_lp = (RelativeLayout.LayoutParams) translate_frame_blank.getLayoutParams();
        translate_frame_main_border=(ScrollView)findViewById(R.id.translate_frame_main_border);
        translate_frame_main_border_lp = (RelativeLayout.LayoutParams) translate_frame_main_border.getLayoutParams();
        translate_frame_main_fr=(TextView)findViewById(R.id.translate_frame_main_fr);
        TextPaint translate_frame_main_fr_paint = translate_frame_main_fr.getPaint();
        translate_frame_main_fr_paint.setTypeface(SEGOEUI);
        translate_frame_main_fr_lp = (LinearLayout.LayoutParams) translate_frame_main_fr.getLayoutParams();
        translate_frame_main_zh=(TextView)findViewById(R.id.translate_frame_main_zh);
        TextPaint translate_frame_main_zh_paint = translate_frame_main_zh.getPaint();
        translate_frame_main_zh_paint.setTypeface(YAHEI);
        translate_frame_main_zh_lp = (LinearLayout.LayoutParams) translate_frame_main_zh.getLayoutParams();
        translate_frame_main_example_title=(TextView)findViewById(R.id.translate_frame_main_example_title);
        TextPaint translate_frame_main_example_title_paint = translate_frame_main_example_title.getPaint();
        translate_frame_main_example_title_paint.setTypeface(YAHEI);
        translate_frame_main_example_title_lp = (LinearLayout.LayoutParams) translate_frame_main_example_title.getLayoutParams();
        translate_frame_main_example=(TextView)findViewById(R.id.translate_frame_main_example);
        TextPaint translate_frame_main_example_paint = translate_frame_main_example.getPaint();
        translate_frame_main_example_paint.setTypeface(YAHEI);
        translate_frame_main_example_lp = (LinearLayout.LayoutParams) translate_frame_main_example.getLayoutParams();
        //绑定退出按钮
        translate_frame_exit.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP){
                    translate_frame_exit.setAlpha(1f);
                }
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    translate_frame_exit.setAlpha(0.75f);
                }
                return false;
            }
        });
        translate_frame_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TranslatorFrame.this.finish();
            }
        });
    }
    final Handler updateCnTranslationHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    String data = (String)msg.obj;
                    if(data==null)
                        return;
                    else if(data.equals("ERROR")){
                        translate_frame_main_zh.setTextColor(Color.parseColor("#FFFF0000"));
                        translate_frame_main_zh.setText(data);
                    }
                    else{
                        translate_frame_main_zh.setTextColor(Color.parseColor("#99000000"));
                        translate_frame_main_zh.setText(data);
                    }
                    break;
                default:
                    break;
            }
        }
    };
    final Handler updateExampleHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    String data = (String)msg.obj;
                    if(data==null)
                        return;
                    else if(data.equals("ERROR")){
                        translate_frame_main_example.setTextColor(Color.parseColor("#FFFF0000"));
                        translate_frame_main_example.setText(data);
                    }
                    else{
                        translate_frame_main_example.setTextColor(Color.parseColor("#99000000"));
                        translate_frame_main_example.setText(data);
                    }
                    break;
                default:
                    break;
            }
        }
    };
    public void initTarget(){
        translate_frame_main_fr.setText(target);
    }
    public void initTranslation(){
        new Thread(){
            public void run(){
                Document doc=null;
                try {
                    doc = Jsoup.connect("http://www.frdic.com/dicts/fr/"+target).get();
                } catch (IOException e) {
                    updateCnTranslationHandler.sendEmptyMessage(0);
                    Message msg = new Message();
                    msg.obj = "ERROR";
                    updateCnTranslationHandler.sendMessage(msg);
                    return;
                }
                String setter="";
                try {
                    String wordType = doc.getElementById("ExpFCChild").getElementsByClass("cara").first().text();
                    setter += "【"+wordType+"】 ";
                }
                catch (Exception e){
                    setter="【NULL】 ";
                }
                try {
                    String wordMeaning=doc.getElementById("ExpFCChild").getElementsByClass("exp").first().text();
                    wordMeaning=wordMeaning.substring(wordMeaning.indexOf(".")+1,wordMeaning.indexOf("："));
                    setter += wordMeaning;
                }
                catch (Exception e){
                    final OkHttpClient okHttpClient = new OkHttpClient();
                    try {
                        String url = "http://api.fanyi.baidu.com/api/trans/vip/translate?q=" + URLEncoder.encode(target, "utf-8") + "&from=fra&to=zh&appid=20180424000149913&salt=1&sign=" + MD5.md5("20180424000149913" + target + "1BqLy8Ld4b6hH61xdw4NM");
                        final Request request_Login_Check = new Request.Builder()
                                .url(url)
                                .get()
                                .build();
                        Response response = null;
                        response = okHttpClient.newCall(request_Login_Check).execute();
                        String info = response.body().string();
                        if (response.code() == 200) {
                            System.out.println("完整内容："+info);
                            String tar=info.substring(info.indexOf("\"dst\":\"")+7,info.indexOf("\"}]}"));
                            System.out.println("分片内容："+tar);
                            setter=MD5.decodeUnicode(tar);
                        } else {
                            setter="NULL";
                            return;
                        }
                    }
                    catch (Exception e1){
                        setter="NULL";
                        return;
                    }
                }
                updateCnTranslationHandler.sendEmptyMessage(0);
                Message msg = new Message();
                msg.obj = setter;
                updateCnTranslationHandler.sendMessage(msg);
            }
        }.start();
    }

    public void  initExample(){
        new Thread(){
            public void run(){
                Document doc=null;
                try {
                    doc = Jsoup.connect("http://www.frdic.com/dicts/fr/"+target).get();
                } catch (IOException e) {
                    updateExampleHandler.sendEmptyMessage(0);
                    Message msg = new Message();
                    msg.obj = "ERROR";
                    updateExampleHandler.sendMessage(msg);
                    return;
                }
                String setter = "";
                try {
                    Elements Example = doc.getElementById("ExpLJChild").getElementsByClass("lj_item");
                    int i = 0;
                    int size = Example.size();
                    for (Element e : Example) {
                        setter += Integer.toString(++i)+". "+e.getElementsByClass("line").first().text() + "\n" + "    " + e.getElementsByClass("exp").first().text() + (i == size ? "" : "\n\n");
                    }
                }
                catch (Exception e){
                    setter="ERROR";
                }
                updateExampleHandler.sendEmptyMessage(0);
                Message msg = new Message();
                msg.obj = setter;
                updateExampleHandler.sendMessage(msg);
            }
        }.start();
    }
}
