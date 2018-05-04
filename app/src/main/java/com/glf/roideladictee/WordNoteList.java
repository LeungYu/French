package com.glf.roideladictee;

import android.content.Intent;
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

import com.glf.roideladictee.MyAdapter.FileAdapter;
import com.glf.roideladictee.MyAdapter.ParticipateCotestAdapter;
import com.glf.roideladictee.MyAdapter.WordNoteAdapter;
import com.glf.roideladictee.MyLitePal.CaptionAss;
import com.glf.roideladictee.MyLitePal.WordNote;
import com.glf.roideladictee.MyView.LoadingPHP;
import com.glf.roideladictee.TranslateWindow.TranslatorFrame;
import com.glf.roideladictee.fr_app.fr_contest;
import com.glf.roideladictee.tools.BaseActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by 11951 on 2018-05-04.
 * 生词本界面
 */

public class WordNoteList extends BaseActivity {

    private ListView listView;
    private PercentRelativeLayout word_note_layout;
    private Typeface YAHEI;
    private WordNoteAdapter wordNoteAdapter;
    private Button delete_button,button;
    private LoadingPHP loadingPHP;
    private TextView selected_title,selected,word_num;
    private List<String> delete_words = new ArrayList<>();
    private Handler handler =new Handler() {
        public void handleMessage(Message msg) {
            String jsonData;
            JSONArray jsonArray = null;
            word_note_layout.removeView(loadingPHP);
            switch (msg.what) {
                case 0X12:
                    jsonData = (String) msg.obj;
                    try {
                        jsonArray = new JSONArray(jsonData);
//                        fr_contestList.clear();
//                        for (int i = 0; i < jsonArray.length(); i++) {
//                            JSONObject jsonObject = jsonArray.getJSONObject(i);
//                            fr_contest frContest = new fr_contest();
//                            frContest.setContest_id(jsonObject.getString("contest_id"));
//                            try {
//                                frContest.setContest_datetime(sdf.parse(jsonObject.getString("contest_datetime")));
//                            } catch (ParseException e) {
//                                e.printStackTrace();
//                            }
//                            frContest.setMovie_name(jsonObject.getString("movie_name"));
//                            frContest.setMov_file(jsonObject.getString("mov_file"));
//                            frContest.setMov_srt(jsonObject.getString("mov_srt"));
//                            fr_contestList.add(frContest);
//                        }

                    }  catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("ljong","ERROR");
                    }
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_note_list);
        Init();// 初始化
    }

    // 初始化
    protected void Init(){
        screenInit();
        textGroupInit();//字体相关初始化
        buttonGroupInit();//按钮相关初始化
        DBInit();//数据库初始化
        listViewInit(); // ListView初始化
    }

    private void screenInit() {
        word_note_layout =(PercentRelativeLayout)findViewById(R.id.word_note_layout);
        loadingPHP = new LoadingPHP(WordNoteList.this);
        loadingPHP.setAlpha(0.75f);
    }

    //字体相关初始化
    protected void textGroupInit(){
        textTypefaceInit();//字体初始化
        textSet();//文字初始化
    }

    //字体初始化
    protected void textTypefaceInit() {
        YAHEI = Typeface.createFromAsset(getAssets(), "fonts/YAHEI.ttc");
        int textViewIds[] = {R.id.title,R.id.selected,R.id.selected_title,R.id.word_num,R.id.button};
        for(int textViewId:textViewIds){
            ((TextView)findViewById(textViewId)).getPaint().setTypeface(YAHEI);
        }
    }

    //文字初始化
    protected void textSet(){
        selected_title = (TextView)findViewById(R.id.selected_title);
        selected = (TextView)findViewById(R.id.selected);
        word_num = (TextView)findViewById(R.id.word_num);
    }

    //按钮相关初始化
    protected void buttonGroupInit(){
        delete_button = (Button)findViewById(R.id.delete_button);
        button = (Button)findViewById(R.id.button);
        delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(findViewById(R.id.botton_bar).getVisibility()==View.VISIBLE){
                    findViewById(R.id.botton_bar).setVisibility(View.INVISIBLE);
                    findViewById(R.id.divide2).setVisibility(View.INVISIBLE);
                    setListView();
                    return;
                }
                showAndCleanBottonBar();//显示和清空bottonBar
            }
        });
        delete_button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == 0) {
                    delete_button.setAlpha(0.75f);
                }
                if (event.getAction() == 1 || event.getAction() == 3) {
                    delete_button.setAlpha(1f);
                }
                return false;
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(delete_words.size() == 0){
                    findViewById(R.id.botton_bar).setVisibility(View.INVISIBLE);
                    findViewById(R.id.divide2).setVisibility(View.INVISIBLE);
                    return;
                }
                for(String delete_word:delete_words){
                    DataSupport.deleteAll(WordNote.class,"word = ?",delete_word);
                    delete_word_php(delete_word);
                }
                delete_words.clear();
                setListView();
                showAndCleanBottonBar();//显示和清空bottonBar
            }
        });
        button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == 0) {
                    button.setAlpha(0.75f);
                }
                if (event.getAction() == 1 || event.getAction() == 3) {
                    button.setAlpha(1f);
                }
                return false;
            }
        });
    }

    //数据库初始化
    protected void DBInit(){
        Connector.getDatabase();
    }

    // ListView初始化
    protected void listViewInit(){
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
                        WordNote wordNote = wordNoteAdapter.getItem(position);
                        if(findViewById(R.id.botton_bar).getVisibility()!=View.VISIBLE){
                            Intent intent = new Intent(WordNoteList.this, TranslatorFrame.class);
                            intent.putExtra("target", wordNote.getWord());
                            startActivity(intent);
                            return;
                        }
                        View imageButton_action = view.findViewById(R.id.imageButton_action);
                        if(imageButton_action.getVisibility() == View.VISIBLE){
                            imageButton_action.setVisibility(View.INVISIBLE);
                            delete_words.remove(wordNote.getWord());
                            if(delete_words.size()==0){
                                selected_title.setText(getResources().getString(R.string.resources_picket_noselect_title));
                                selected.setText("");
                                word_num.setVisibility(View.INVISIBLE);
                                button.setText(getResources().getString(R.string.word_note_list_cancel_delete));
                            }else{
                                selected.setText(""+delete_words.size());
                            }
                        }else{
                            imageButton_action.setVisibility(View.VISIBLE);
                            delete_words.add(wordNote.getWord());
                            selected_title.setText(getResources().getString(R.string.resources_picket_selected_title));
                            selected.setText(""+delete_words.size());
                            word_num.setVisibility(View.VISIBLE);
                            button.setText(getResources().getString(R.string.word_note_list_delete));
                        }
                    }
                });
    }

    //显示和清空bottonBar
    protected void showAndCleanBottonBar(){
        findViewById(R.id.botton_bar).setVisibility(View.VISIBLE);
        findViewById(R.id.divide2).setVisibility(View.VISIBLE);
        selected_title.setText(getResources().getString(R.string.resources_picket_noselect_title));
        button.setText(getResources().getString(R.string.word_note_list_cancel_delete));
        selected.setText("");
        delete_words.clear();
        word_num.setVisibility(View.INVISIBLE);
    }

    // 设置ListView数据
    private void setListView() {
        WordNote[] wordNotes = DataSupport.select("word,info").where().find(WordNote.class).toArray(new WordNote[0]);
        wordNoteAdapter = new WordNoteAdapter(WordNoteList.this, wordNotes,YAHEI);
        listView.setAdapter(wordNoteAdapter);
    }

    protected void delete_word_php(final String word){
        word_note_layout.addView(loadingPHP);
        new Thread(new Runnable() {
            @Override
            public void run() {
                final OkHttpClient okHttpClient = new OkHttpClient();
                FormBody body = new FormBody.Builder()
                        .add("phone_number", login_user)
                        .add("new_word",word)
                        .build();
                final Request request = new Request.Builder()
                        .url("http://fr.xsinweb.com/fr/service/Delete_New_Word.php")
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
