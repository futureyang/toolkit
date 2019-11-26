package com.future.toolkit.utils.log;

import android.util.Log;

import androidx.annotation.NonNull;

/**
 * Created by yangqc on 2019/9/23
 *
 * @Author yangqc
 */
public class LogUtils {

    private static ILog sLog = new LogImpl();

    public static void setLogImpl(@NonNull ILog log){
        sLog = log;
    }

    /**
     * 判断log是否打开，以Log.VERBOSE为判断条件
     *
     * $: adb shell setprop log.tag.{propertyName} V
     *  关闭命令:
     * $: adb shell setprop log.tag.{propertyName} D
     */
    public static boolean isPropertyEnabled(String propertyName) {
        return Log.isLoggable(propertyName, Log.VERBOSE);
    }

    public static void v(String TAG, String msg){
        sLog.v(TAG, msg);
    }

    public static void d(String TAG, String msg) {
        sLog.d(TAG, msg);
    }

    public static void i(String TAG, String msg) {
        sLog.i(TAG, msg);
    }

    public static void w(String TAG, String msg) {
        sLog.w(TAG, msg);
    }

    public static void e(String TAG, String msg) {
        sLog.e(TAG, msg);
    }

    public static void e(String TAG, String msg, Throwable e) {
        sLog.e(TAG, msg, e);
    }

    public static class LogImpl implements ILog {

        @Override
        public void v(String TAG, String msg) {
            Log.v(TAG, msg);
        }

        @Override
        public void d(String TAG, String msg) {
            Log.d(TAG, msg);
        }

        @Override
        public void i(String TAG, String msg) {
            Log.i(TAG, msg);
        }

        @Override
        public void w(String TAG, String msg) {
            Log.w(TAG, msg);
        }

        @Override
        public void e(String TAG, String msg) {
            Log.e(TAG, msg);
        }

        @Override
        public void e(String TAG, String msg, Throwable e) {
            Log.e(TAG , msg, e);
        }
    }
}

