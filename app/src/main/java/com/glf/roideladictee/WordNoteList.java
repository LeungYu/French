package com.glf.roideladictee;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.glf.roideladictee.MyAdapter.FileAdapter;
import com.glf.roideladictee.MyAdapter.WordNoteAdapter;
import com.glf.roideladictee.MyLitePal.CaptionAss;
import com.glf.roideladictee.MyLitePal.WordNote;
import com.glf.roideladictee.tools.BaseActivity;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 11951 on 2018-05-04.
 * 生词本界面
 */

public class WordNoteList extends BaseActivity {

    private ListView listView;
    private Typeface YAHEI;
    private WordNoteAdapter wordNoteAdapter;
    private Button delete_button,button;
    private TextView selected_title,selected,word_num;
    private List<String> delete_words = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_note_list);
        Init();// 初始化
    }

    // 初始化
    protected void Init(){
        textGroupInit();//字体相关初始化
        buttonGroupInit();//按钮相关初始化
        DBInit();//数据库初始化
        listViewInit(); // ListView初始化
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
                        if(findViewById(R.id.botton_bar).getVisibility()!=View.VISIBLE)return;
                        WordNote wordNote = wordNoteAdapter.getItem(position);
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
}
