package com.glf.roideladictee;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.glf.roideladictee.MyAdapter.FileAdapter;
import com.glf.roideladictee.MyAdapter.WordNoteAdapter;
import com.glf.roideladictee.MyLitePal.CaptionAss;
import com.glf.roideladictee.MyLitePal.WordNote;
import com.glf.roideladictee.tools.BaseActivity;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * Created by 11951 on 2018-05-04.
 */

public class WordNoteList extends BaseActivity {

    private ListView listView;
    private Typeface YAHEI;
    private WordNoteAdapter wordNoteAdapter;

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
    }

    //文字初始化
    protected void textSet(){

    }

    //按钮相关初始化
    protected void buttonGroupInit(){

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

                    }
                });
    }

    // 设置ListView数据
    private void setListView() {
        WordNote[] wordNotes = DataSupport.select("word,info").where().find(WordNote.class).toArray(new WordNote[0]);
        wordNoteAdapter = new WordNoteAdapter(WordNoteList.this, wordNotes,YAHEI);
        listView.setAdapter(wordNoteAdapter);
    }
}
