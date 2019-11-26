package com.future.toolkit.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created by yangqc on 2019/9/25
 * 网络状态
 * @Author yangqc
 */
@SuppressLint("MissingPermission")
public class NetUtils {

    public static boolean isNetworkAvailable(Context context) {
        if (context == null) {
            return false;
        }
        try {
            ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {
                NetworkInfo info = connectivity.getActiveNetworkInfo();
                return info.isAvailable();
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    @Nullable
    public static NetworkInfo getActiveNetworkInfo(@NonNull Context cxt){
        ConnectivityManager cm = (ConnectivityManager) cxt.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm != null ? cm.getActiveNetworkInfo() : null;
    }

    /**
     * 就否已连接
     * @param cxt
     * @return
     */
    public static boolean isConnected(@NonNull Context cxt){
        NetworkInfo info = getActiveNetworkInfo(cxt);
        return info != null && info.isAvailable() && info.isConnected();
    }

    public static boolean isWiFi(@NonNull Context cxt) {
        NetworkInfo info = getActiveNetworkInfo(cxt);
        return info != null && info.getType() == ConnectivityManager.TYPE_WIFI;
    }

    public static boolean isMobile(@NonNull Context cxt){
        NetworkInfo info = getActiveNetworkInfo(cxt);
        return info != null && info.getType() == ConnectivityManager.TYPE_MOBILE;
    }
}
