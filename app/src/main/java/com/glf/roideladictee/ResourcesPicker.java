package com.glf.roideladictee;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.glf.roideladictee.MyAdapter.FileAdapter;
import com.glf.roideladictee.MyComparator.FileComparator;
import com.glf.roideladictee.tools.BaseActivity;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * Created by 11951 on 2018-05-02.
 */

public class ResourcesPicker extends BaseActivity {

    Button button;
    View button_action;
    ListView listView;
    FileAdapter fileAdapter;
    File[] data;
    File f;
    Comparator cmp;
    TextView resources_picket_now_path;
    String selectFileEndsWith = ".mp4";
    String videoPath,captionsPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resources_picker);
        Init();// 初始化
    }

    protected void Init(){
        getExtra();//获取参数
        buttonGroupInit();//按钮相关初始化
        listViewInit(); // ListView初始化
    }

    //获取参数
    protected void getExtra(){
        try{
            selectFileEndsWith = getIntent().getExtras().getString("selectFileEndsWith");
            videoPath = getIntent().getExtras().getString("videoPath");
            captionsPath = getIntent().getExtras().getString("captionsPath");
        }catch (Exception e){
            //这个会经常抛出，是正常情况，里面顺序不能变
        }
        if (selectFileEndsWith == null)selectFileEndsWith = ".mp4";
    }

    //按钮相关初始化
    protected void buttonGroupInit(){
        buttonInit();//按钮初始化
        buttonTextSet();//按钮文字初始化
    }

    //按钮初始化
    protected void buttonInit(){
        button = (Button)findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(f.isDirectory())return;
                if(selectFileEndsWith == ".mp4"){
                    Intent intent = new Intent(ResourcesPicker.this,ResourcesPicker.class);
                    intent.putExtra("videoPath",f.getPath());
                    intent.putExtra("selectFileEndsWith",".ass");
                    Log.e("ljong_rp1",f.getPath());
                    startActivity(intent);
                    finish();
                }else{
                    Intent intent = new Intent(ResourcesPicker.this,VideoAddNewWord.class);
                    intent.putExtra("videoPath",videoPath);
                    intent.putExtra("captionsPath",f.getPath());
                    Log.e("ljong_rp2",videoPath+" "+f.getPath());
                    startActivity(intent);
                    finish();
                }
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
    //按钮文字初始化
    protected void buttonTextSet(){
        if(selectFileEndsWith == ".mp4"){
            button.setText(getResources().getString(R.string.resources_picket_next));
        }else{
            button.setText(getResources().getString(R.string.resources_picket_ok));
        }
    }


    // ListView初始化
    private void listViewInit() {
        listView = (ListView) findViewById(R.id.listView);
        f = new File(Environment.getExternalStorageDirectory() + "/french");
        setListView();

        fileAdapter = new FileAdapter(this, data);
        listView.setAdapter(fileAdapter);

        // 注册监听器
        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(
                            AdapterView<?> parent,
                            View view,
                            int position,
                            long id) {

                        f = fileAdapter.getItem(position);
                        // 如果是目录 进入目录
                        if (f.isDirectory()) {
                            // TODO
                            setListView();
                        } else {
                            // TODO
                            if(button_action != null){
                                button_action.setVisibility(View.INVISIBLE);
                            };
                            button_action =  view.findViewById(R.id.imageButton_action);
                            button_action.setVisibility(View.VISIBLE);
                            TextView resources_picket_selected_title = (TextView)findViewById(R.id.resources_picket_selected_title);
                            resources_picket_selected_title.setText(getResources().getString(R.string.resources_picket_selected_title));
                            TextView resources_picket_selected = (TextView)findViewById(R.id.resources_picket_selected);
                            resources_picket_selected.setText(f.getName());
                        }
                    }
                }
        );
    }

    private void setListView(){
        data = f.listFiles();
        ArrayList<File> files = new ArrayList<>();
        for(File file:data){
            if(file.isDirectory()||file.getName().endsWith(selectFileEndsWith)){
                files.add(file);
            }
        }
        data = files.toArray(new File[0]);
        Arrays.sort(data,cmp);
        fileAdapter = new FileAdapter(ResourcesPicker.this, data);
        listView.setAdapter(fileAdapter);
        resources_picket_now_path = (TextView)findViewById(R.id.resources_picket_now_path) ;
        resources_picket_now_path.setText(f.getPath());
    }

    @Override
    public void onBackPressed() {
        if(f.equals(Environment.getExternalStorageDirectory()))super.onBackPressed();
        else{
            f = f.getParentFile();
            setListView();
        }
    }
}

