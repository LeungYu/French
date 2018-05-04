package com.glf.roideladictee;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.glf.roideladictee.tools.BaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 11951 on 2018-05-02.
 * 测试结果界面
 */

public class TestResult extends BaseActivity {

    private Button ok;
    private TextView test_result_score,test_sum_word,test_sum_word_right,test_sum_word_wrong;

    private int percentx= 1,percenty = 1;//遵循这个屏幕缩放比例
    private int sum,right;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_result);
        Init();//初始化
    }

    //初始化
    protected void Init(){
        screenPercentInit();//获取屏幕的宽高，生成与原型的相差比例
        buttonInit();//按钮初始化
        textGroupInit();//文字相关初始化
    }

    //获取屏幕的宽高，生成与原型的相差比例
    protected void screenPercentInit(){
    }

    //按钮初始化
    protected void buttonInit(){
        ok = (Button)findViewById(R.id.ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ok.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == 0) {
                    ok.setAlpha(0.75f);
                }
                if (event.getAction() == 1 || event.getAction() == 3) {
                    ok.setAlpha(1f);
                }
                return false;
            }
        });
    }

    //文字相关初始化
    protected void textGroupInit(){
        textTypefaceInit();//字体初始化
        textSet();//文字内容设置
    }

    //字体初始化
    protected void textTypefaceInit(){
        Typeface YAHEI = Typeface.createFromAsset(getAssets(), "fonts/YAHEI.ttc");
        int textViewIds[] ={R.id.title, R.id.ok,
                             R.id.test_result_score_notes,R.id.test_result_score,R.id.test_result_score_unit,
                             R.id.test_sum_word_notes,R.id.test_sum_word,R.id.test_sum_word_unit,
                             R.id.test_sum_word_right_notes,R.id.test_sum_word_right,R.id.test_sum_word_right_unit,
                             R.id.test_sum_word_wrong_note,R.id.test_sum_word_wrong,R.id.test_sum_word_wrong_unit};
        for(int textViewId:textViewIds){
            ((TextView)findViewById(textViewId)).getPaint().setTypeface(YAHEI);
        }
    }

    //文字内容设置
    protected void textSet(){
        test_result_score = (TextView)findViewById(R.id.test_result_score);
        test_sum_word = (TextView)findViewById(R.id.test_sum_word);
        test_sum_word_right = (TextView)findViewById(R.id.test_sum_word_right);
        test_sum_word_wrong = (TextView)findViewById(R.id.test_sum_word_wrong);
        sum = getIntent().getExtras().getInt("sum");
        right = getIntent().getExtras().getInt("right");
        if(sum == 0)test_result_score.setText("100");
        else test_result_score.setText(""+Math.round(right/(double)sum));
        test_sum_word.setText(""+sum);
        test_sum_word_right.setText(""+right);
        test_sum_word_wrong.setText(""+(sum - right));
    }
}
