package com.future.toolkit.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.future.toolkit.utils.log.LogUtils;

import static android.content.ContentValues.TAG;

/**
 * Created by yangqc on 2019/9/23
 *
 * @Author yangqc
 */
public class AppUtils {

    public static String APPLICATION_ID = "";

    public static boolean IS_DEVELOP = false;
    public static boolean IS_DEBUG = false;

    /**
     * @param context
     * @param pkg
     * @return
     */
    public static boolean isAppInstalled(@NonNull Context context, @Nullable String pkg) {
        try {
            context.getPackageManager().getApplicationInfo(pkg, 0);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 获取versioncpde
     *
     * @param context
     * @param pkg
     * @return
     */
    public static int getAppVersionCode(@NonNull Context context, @Nullable String pkg) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(pkg, 0);
            return info.versionCode;
        } catch (Exception e) {
            return 0;
        }
    }


    public static String getAppVersionName(Context context) {
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pi.versionName;
        } catch (Exception e) {
            LogUtils.e(TAG, "getAppVersionCode", e);
            return "";
        }
    }

    /**
     * 是否系统应用
     *
     * @param context
     * @param packageName
     * @return
     */
    public static boolean isSystemApp(Context context, String packageName) {
        boolean isSys = false;
        PackageManager pm = context.getPackageManager();
        try {
            ApplicationInfo applicationInfo = pm.getApplicationInfo(packageName, 0);
            if (applicationInfo != null && (applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) > 0) {
                isSys = true;
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            isSys = false;
        }
        return isSys;
    }

    public static boolean isDevelop() {
        return IS_DEVELOP;
    }

    public static void killLauncher() {
        System.exit(0);
    }

    /**
     * get string data
     *
     * @param context  application context
     * @param metaName define in manifest
     * @return the value of metaName
     */
    public static String getMetaDataValue(@NonNull Context context, String metaName) {
        String metaValue = "";
        try {
            Context appContext = context.getApplicationContext();
            metaValue = appContext.getPackageManager()
                    .getApplicationInfo(appContext.getPackageName(), PackageManager.GET_META_DATA)
                    .metaData.getString(metaName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return metaValue;
    }

    /**
     * 实现文本复制功能
     *
     * @param content
     */
    public static boolean copyToClipboard(@NonNull Context context, String content) {
        // 得到剪贴板管理器
        ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        if (cmb != null){
            cmb.setPrimaryClip(ClipData.newPlainText(null, content));
            return true;
        }
        return false;
    }

    /**
     * 实现粘贴功能
     *
     * @param context
     * @return
     */
    public static String pasteFromClipboard(@NonNull Context context) {
        // 得到剪贴板管理器
        ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        if (cmb != null){
            ClipData abc = cmb.getPrimaryClip();
            ClipData.Item item = abc.getItemAt(0);
            return item.getText().toString();
        }
        return "";
    }


    /**
     * 是否为竖屏
     * @param context
     * @return
     */
    public static boolean isPortrait(Context context){
        return isPortrait(context.getResources().getConfiguration());
    }

    public static boolean isPortrait(Configuration newConfig){
        return newConfig.orientation == Configuration.ORIENTATION_PORTRAIT;
    }
}
