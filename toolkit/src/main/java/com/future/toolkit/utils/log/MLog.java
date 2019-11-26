package com.future.toolkit.utils.log;

import android.util.Log;

import com.future.toolkit.BuildConfig;


/**
 * Created by yangqc on 2019/9/23
 * Log打印工具，log.d和log.i会被控制输出，log.w和log.e会一直输出
 * @Author yangqc
 */
public class MLog implements ILog {
    public static final String TAG = "SoPark";
    /**
     * release版下通过以下命令打印log
     * $: adb shell setprop log.tag.SoPark V
     * 关闭命令:
     * $: adb shell setprop log.tag.SoPark D
     */
    private static final boolean DEFAULT_LOG_STATUS = BuildConfig.DEBUG || isPropertyEnabled(TAG);
    private static boolean sLogStatus = DEFAULT_LOG_STATUS;

    public static boolean isPropertyEnabled(String propertyName) {
        return Log.isLoggable(propertyName, Log.VERBOSE);
    }

    public static void setDebug(boolean isDebug){
            sLogStatus =  isDebug;
    }

    public static boolean getDebugStatus(){
        return sLogStatus;
    }

    /**
     *
     */
    public static void initLogStatus() {
        sLogStatus = DEFAULT_LOG_STATUS;
    }


    /**
     * adb shell dumpsys activity com.mimikko.mimikkoui open/close
     *
     * @param command
     */

    public static void dumpLog(String[] command) {
        if (DEFAULT_LOG_STATUS || command == null || command.length == 0) {
            return;
        }
        if (command[0].contains("open")) {
            sLogStatus = true;
        } else if (command[0].contains("close")) {
            initLogStatus();
        }
    }


    public  void v(String tag, String msg) {
        if (sLogStatus) {
            Log.v(TAG, tag + " " + msg);
        }
    }

    public  void i(String msg) {
        if (sLogStatus) {
            Log.i(TAG, msg);
        }
    }

    public  void i(String tag, String msg) {
        Log.i(TAG, tag + " " + msg);
    }

    public  void d(String msg) {
        if (sLogStatus) {
            Log.d(TAG, msg);
        }
    }

    public  void d(String tag, String msg){
        if (sLogStatus) {
            Log.d(TAG, tag + " " +msg);
        }
    }

    public static void w(String msg) {
        Log.w(TAG, msg);
    }

    public  void w(String tag, String msg) {
        w(tag + " "+msg);
    }


    public void e(String msg) {
        Log.e(TAG, msg);
    }

    public void e(String tag, String msg) {
        e(tag + " " + msg);
    }

    public void e(String tag, String msg, Throwable e) {
        e(tag + " " + msg, e);
    }

    public void e(String msg, Throwable e) {
        Log.e(TAG, msg, e);
    }

    public static Throwable getStackTrace() {
        final StackTraceElement[] stackTrace = new Throwable().getStackTrace();
        Throwable e = new Throwable();
        e.setStackTrace(stackTrace);
        return e;
    }
}
