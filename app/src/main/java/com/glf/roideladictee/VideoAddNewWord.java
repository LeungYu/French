package com.glf.roideladictee;

import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Message;
import android.support.percent.PercentRelativeLayout;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.glf.roideladictee.MyAdapter.CaptionAdapter;
import com.glf.roideladictee.MyEnum.TestStatus;
import com.glf.roideladictee.MyEnum.VideoMode;
import com.glf.roideladictee.MyLayoutManager.CaptionLayoutManager;
import com.glf.roideladictee.MyLitePal.CaptionAss;
import com.glf.roideladictee.MyLitePal.WordNote;
import com.glf.roideladictee.MyView.FullScreenVideoView;
import com.glf.roideladictee.MyView.TestInputDialog;
import com.glf.roideladictee.TranslateWindow.TranslatorFrame;
import com.glf.roideladictee.fr_app.fr_contest;
import com.glf.roideladictee.tools.BaseActivity;
import com.glf.roideladictee.tools.LocaleUtils;

import org.json.JSONArray;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.glf.roideladictee.tools.LocaleUtils.LOCALE_CHINESE;

/**
 * Created by 11951 on 2018-04-24.
 * 电影播放界面--两种模式：ADD（原本目的功能）TEST(扩展之后的功能)
 * 类命名为AddNewWord（ADD）,实际上也可以扩展出TEST功能
 */
public class VideoAddNewWord extends BaseActivity {

    private PercentRelativeLayout video_add_new_word_percent_relative_layout;
    private Button add_word_button;
    private Button play_button;
    private TextView movie_title;
    private fr_contest frcontest;

    /*全局设置top*/
    private VideoMode videoMode = VideoMode.ADD;
    /*全局设置botton*/

    /*其他handler what值top*/
    private static final int TRANS = 1101;
    /*其他handler what值botton*/

    /*外用top*/
    public static int percentx= 1,percenty = 1;//CaptionAdapter,MyLayoutManager遵循这个屏幕缩放比例
    public static Boolean playing = false;//是否在播放
    public static TestStatus testStatus = TestStatus.TESTING;
    /*外用botton*/

    /*字幕用top*/
    private RecyclerView captions_recyclerview;
    private List<String> captions;
    private List<Boolean> isTestCaptions;
    private CaptionAdapter captionAdapter;
    private CaptionLayoutManager mLayoutManager;
    private String captionsPath = "storage/emulated/0/french/test.ass";
    private int startIndex,endIndex,textIndex;
    private String lastCaptions = "";
        /*ADD模式用top*/
        private String addWord;
        /*ADD模式用botton*/
        /*TEST模式用top*/
        private int testCaptionMinLength = 6;
        private float numPercentCaption = (float)0.50;
        private Boolean hasSetTestCaption = false;
        private String testCaption = "TESTCAPTION";
        private int seeToWhere = -3;
        private boolean firstCaptions = true;
        private TestInputDialog testInputDialog;
        private int sum = 0;
        private int right = 0;
        private float testMin =(float) 2;
        /*TEST模式用end*/
    /*字幕用botton*/

    /*视频用top*/
    private int endTime = -1;
    private VideoView videoView;
    private String videoPath = "storage/emulated/0/french/test.mp4";
    private Uri videoUrl;
    private static final int PLAY = 1111,LOADING = 1110;
    Handler handler;
    /*视频用botton*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (language.equals("ZH")) {
            LocaleUtils.updateLocale(VideoAddNewWord.this, LOCALE_CHINESE);
        }else{
            LocaleUtils.updateLocale(VideoAddNewWord.this, LocaleUtils.LOCALE_FRENCH);
        }
        setContentView(R.layout.video_add_new_word);
        Init();//初始化
    }

    //初始化
    protected void Init(){
        getExtra();//获取参数
        screenPercentInit();//获取屏幕的宽高，生成与原型的相差比例
        screenInit();//屏幕初始化
        textGroupInit();//字体相关初始化
        headBarInit();//顶头信息条初始化
        if(videoMode == VideoMode.ADD){
            addWordButtonInit();//“添加到生词本”按钮初始化，及相关设置
        }
        playButtonInit();//播放按钮初始化
        videoGroupInit();//视频相关初始化
    }

    //获取参数
    protected void getExtra(){
        try {
            videoMode = (VideoMode) getIntent().getExtras().get("videoMode");
            videoPath = getIntent().getExtras().getString("videoPath");
            captionsPath = getIntent().getExtras().getString("captionsPath");
            frcontest = (fr_contest) getIntent().getSerializableExtra("fr_contest");
        }catch (Exception e){
            Log.e("ljong","这里会经常抛出，是正常现象.");
        }
    }

    //获取屏幕的宽高，生成与原型的相差比例
    protected void screenPercentInit(){
    }

    //文字相关初始化
    protected void textGroupInit(){
        Typeface YAHEI = Typeface.createFromAsset(getAssets(), "fonts/YAHEI.ttc");
        int textViewIds[] = {R.id.movie_title,R.id.movie_name,R.id.add_word_button};
        for(int textViewId:textViewIds){
            ((TextView)findViewById(textViewId)).getPaint().setTypeface(YAHEI);
        }
    }

    //屏幕初始化
    protected void screenInit(){
        video_add_new_word_percent_relative_layout = (PercentRelativeLayout)findViewById(R.id.video_add_new_word_percent_relative_layout);
        if(videoMode == VideoMode.ADD)video_add_new_word_percent_relative_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(playing)videoView.pause();
                else if(endTime != videoView.getCurrentPosition())videoView.start();
            }
        });
    }

    //顶头信息条初始化
    protected void headBarInit(){
        movie_title = (TextView)findViewById(R.id.movie_title);
        String path_splits[] = videoPath.split("/");
        if(videoMode ==VideoMode.ADD){
            movie_title.setText(path_splits[path_splits.length-1]);
        }else{
            movie_title.setText(path_splits[path_splits.length-1]+getString(R.string.test_head_sign));
        }
    }

    //“添加到生词本”按钮初始化，及相关设置
    protected void addWordButtonInit(){
        add_word_button = (Button)findViewById(R.id.add_word_button);
        add_word_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_word_button.setVisibility(View.INVISIBLE);
                stringInitTranslation(addWord);
                add_word_php(addWord.replaceAll("\\.|@|\\?|!|\"|,",""));
            }
        });
        add_word_button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == 0) {
                    add_word_button.setAlpha(0.75f);
                }
                if (event.getAction() == 1 || event.getAction() == 3) {
                    add_word_button.setAlpha(1f);
                }
                return false;
            }
        });
    }

    //播放按钮初始化
    protected void playButtonInit(){
        play_button = (Button)findViewById(R.id.play_button);
        play_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(videoMode == VideoMode.TEST){
                    captions.clear();
                    isTestCaptions.clear();
                    captionAdapter.setCaptions(captions,isTestCaptions);
                    captions_recyclerview.setAdapter(captionAdapter);
                }
                play_button.setVisibility(View.INVISIBLE);
                videoView.start();
            }
        });
        play_button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == 0) {
                    play_button.setAlpha(0.75f);
                }
                if (event.getAction() == 1 || event.getAction() == 3) {
                    play_button.setAlpha(1f);
                }
                return false;
            }
        });
    }

    //视频相关初始化
    protected void videoGroupInit(){
        handlerInit();//handler初始化
        captionsInit();//字幕容器RecyclerView初始化
        captionsAssDBInit();//字幕Ass数据库初始化
        videoViewInit();//视频屏幕及源初始化
        new Thread(new Runnable() {
            @Override
            public void run() {
                captionsSet();//字幕源设置
            }
        }).start();
    }

    //handler初始化
    protected void handlerInit(){
        handler=new Handler(){
            public void handleMessage(Message msg){
                switch (msg.what) {
                    case PLAY://字幕同步handler型线程
                        if (!playing) break;
                        if(videoMode == VideoMode.TEST && videoView.getCurrentPosition() >= seeToWhere + testMin * 60 * 1000){
                            Intent intent = new Intent(VideoAddNewWord.this, TestResult.class);
                            intent.putExtra("sum", sum);
                            intent.putExtra("right", right);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("fr_contest", frcontest);
                            intent.putExtras(bundle);
                            startActivity(intent);
                            finish();
                        };
                        String currentPosition = String.valueOf(videoView.getCurrentPosition());
                        List<CaptionAss> captionAsses = DataSupport.select("text").where("start <= ? and end >= ?", currentPosition, currentPosition).find(CaptionAss.class);
                        StringBuilder sb = new StringBuilder();
                        for (CaptionAss captionAss : captionAsses) {
                            sb.append(captionAss.getText());
                            sb.append(" ");
                        }
                        if (!lastCaptions.equals(sb.toString())){
                            if (hasSetTestCaption){
                                videoView.pause();
                                String temp="";
                                for(int i =0;i < testCaption.length();i++){
                                    temp += "_";
                                }
                                String tempCaptions = lastCaptions.replace(testCaption,temp);
                                Log.e("ljong",testCaption+" "+tempCaptions);
                                testInputDialog = new TestInputDialog(VideoAddNewWord.this);
                                video_add_new_word_percent_relative_layout.addView(testInputDialog);
                                testInputDialog.idontknow.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        video_add_new_word_percent_relative_layout.removeView(testInputDialog);
                                        play_button.setVisibility(View.VISIBLE);
                                        setTestStatus(TestStatus.WRONG);
                                        captionsRefresh(lastCaptions);
                                    }
                                });
                                testInputDialog.ok.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        video_add_new_word_percent_relative_layout.removeView(testInputDialog);
                                        play_button.setVisibility(View.VISIBLE);
                                        if(testCaption.equals(testInputDialog.test_input.getText().toString()))setTestStatus(TestStatus.RIGHT);
                                        else {
                                            setTestStatus(TestStatus.WRONG);
                                        }
                                        captionsRefresh(lastCaptions);
                                    }
                                });
                            }else {
                                testStatus = TestStatus.TESTING;
                                captionsRefresh(sb.toString());//刷新字幕
                                firstCaptions = false;
                                lastCaptions = sb.toString();
                            }
                        }
                        handler.sendEmptyMessageDelayed(PLAY,500);
                        break;
                    case LOADING://载入资源LOADING完成
                        findViewById(R.id.loading).setVisibility(View.INVISIBLE);
                        videoPlay();
                        break;
                    case TRANS:
                        String[] setter = (String[])msg.obj;
                        WordNote wordNote = new WordNote();
                        try{
                            wordNote.setWord(""+setter[0]);
                            wordNote.setInfo(""+setter[1]+setter[2]);
                        }catch (Exception e){
                            Log.e("ljong","日常报错");
                        }
                        wordNote.save();
                        break;
                    case 0X12:
                        String jsonData;
                        jsonData = (String) msg.obj;
                        if(!jsonData.equals("1")) Toast.makeText(VideoAddNewWord.this,jsonData,Toast.LENGTH_LONG).show();
                        break;
                }
            }
        };
    }

    //字幕容器RecyclerView初始化
    protected void captionsInit(){
        captions_recyclerview = (RecyclerView)findViewById(R.id.captions_recyclerview);
        captions_recyclerview.setHasFixedSize(true);
        mLayoutManager = new CaptionLayoutManager();
        mLayoutManager.setAutoMeasureEnabled(true);
        captions_recyclerview.setLayoutManager(mLayoutManager);
        captions = new ArrayList<>();
        isTestCaptions = new ArrayList<>();
        captionAdapter = new CaptionAdapter(videoMode);
        captionAdapter.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/YAHEI.ttc"));
        if(videoMode == VideoMode.ADD){captionAdapter.setOnItemClickListener(new CaptionAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(TextView mTextview, String caption,Boolean isTestCaption) {
                    if(playing)videoView.pause();
                    reflesh();//选定的单词时的样式刷新
                    mTextview.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG|Paint.ANTI_ALIAS_FLAG);
                    addWord = caption;
                    add_word_button.setVisibility(View.VISIBLE);
                    ViewGroup.MarginLayoutParams margin = new ViewGroup.MarginLayoutParams(add_word_button.getLayoutParams());
                    margin.setMargins(captions_recyclerview.getLeft() + mTextview.getRight() - 40 * percentx,mTextview.getTop() + captions_recyclerview.getTop() - margin.height + 20 * percenty,0,0);
                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(margin);
                    add_word_button.setLayoutParams(layoutParams);
                }
            });
        }
    }

    //字幕Ass数据库初始化
    protected void captionsAssDBInit(){
        Connector.getDatabase();
        DataSupport.deleteAll(CaptionAss.class);
    }

    //字幕源设置
    protected void captionsSet(){
        try {
            File file = new File(captionsPath);
            int record = 0;
            startIndex = -1;
            endIndex = -1;
            textIndex = -1;
            if (file.isFile() && file.exists()) {       //文件存在的前提
                InputStreamReader isr = new InputStreamReader(new FileInputStream(file));
                BufferedReader br = new BufferedReader(isr);
                String lineTxt = null;
                while ((lineTxt = br.readLine()) != null) {
                    if (!"".equals(lineTxt)) {
                        if(record !=2 && lineTxt.startsWith("Format:")){
                            record++;
                            lineTxt = lineTxt.substring(8);
                            String types[] = lineTxt.split(",");
                            for(int i = 0;i<types.length;i++){
                                if(types[i].equals(" Start")){
                                    startIndex = i;
                                }
                                else if(types[i].equals(" End")){
                                    endIndex = i;
                                }
                                else if(types[i].equals(" Text")){
                                    textIndex = i;
                                }
                            }
                        }else if(record == 2 && lineTxt.startsWith("Dialogue:")){
                            lineTxt = lineTxt.substring(9);
                            String assDatas[] = lineTxt.split(",");
                            CaptionAss captionAss = new CaptionAss();
                            captionAss.setStart(stringToTime(assDatas[startIndex]));
                            captionAss.setEnd(stringToTime(assDatas[endIndex]));
                            StringBuilder sb = new StringBuilder();
                            sb.append(assDatas[textIndex]);
                            for(int i = textIndex + 1;i < assDatas.length;i++){
                                sb.append(",");
                                sb.append(assDatas[i]);
                            }
                            captionAss.setText(sb.toString());
                            captionAss.save();
                        }
                    }
                }
                isr.close();
                br.close();
                handler.sendEmptyMessage(LOADING);
            }else {
                Toast.makeText(getApplicationContext(),"字幕不存在",Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),"无法加载字幕",Toast.LENGTH_SHORT).show();
        }
    }

    //选定的单词时的样式刷新
    protected void reflesh(){
        for(int i = 0; i < mLayoutManager.getItemCount(); i++) {
            TextView tTextview = (TextView)mLayoutManager.getChildAt(i);
            try {
                tTextview.setPaintFlags(Paint.ANTI_ALIAS_FLAG);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    //视频屏幕及源初始化
    protected void videoViewInit(){
        videoView = (VideoView)this.findViewById(R.id.videoView);
        MediaController mc = new MediaController(this);
        mc.setVisibility(View.INVISIBLE);
        videoView.setMediaController(mc);

        ((FullScreenVideoView)videoView).setOnStatusChangeListener(new FullScreenVideoView.OnStatusChangeListener() {
            @Override
            public void onPause() {
                setPlaying(false);
            }

            @Override
            public void onPlay() {
                setPlaying(true);
            }
        });
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                setPlaying(false);
                endTime = videoView.getCurrentPosition();
            }
        });
    }

    //视屏播放
    protected void videoPlay(){
        videoUrl = Uri.parse(videoPath);
        videoView.setVideoURI(videoUrl);
        videoView.start();
        if(videoMode == VideoMode.TEST){
            firstCaptions = true;
            MediaMetadataRetriever mmr = new MediaMetadataRetriever();
            mmr.setDataSource(VideoAddNewWord.this,videoUrl);
            String duration = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            seeToWhere = (int)Math.round(Math.random() * (Integer.parseInt(duration) - 5 * 60 * 1000));
            if(seeToWhere < 0)seeToWhere = 0;
            videoView.seekTo(seeToWhere);
        }
    }

    //字符串时间0:00:01.00转换为int时间1000
    protected long stringToTime(String s){
        String s1[] = s.split(":");
        int time = 0;
        try {
            time = Integer.parseInt(s1[0]) * 60 + Integer.parseInt(s1[1]);
            String s2[] = s1[2].split("\\.");
            time = time * 60000 + Integer.parseInt(s2[0]) * 1000 + Integer.parseInt(s2[1]) * 10;
        }catch (Exception e){
            time = 0;
            Log.e("VideoAddNewWord","时间格式不对");
        }
        return time;
    }

    //设置播放
    protected void setPlaying(boolean b){
        if(b){
            playing = true;
            if(videoMode == VideoMode.ADD){
                add_word_button.setVisibility(View.INVISIBLE);
                reflesh();
                play_button.setVisibility(View.INVISIBLE);
            }else {
                hasSetTestCaption = false;
            }
            handler.sendEmptyMessage(PLAY);//字幕同步handler型线程
        }else{
            playing = false;
            if(videoMode == VideoMode.ADD){
                play_button.setVisibility(View.VISIBLE);
            }
        }
    }

    //字幕刷新
    protected  void captionsRefresh(String text){
        if(testStatus == TestStatus.WRONG || testStatus == TestStatus.RIGHT ){
            captions.clear();
            isTestCaptions.clear();
            String mcaptions[] = text.split(" ");
            hasSetTestCaption = false;
            for(String mcaption:mcaptions) {
                captions.add(mcaption);
                if(testCaption.equals(mcaption.replaceAll("\\.|@|\\?|!|\"|,",""))){
                    isTestCaptions.add(true);
                }else isTestCaptions.add(false);
            }
        }else{
            captions.clear();
            isTestCaptions.clear();
            String mcaptions[] = text.split(" ");
            hasSetTestCaption = false;
            for(String mcaption:mcaptions) {
                captions.add(mcaption);
                if (videoMode == VideoMode.TEST) {
                    if (!firstCaptions && !hasSetTestCaption && mcaption.length() > testCaptionMinLength
                            && mcaption.indexOf('\'')==-1 && mcaption.indexOf('-')==-1 && Math.random() > numPercentCaption) {
                        hasSetTestCaption = true;
                        testCaption = mcaption.replaceAll("\\.|@|\\?|!|\"|,","");
                        isTestCaptions.add(true);
                    } else {
                        isTestCaptions.add(false);
                    }
                }else{
                    isTestCaptions.add(false);
                }
            }
        }
        captionAdapter.setCaptions(captions,isTestCaptions);
        captions_recyclerview.setAdapter(captionAdapter);
    }

    //听写模式下是否正确
    protected void setTestStatus(TestStatus testStatus){
        if(testStatus == TestStatus.RIGHT){
            this.testStatus = testStatus;
            right++;
            sum++;
        }else {
            stringInitTranslation(testCaption);
            wrong_word_php(testCaption.replaceAll("\\.|@|\\?|!|\"|,",""));
            sum++;
            this.testStatus = testStatus;
            Intent intent = new Intent(VideoAddNewWord.this, TranslatorFrame.class);
            intent.putExtra("target", testCaption);
            startActivity(intent);
        }
    }

    public void stringInitTranslation(String temp){
        final String target = temp.replaceAll("\\.|@|\\?|!|\"|,","");
        if (DataSupport.select("word").where("word = ?",target).find(WordNote.class).size() != 0)return;
        final String[] setter = {target,"",""};
        new Thread(){
            public void run(){
                Document doc=null;
                try {
                    doc = Jsoup.connect("http://www.frdic.com/dicts/fr/"+target).get();
                } catch (IOException e) {
                    return;
                }
                try {
                    String wordType = doc.getElementById("ExpFCChild").getElementsByClass("cara").first().text();
                    setter[1] = wordType;
                }
                catch (Exception e){
                    setter[1]="NULL";
                }
                try {
                    String wordMeaning=doc.getElementById("ExpFCChild").getElementsByClass("exp").first().text();
                    wordMeaning.substring(wordMeaning.indexOf(","),wordMeaning.length());
                    setter[2] = wordMeaning;
                }
                catch (Exception e){
                    setter[2]="NULL";
                }

                Message msg = new Message();
                msg.obj = setter;
                msg.what = TRANS;
                handler.sendMessage(msg);
            }
        }.start();
    }

    protected void add_word_php(final String word){
        new Thread(new Runnable() {
            @Override
            public void run() {
                final OkHttpClient okHttpClient = new OkHttpClient();
                FormBody body = new FormBody.Builder()
                        .add("phone_number", login_user)
                        .add("new_word",word)
                        .build();
                final Request request = new Request.Builder()
                        .url("http://fr.xsinweb.com/fr/service/Add_New_Word.php")
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
    protected void wrong_word_php(final String word){
        new Thread(new Runnable() {
            @Override
            public void run() {
                final OkHttpClient okHttpClient = new OkHttpClient();
                FormBody body = new FormBody.Builder()
                        .add("phone_number", login_user)
                        .add("new_word",word)
                        .build();
                final Request request = new Request.Builder()
                        .url("http://fr.xsinweb.com/fr/service/Add_Wrong_Word.php")
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
