package com.mainli.d.d2018;

import android.app.Application;
import android.content.Context;
import android.provider.Settings;
import android.util.Log;

import com.mainli.log.CrashHandler;
import com.mainli.log.L;

import java.io.File;
import java.util.Arrays;


/**
 * Application基类
 * Created by shixiaoming on 16/12/6.
 */

public class MyApplication extends Application implements CrashHandler.ErrorHandler {
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        MyApplication.mContext = getApplicationContext();
        L.init(new File(getExternalCacheDir(), "log"), 4028, false);
        CrashHandler.init(this, this);
        L.i("Mainli", "初始化完成");
    }

    public static Context getAppContext() {
        return MyApplication.mContext;
    }

    public static String getAndroidId() {
        return Settings.Secure.getString(getAppContext().getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    private static final String CRASH_HANDLER_TAG = "CrashHandler";
    private static StringBuilder mPrefixInfo = null;
    private static int headLenght=0;

    @Override
    public boolean errorHandler(Thread thread, Throwable t) {
        if (mPrefixInfo == null) {
            mPrefixInfo = new StringBuilder()
                    .append("\n***********************************\n")
                    .append("versionName: ").append(BuildConfig.VERSION_NAME).append('\n')
                    .append("versionCode: ").append(BuildConfig.VERSION_CODE).append('\n')
                    .append("手机品牌: ").append(android.os.Build.BRAND).append('\n')
                    .append("手机型号: ").append(android.os.Build.MODEL).append('\n')
                    .append("CPU: ").append(Arrays.toString(android.os.Build.SUPPORTED_ABIS));
            headLenght = mPrefixInfo.length();
        }
        if (mPrefixInfo.length()>headLenght){
            mPrefixInfo.delete(headLenght,mPrefixInfo.length());
        }
        mPrefixInfo.append("\nCrash-Thread-Name: ");
        mPrefixInfo.append(thread.getName()).append('\n');
        mPrefixInfo.append(Log.getStackTraceString(t));
        mPrefixInfo .append("\n***********************************\n");
        L.e(CRASH_HANDLER_TAG, mPrefixInfo.toString());
        return true;
    }
}
