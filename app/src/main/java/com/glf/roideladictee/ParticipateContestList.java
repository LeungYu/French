package com.glf.roideladictee;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.percent.PercentRelativeLayout;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.glf.roideladictee.MyAdapter.ParticipateCotestAdapter;
import com.glf.roideladictee.MyAdapter.WordNoteAdapter;
import com.glf.roideladictee.MyLitePal.WordNote;
import com.glf.roideladictee.MyView.LoadingPHP;
import com.glf.roideladictee.fr_app.fr_contest;
import com.glf.roideladictee.tools.BaseActivity;
import com.glf.roideladictee.tools.LocaleUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.glf.roideladictee.tools.LocaleUtils.LOCALE_CHINESE;

/**
 * Created by 11951 on 2018-05-04.
 * 参加比赛界面
 */

public class ParticipateContestList extends BaseActivity {

    private ListView listView;
    private Typeface YAHEI;
    private ParticipateCotestAdapter participateCotestAdapter;
    private Button participate_button,back_button;
    private View button_action;
    private TextView selected_title;
    private PercentRelativeLayout participate_contest_layout;
    private LoadingPHP loadingPHP;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private List<fr_contest> fr_contestList = new ArrayList<>();
    private fr_contest select_fr_contest;
    private Handler handler =new Handler() {
        public void handleMessage(Message msg) {
            String jsonData;
            JSONArray jsonArray = null;
            participate_contest_layout.removeView(loadingPHP);
            switch (msg.what) {
                case 0X12:
                    jsonData = (String) msg.obj;
                    try {
                        jsonArray = new JSONArray(jsonData);
                        fr_contestList.clear();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            fr_contest frContest = new fr_contest();
                            frContest.setContest_id(jsonObject.getString("contest_id"));
                            try {
                                frContest.setContest_datetime(sdf.parse(jsonObject.getString("contest_datetime")));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            frContest.setMovie_name(jsonObject.getString("movie_name"));
                            frContest.setMov_file(jsonObject.getString("mov_file"));
                            frContest.setMov_srt(jsonObject.getString("mov_srt"));
                            fr_contestList.add(frContest);
                        }

                    }  catch (JSONException e) {
                        e.printStackTrace();
                    }
                    fr_contest[] frcontests = fr_contestList.toArray(new fr_contest[0]);
                    participateCotestAdapter = new ParticipateCotestAdapter(ParticipateContestList.this, frcontests,YAHEI);
                    listView.setAdapter(participateCotestAdapter);
                    break;
                case 0X14:
                    jsonData = (String) msg.obj;
                    if(jsonData.equals("1")){
                        fr_contestList.remove(select_fr_contest);
                    }else{
                        Toast.makeText(ParticipateContestList.this,"ERROR",Toast.LENGTH_LONG).show();
                    }
                    button_action = null;
                    select_fr_contest = null;
                    cleanBottonBar();
                    setListView();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (language.equals("ZH")) {
            LocaleUtils.updateLocale(ParticipateContestList.this, LOCALE_CHINESE);
        }else{
            LocaleUtils.updateLocale(ParticipateContestList.this, LocaleUtils.LOCALE_FRENCH);
        }
        setContentView(R.layout.participate_contest_list);
//        test();//测试用
        Init();// 初始化
    }

    private void test() {
        Date date;
        try {
            date = sdf.parse("2018-05-05 11:00:00");
        } catch (ParseException e) {
            Log.e("ljong","sdf ERROR");
            return;
        }
        fr_contest frContest = new fr_contest("1",date,"放牛班的春天","test.mp4","test.ass");
        fr_contest frContest2 = new fr_contest("2",date,"张振丰漂流记","test.mp4","test.ass");
        fr_contestList.add(frContest);
        fr_contestList.add(frContest2);
    }

    // 初始化
    protected void Init(){
        getExtra();//获取参数
        screenInit();//屏幕初始化
        textGroupInit();//字体相关初始化
        buttonGroupInit();//按钮相关初始化
        listViewInit(); // ListView初始化
    }


    private void getExtra() {

    }

    private void screenInit() {
        participate_contest_layout = (PercentRelativeLayout)findViewById(R.id.participate_contest_layout);
        loadingPHP = new LoadingPHP(ParticipateContestList.this);
        loadingPHP.setAlpha(0.75f);
    }

    private void textGroupInit() {
        textTypefaceInit();//字体初始化
        textSet();//文字初始化
    }

    private void textTypefaceInit() {
        YAHEI = Typeface.createFromAsset(getAssets(), "fonts/YAHEI.ttc");
        int textViewIds[] = {R.id.title,R.id.selected_title,R.id.participate_button,R.id.back_button};
        for(int textViewId:textViewIds){
            ((TextView)findViewById(textViewId)).getPaint().setTypeface(YAHEI);
        }
    }

    private void textSet() {
        selected_title = (TextView)findViewById(R.id.selected_title);
    }

    private void buttonGroupInit() {
        participate_button = (Button)findViewById(R.id.participate_button);
        back_button = (Button)findViewById(R.id.back_button);
        participate_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(button_action == null)return;
                participate_php();//参加比赛请求php
            }
        });
        participate_button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == 0) {
                    participate_button.setAlpha(0.75f);
                }
                if (event.getAction() == 1 || event.getAction() == 3) {
                    participate_button.setAlpha(1f);
                }
                return false;
            }
        });
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        back_button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == 0) {
                    back_button.setAlpha(0.75f);
                }
                if (event.getAction() == 1 || event.getAction() == 3) {
                    back_button.setAlpha(1f);
                }
                return false;
            }
        });
    }


    private void listViewInit() {
        listView = (ListView) findViewById(R.id.listView);
        setListView();
        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(
                            AdapterView<?> parent,
                            View view,
                            int position,
                            long id) {
                        select_fr_contest = participateCotestAdapter.getItem(position);
                        if(button_action != null){
                            button_action.setVisibility(View.INVISIBLE);
                        };
                        button_action =  view.findViewById(R.id.imageButton_action);
                        button_action.setVisibility(View.VISIBLE);
                        selected_title.setText(getResources().getString(R.string.resources_picket_selected_title));
                    }
                });
    }

    private void setListView(){
       participate_contest_layout.addView(loadingPHP);
       new Thread(new Runnable() {
           @Override
           public void run() {
               final OkHttpClient okHttpClient = new OkHttpClient();
               FormBody body = new FormBody.Builder()
                       .add("date_time", sdf.format(new Date(System.currentTimeMillis())))
                       .add("phone_number", login_user)
                       .build();
               final Request request = new Request.Builder()
                       .url("http://fr.xsinweb.com/fr/service/Get_Contest.php")
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

    private void cleanBottonBar() {
        selected_title.setText(getResources().getString(R.string.resources_picket_noselect_title));
    }

    private void participate_php() {
        if(select_fr_contest == null)return;
        participate_contest_layout.addView(loadingPHP);
        new Thread(new Runnable() {
            @Override
            public void run() {
                final OkHttpClient okHttpClient = new OkHttpClient();
                FormBody body = new FormBody.Builder()
                        .add("contest_id", select_fr_contest.getContest_id())
                        .add("phone_number", login_user)
                        .build();
                final Request request = new Request.Builder()
                        .url("http://fr.xsinweb.com/fr/service/Join_Contest.php")
                        .post(body)
                        .build();
                try {
                    Response response = null;
                    response = okHttpClient.newCall(request).execute();
                    String getInfo = response.body().string();
                    if (response.code() == 200) {
                        Message message = new Message();
                        message.what = 0X14;
                        message.obj = getInfo;
                        handler.sendMessage(message);
                    }
                }catch (Exception e){

                }
            }
        }).start();
    }
}
