package com.glf.roideladictee.tools;

import android.app.Activity;
import android.app.Application;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by lenovo on 2018/4/27.
 */


public class BaseActivity extends AppCompatActivity {
    protected static boolean isFirst=true;
    protected static boolean isLogin=false;
    protected static String login_user = "13590799319";
    protected ActivityController activityController;
    protected HomeWatcherReceiver homewatcherreceiver;
    protected static NotificationManager myManager;
    /**
     * 管理所有Activity的类
     */
    public class ActivityController extends Application {
        protected List<Activity> activities = new ArrayList<>();
        /**
         * 添加Activity
         *
         * @param activity
         */
        protected void addActivity(Activity activity) {
            activities.add(activity);
        }
        /**
         * 移除Activity
         *
         * @param activity
         */
        protected void removeActivity(Activity activity) {
            activities.remove(activity);
        }
        public Activity getRecentActivity(){
            return activities.get(activities.size()-1);
        }
        /**
         * 结束所有Activity
         */
        public void finishAll() {
            for (Activity activity : activities) {
                if (!activity.isFinishing()) {
                    Log.e("hahaha","结束活动："+activity.getLocalClassName());
                    activity.finish();
                }
            }
        }
        @Override
        public void onTerminate() {
            Log.e("hahaha","当前PID="+android.os.Process.myPid());
            super.onTerminate();
            finishAll();
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }
    /**
     * 广播接收器
     */
    protected class HomeWatcherReceiver extends BroadcastReceiver {
        private static final String LOG_TAG = "HomeReceiver";
        private static final String SYSTEM_DIALOG_REASON_KEY = "reason";
        private static final String SYSTEM_DIALOG_REASON_RECENT_APPS = "recentapps";
        private static final String SYSTEM_DIALOG_REASON_HOME_KEY = "homekey";
        private static final String SYSTEM_DIALOG_REASON_LOCK = "lock";
        private static final String SYSTEM_DIALOG_REASON_ASSIST = "assist";
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.i(LOG_TAG, "onReceive: action: " + action);
            if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
                // android.intent.action.CLOSE_SYSTEM_DIALOGS
                String reason = intent.getStringExtra(SYSTEM_DIALOG_REASON_KEY);
                Log.i(LOG_TAG, "reason: " + reason);
                if (SYSTEM_DIALOG_REASON_HOME_KEY.equals(reason)) {
                    // 短按Home键
                    Log.i(LOG_TAG, "homekey");
                    Toast.makeText(getApplicationContext(), "最小化到后台运行",Toast.LENGTH_SHORT).show();
                }
                else if (SYSTEM_DIALOG_REASON_RECENT_APPS.equals(reason)) {
                    // 长按Home键 或者 activity切换键
                    Log.i(LOG_TAG, "long press home key or activity switch");
                }
                else if (SYSTEM_DIALOG_REASON_LOCK.equals(reason)) {
                    // 锁屏
                    Log.i(LOG_TAG, "lock");
                }
                else if (SYSTEM_DIALOG_REASON_ASSIST.equals(reason)) {
                    // samsung 长按Home键
                    Log.i(LOG_TAG, "assist");
                }
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //创建出管理所有Activity类的对象
        activityController = new ActivityController();
        //将所有的Activity添加进来
        activityController.addActivity(this);
        //系统按键监听器
        homewatcherreceiver=new HomeWatcherReceiver();
    }
    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter homeFilter = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        registerReceiver(homewatcherreceiver, homeFilter);
    }

    @Override
    protected void onPause() {
        if (homewatcherreceiver != null) {
            unregisterReceiver(homewatcherreceiver);
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        //在这个生命周期中销毁所有的Activity
        activityController.removeActivity(this);
        super.onDestroy();
    }

    public static void setAllEnabled(View view, boolean enabled) {
        if (null == view) {
            return;
        }
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            LinkedList<ViewGroup> queue = new LinkedList<ViewGroup>();
            queue.add(viewGroup);
            // 遍历viewGroup
            while (!queue.isEmpty()) {
                ViewGroup current = queue.removeFirst();
                if (/*current.getId()!= R.id.custom_departure_page_2_departure_info_start_address*/true) {
                    current.setEnabled(enabled);
                    for (int i = 0; i < current.getChildCount(); i++) {
                        if (current.getChildAt(i) instanceof ViewGroup) {
                            queue.addLast((ViewGroup) current.getChildAt(i));
                        } else {
                            if (/*current.getChildAt(i).getId()!=R.id.custom_departure_page_2_departure_info_start_address*/true) {
                                current.getChildAt(i).setEnabled(enabled);
                            }
                        }
                    }
                } else {
                    if (/*view.getId()!=R.id.custom_departure_page_2_departure_info_start_address*/true) {
                        view.setEnabled(enabled);
                    }
                }
            }
        }
    }
    public static void setAllVisible(View view, boolean enabled) {
        if(null == view) {
            return;
        }
        if(view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            LinkedList<ViewGroup> queue = new LinkedList<ViewGroup>();
            queue.add(viewGroup);
            // 遍历viewGroup
            while(!queue.isEmpty()) {
                ViewGroup current = queue.removeFirst();
                current.setVisibility(enabled?View.VISIBLE:View.INVISIBLE);
                for(int i = 0; i < current.getChildCount(); i ++) {
                    if(current.getChildAt(i) instanceof ViewGroup) {
                        queue.addLast((ViewGroup) current.getChildAt(i));
                    }else {
                        current.getChildAt(i).setVisibility(enabled?View.VISIBLE:View.INVISIBLE);
                    }
                }
            }
        }else {
            view.setVisibility(enabled?View.VISIBLE:View.INVISIBLE);
        }
    }
    public static void setAllClickable(View view, boolean enabled) {
        if(null == view) {
            return;
        }
        if(view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            LinkedList<ViewGroup> queue = new LinkedList<ViewGroup>();
            queue.add(viewGroup);
            // 遍历viewGroup
            while(!queue.isEmpty()) {
                ViewGroup current = queue.removeFirst();
                current.setClickable(enabled);
                for(int i = 0; i < current.getChildCount(); i ++) {
                    if(current.getChildAt(i) instanceof ViewGroup) {
                        queue.addLast((ViewGroup) current.getChildAt(i));
                    }else {
                        current.getChildAt(i).setClickable(enabled);
                    }
                }
            }
        }else {
            view.setClickable(enabled);
        }
    }
}
