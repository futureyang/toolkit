package com.future.toolkit.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.future.toolkit.utils.log.LogUtils;

import java.util.List;

/**
 * Created by yangqc on 2019/9/23
 * 进程信息
 * @Author yangqc
 */
public class ProcessInfoUtils {
    private static final String TAG = "ProcessInfoUtils";

    private static String sCurrentProgressName = null;
    private static Boolean sIsMainProcess = null;

    /**
     * 获取进程名称，通过ActivityManager
     *
     * @param context
     * @return
     */
    public static String getCurrentProcessName(Context context) {
        if (sCurrentProgressName != null){
            return sCurrentProgressName;
        }
        String currentProgressName = "";
        int currentPid = android.os.Process.myPid();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> processInfos = activityManager != null ?
                activityManager.getRunningAppProcesses() : null;
        if (processInfos == null){
            return null;
        }
        for (ActivityManager.RunningAppProcessInfo appProcessInfo : processInfos) {
            if (appProcessInfo.pid == currentPid) {
                currentProgressName = appProcessInfo.processName;
                LogUtils.d(TAG, "getCurrentProcessName="+currentProgressName);
                sCurrentProgressName = currentProgressName;
                return currentProgressName;
            }
        }
        return currentProgressName;
    }

    /**
     * 根据context与进程后缀名获取进程名
     *
     * @param context
     * @param processSuffix
     * @return
     */
    public static String getProcessName(Context context, String processSuffix) {
        if (context == null) {
            return "";
        }
        return context.getPackageName() + processSuffix;
    }

    public static boolean isProcess(@NonNull Context context, String progressName){
        String currentProcess = getCurrentProcessName(context);
        return TextUtils.equals(currentProcess, context.getPackageName() + progressName);
    }

    /**
     * 判断是否是主进程
     * @param context
     * @return
     */
    public static boolean isMainProcess(@NonNull Context context){
        if (sIsMainProcess == null){
            String currentProcess = getCurrentProcessName(context);
            sIsMainProcess = currentProcess != null ?
                    TextUtils.equals(currentProcess, context.getPackageName()) : null;
        }
        return sIsMainProcess != null ? sIsMainProcess : true;
    }
}

