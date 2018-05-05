package com.glf.roideladictee;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import com.glf.roideladictee.MyEnum.VideoMode;
import com.glf.roideladictee.fr_app.fr_contest;
import com.glf.roideladictee.tools.BaseActivity;
import com.glf.roideladictee.tools.LocaleUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import static com.glf.roideladictee.tools.LocaleUtils.LOCALE_CHINESE;

/**
 * Created by 11951 on 2018-05-02.
 * 文件管理器
 * 两个模式:.mp4,.ass
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
    String videoPath, captionsPath;
    VideoMode videoMode;
    int ASK_PERMISSION = 0X12;
    Typeface YAHEI;
    fr_contest frcontest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (language.equals("ZH")) {
            LocaleUtils.updateLocale(ResourcesPicker.this, LOCALE_CHINESE);
        }else{
            LocaleUtils.updateLocale(ResourcesPicker.this, LocaleUtils.LOCALE_FRENCH);
        }
        setContentView(R.layout.resources_picker);
        Init();// 初始化
    }

    protected void Init() {
        getExtra();//获取参数
        comparatorInit();//排序规则初始化
        textGroupInit();//字体相关初始化
        askPermission();//访问权限
        buttonGroupInit();//按钮相关初始化
    }

    //获取参数
    protected void getExtra() {
        try {
            videoMode = (VideoMode) getIntent().getExtras().get("videoMode");
            selectFileEndsWith = getIntent().getExtras().getString("selectFileEndsWith");
            videoPath = getIntent().getExtras().getString("videoPath");
            captionsPath = getIntent().getExtras().getString("captionsPath");
        } catch (Exception e) {
            //这个会经常抛出，是正常情况，里面顺序不能变
        }
        if (selectFileEndsWith == null) selectFileEndsWith = ".mp4";
        if (videoMode == null) videoMode = VideoMode.ADD;
        else if (videoMode == VideoMode.TEST) {
            frcontest = (fr_contest) getIntent().getSerializableExtra("fr_contest");
        }
    }

    //排序规则初始化
    protected void comparatorInit() {
        cmp = new FileComparator();
    }

    //访问权限
    protected void askPermission() {
        final String[] PERMISSIONS_STORAGE = {
                "android.permission.READ_EXTERNAL_STORAGE",
                "android.permission.WRITE_EXTERNAL_STORAGE"};

        //检查权限
        //检查版本是否大于M
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                //进入到这里代表没有权限.
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    //已经禁止提示了
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setCancelable(false)
                            .setMessage("应用需要存储权限来让您选择手机中的相片！")
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(ResourcesPicker.this, "点击了取消按钮", Toast.LENGTH_LONG).show();
                                }
                            })
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ActivityCompat.requestPermissions(ResourcesPicker.this, PERMISSIONS_STORAGE, ASK_PERMISSION);
                                }
                            }).show();

                } else {
                    ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, ASK_PERMISSION);
                }

            } else {
                listViewInit(); // ListView初始化
            }
        } else {
            listViewInit(); // ListView初始化
        }
    }

    //字体相关初始化
    protected void textGroupInit() {
        textTypefaceInit();//字体初始化
        textSet();//文字初始化
    }

    //字体初始化
    protected void textTypefaceInit() {
        YAHEI = Typeface.createFromAsset(getAssets(), "fonts/YAHEI.ttc");
        int textViewIds[] = {R.id.title, R.id.resources_picket_now_path_title, R.id.resources_picket_now_path, R.id.resources_picket_selected_title, R.id.resources_picket_selected};
        for (int textViewId : textViewIds) {
            ((TextView) findViewById(textViewId)).getPaint().setTypeface(YAHEI);
        }
    }

    //文字初始化
    protected void textSet() {
        TextView title = (TextView) findViewById(R.id.title);
        TextView resources_picket_selected_title = (TextView) findViewById(R.id.resources_picket_selected_title);
        TextView resources_picket_selected = (TextView) findViewById(R.id.resources_picket_selected);
        if (selectFileEndsWith.equals(".mp4")) {
            title.setText(getResources().getString(R.string.resources_picket_title_mp4));
        } else {
            title.setText(getResources().getString(R.string.resources_picket_title_ass));
        }
        if(videoMode == VideoMode.TEST){
            resources_picket_selected_title.setText(getResources().getString(R.string.resources_picket_please_select));
            if (selectFileEndsWith.equals(".mp4")) {
                resources_picket_selected.setText(frcontest.getMov_file());
            } else {
                resources_picket_selected.setText(frcontest.getMov_srt());
            }
        }
    }

    //按钮相关初始化
    protected void buttonGroupInit() {
        buttonInit();//按钮初始化
        buttonTextSet();//按钮文字初始化
    }

    //按钮初始化
    protected void buttonInit() {
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (f.isDirectory()) return;
                if (selectFileEndsWith.equals(".mp4")) {
                    Intent intent = new Intent(ResourcesPicker.this, ResourcesPicker.class);
                    intent.putExtra("videoPath", f.getPath());
                    intent.putExtra("selectFileEndsWith", ".ass");
                    intent.putExtra("videoMode", videoMode);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("fr_contest", frcontest);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(ResourcesPicker.this, VideoAddNewWord.class);
                    intent.putExtra("videoPath", videoPath);
                    intent.putExtra("captionsPath", f.getPath());
                    intent.putExtra("videoMode", videoMode);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("fr_contest", frcontest);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
                }
            }
        });

        button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
    }

    //按钮文字初始化
    protected void buttonTextSet() {
        if (selectFileEndsWith.equals(".mp4")) {
            button.setText(getResources().getString(R.string.resources_picket_next));
        } else {
            button.setText(getResources().getString(R.string.resources_picket_ok));
        }
    }


    // ListView初始化
    private void listViewInit() {
        listView = (ListView) findViewById(R.id.listView);
        f = Environment.getExternalStorageDirectory();
        setListView();

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
                            if (button_action != null) {
                                button_action.setVisibility(View.INVISIBLE);
                            }
                            ;
                            button_action = view.findViewById(R.id.imageButton_action);
                            button_action.setVisibility(View.VISIBLE);
                            if(videoMode == VideoMode.ADD){
                                TextView resources_picket_selected_title = (TextView) findViewById(R.id.resources_picket_selected_title);
                                resources_picket_selected_title.setText(getResources().getString(R.string.resources_picket_selected_title));
                                TextView resources_picket_selected = (TextView) findViewById(R.id.resources_picket_selected);
                                resources_picket_selected.setText(f.getName());
                            }
                        }
                    }
                }
        );
    }

    // 设置ListView数据
    private void setListView() {
        data = f.listFiles();
        ArrayList<File> files = new ArrayList<>();
        if(videoMode == VideoMode.ADD){
            for (File file : data) {
                if (file.isDirectory() || file.getName().endsWith(selectFileEndsWith)) {
                    files.add(file);
                }
            }
        }else{
            for (File file : data) {
                if (file.isDirectory()) {
                    files.add(file);
                }else if(selectFileEndsWith.equals(".mp4") && frcontest.getMov_file().equals(file.getName())){
                    files.add(file);
                }else if(selectFileEndsWith.equals(".ass") && frcontest.getMov_srt().equals(file.getName())){
                    files.add(file);
                }
            }
        }
        data = files.toArray(new File[0]);
        Arrays.sort(data, cmp);
        fileAdapter = new FileAdapter(ResourcesPicker.this, data, YAHEI);
        listView.setAdapter(fileAdapter);
        resources_picket_now_path = (TextView) findViewById(R.id.resources_picket_now_path);
        resources_picket_now_path.setText(f.getPath());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == ASK_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                listViewInit();
            } else {
                Toast.makeText(ResourcesPicker.this, "你不给权限给我干嘛,小拳拳捶你胸，哼！", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (f.equals(Environment.getExternalStorageDirectory())) super.onBackPressed();
        else {
            f = f.getParentFile();
            setListView();
        }
    }
}

