package com.glf.roideladictee;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.percent.PercentRelativeLayout;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.glf.roideladictee.MyAdapter.ParticipateCotestAdapter;
import com.glf.roideladictee.MyView.LoadingPHP;
import com.glf.roideladictee.fr_app.fr_contest;
import com.glf.roideladictee.tools.BaseActivity;
import com.glf.roideladictee.tools.LocaleUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.glf.roideladictee.tools.LocaleUtils.LOCALE_CHINESE;

/**
 * Created by 11951 on 2018-05-02.
 * 测试结果界面
 */

public class TestResult extends BaseActivity {

    private PercentRelativeLayout percentRelativeLayout;
    private Button ok;
    private TextView test_result_score,test_sum_word,test_sum_word_right,test_sum_word_wrong;

    private LoadingPHP loadingPHP;
    private int percentx= 1,percenty = 1;//遵循这个屏幕缩放比例
    private int sum,right;
    private fr_contest frcontest;
    private String score;
    private Handler handler =new Handler() {
        public void handleMessage(Message msg) {
            String jsonData;
            JSONArray jsonArray = null;
            percentRelativeLayout.removeView(loadingPHP);
            switch (msg.what) {
                case 0X12:
                    jsonData = (String) msg.obj;
                    if(!jsonData.equals("1")) Toast.makeText(TestResult.this,"ERROR",Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (language.equals("ZH")) {
            LocaleUtils.updateLocale(TestResult.this, LOCALE_CHINESE);
        }else{
            LocaleUtils.updateLocale(TestResult.this, LocaleUtils.LOCALE_FRENCH);
        }
        setContentView(R.layout.test_result);
        Init();//初始化
    }

    //初始化
    protected void Init(){
        getExtra();
        screenPercentInit();
        buttonInit();//按钮初始化
        textGroupInit();//文字相关初始化
        finishContest_php();
    }

    private void getExtra() {
        frcontest = (fr_contest) getIntent().getSerializableExtra("fr_contest");
    }

    protected void screenPercentInit(){
        percentRelativeLayout = (PercentRelativeLayout)findViewById(R.id.test_result_layout);
        loadingPHP = new LoadingPHP(TestResult.this);
        loadingPHP.setAlpha(0.75f);
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
        if(sum == 0)score = "100";
        else score = ""+Math.round(100 * right/(double)sum);
        test_result_score.setText(score);
        test_sum_word.setText(""+sum);
        test_sum_word_right.setText(""+right);
        test_sum_word_wrong.setText(""+(sum - right));
    }

    protected void finishContest_php(){
        percentRelativeLayout.addView(loadingPHP);
        Log.e("ljong",login_user+" "+frcontest.getContest_id()+" "+score);
        new Thread(new Runnable() {
            @Override
            public void run() {
                final OkHttpClient okHttpClient = new OkHttpClient();
                FormBody body = new FormBody.Builder()
                        .add("phone_number", login_user)
                        .add("contest_id",frcontest.getContest_id())
                        .add("contest_mark",score)
                        .build();
                final Request request = new Request.Builder()
                        .url("http://fr.xsinweb.com/fr/service/Finish_Contest.php")
                        .post(body)
                        .build();
                try {
                    Response response = null;
                    response = okHttpClient.newCall(request).execute();
                    String getInfo = response.body().string();
                    if (response.code() == 200) {
                        Message message = new Message();
                        message.what = 0X12;
                        message.obj = getInfo;
                        handler.sendMessage(message);
                    }
                }catch (Exception e){

                }
            }
        }).start();
    }
}
