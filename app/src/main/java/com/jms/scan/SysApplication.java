package com.jms.scan;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import org.xutils.x;

import java.util.LinkedList;
import java.util.List;

/**
 * 全局APPlication
 *
 * @author zhangdb
 */
public class SysApplication extends Application {
    private static SysApplication mInstance;
    private final List<Activity> activityList=new LinkedList<Activity>();
    private static Context mContext;
    private static final String TAG=SysApplication.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        x.Ext.setDebug(BuildConfig.DEBUG);
        mInstance=this;
        DataCenter.init(this);
        mContext = getApplicationContext();
//        UnCeHandler catchExcep=new UnCeHandler(this);
//        Thread.setDefaultUncaughtExceptionHandler(catchExcep);
    }

    public static Context getContext(){
        return mContext;
    }

    public static SysApplication getInstance() {
        if (null == mInstance) {
            synchronized (SysApplication.class) {
                if (null == mInstance) {
                    mInstance=new SysApplication();
                }
            }
        }
        return mInstance;
    }

    public void addActivity(Activity activity) {
        if (activityList != null && activityList.contains(activity)) return;
        activityList.add(activity);
    }


    public void exit() {
        //清空缓存
        DataCenter.get().clearCache();
        //清空Activity栈
        for (Activity activity : activityList) {
            if (activity != null) {
                if (!activity.isFinishing()) activity.finish();
                activity=null;
            }
            activityList.remove(activity);
        }
    }


}
