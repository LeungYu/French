package com.glf.roideladictee.TranslateWindow;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.glf.roideladictee.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Created by lenovo on 2018/4/25.
 */

public class TranslatorFrame extends AppCompatActivity {
    protected String target="";
    LinearLayout translate_frame_border;
    LinearLayout translate_frame_title_border;
    TextView translate_frame_title;
    LinearLayout translate_frame_exit_border;
    ImageView translate_frame_exit;
    LinearLayout translate_frame_blank;
    ScrollView translate_frame_main_border;
    TextView translate_frame_main_fr;
    TextView translate_frame_main_zh;
    TextView translate_frame_main_example_title;
    TextView translate_frame_main_example;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.translate_frame);
        target=getIntent().getStringExtra("target");
        initStyle();
        initTarget();
        initTranslation();
        initExample();
    }
    public void initStyle(){
        translate_frame_border=(LinearLayout)findViewById(R.id.translate_frame_border);
        translate_frame_title_border=(LinearLayout)findViewById(R.id.translate_frame_title_border);
        translate_frame_title=(TextView)findViewById(R.id.translate_frame_title);
        translate_frame_exit_border=(LinearLayout)findViewById(R.id.translate_frame_exit_border);
        translate_frame_exit=(ImageView)findViewById(R.id.translate_frame_exit);
        translate_frame_blank=(LinearLayout)findViewById(R.id.translate_frame_blank);
        translate_frame_main_border=(ScrollView)findViewById(R.id.translate_frame_main_border);
        translate_frame_main_fr=(TextView)findViewById(R.id.translate_frame_main_fr);
        translate_frame_main_zh=(TextView)findViewById(R.id.translate_frame_main_zh);
        translate_frame_main_example_title=(TextView)findViewById(R.id.translate_frame_main_example_title);
        translate_frame_main_example=(TextView)findViewById(R.id.translate_frame_main_example);
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
                    else if(data.equals("中文翻译获取失败")){
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
                    else if(data.equals("例句获取失败")){
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
                    doc = Jsoup.connect("http://www.frdic.com/mdicts/fr/"+target).get();
                } catch (IOException e) {
                    updateCnTranslationHandler.sendEmptyMessage(0);
                    Message msg = new Message();
                    msg.obj = "中文翻译获取失败";
                    updateCnTranslationHandler.sendMessage(msg);
                    return;
                }
                String setter="";
                String wordType = doc.getElementById("FCChild").getElementsByClass("cara").first().text();
                setter+=wordType+"  ";
                Elements cnTranslations=doc.getElementById("FCChild").getElementsByClass("exp");
                for(Element e:cnTranslations){
                    StringBuffer temp=new StringBuffer(e.text());
                    for(int i=0;i<temp.length();i++){
                        if(temp.charAt(i)==' '){
                            temp.setCharAt(i,'；');
                        }
                        if(temp.charAt(i)=='，'){
                            temp.setCharAt(i,'\0');
                        }
                    }
                    setter+=temp;
                }
                updateCnTranslationHandler.sendEmptyMessage(0);
                Message msg = new Message();
                msg.obj = setter;
                updateCnTranslationHandler.sendMessage(msg);
            }
        }.start();
    }
    public String stringInitTranslation(){
        final String[] setter = {""};
        new Thread(){
            public void run(){
                Document doc=null;
                try {
                    doc = Jsoup.connect("http://www.frdic.com/mdicts/fr/"+target).get();
                } catch (IOException e) {
                    Message msg = new Message();
                    setter[0] = "中文翻译获取失败";
                    return;
                }
                String wordType = doc.getElementById("FCChild").getElementsByClass("cara").first().text();
                setter[0] +=wordType+"  ";
                Elements cnTranslations=doc.getElementById("FCChild").getElementsByClass("exp");
                for(Element e:cnTranslations){
                    StringBuffer temp=new StringBuffer(e.text());
                    for(int i=0;i<temp.length();i++){
                        if(temp.charAt(i)==' '){
                            temp.setCharAt(i,'；');
                        }
                        if(temp.charAt(i)=='，'){
                            temp.setCharAt(i,'\0');
                        }
                    }
                    setter[0] +=temp;
                }
            }
        }.start();
        return setter[0];
    }
    public void  initExample(){
        new Thread(){
            public void run(){
                Document doc=null;
                try {
                    doc = Jsoup.connect("http://www.frdic.com/mdicts/fr/"+target).get();
                } catch (IOException e) {
                    updateExampleHandler.sendEmptyMessage(0);
                    Message msg = new Message();
                    msg.obj = "例句获取失败";
                    updateExampleHandler.sendMessage(msg);
                    return;
                }
                Elements Example=doc.getElementById("lj_div").getElementsByTag("li");
                String setter="";
                int i=0;
                int size=Example.size();
                for(Element e:Example){
                    setter+=Integer.toString(++i)+". "+e.getElementsByClass("line").first().text()+"\n"+"    "+e.getElementsByClass("exp").first().text()+(i==size?"":"\n\n");
                }
                updateExampleHandler.sendEmptyMessage(0);
                Message msg = new Message();
                msg.obj = setter;
                updateExampleHandler.sendMessage(msg);
            }
        }.start();
    }
}
