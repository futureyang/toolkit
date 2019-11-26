package com.future.toolkit.utils;

import android.content.Context;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

import java.lang.reflect.Method;

/**
 * Created by yangqc on 2019/9/23
 *
 * @Author yangqc
 */
public class ToastUtils {

    private static Toast mToast;

    public static void showToast(Context context, String msg) {
        showShort(context, msg);
    }

    /**
     * 自定义时间显示
     * @param context
     * @param msg
     * @param duration
     */
    public static void show(@NonNull Context context, @NonNull CharSequence msg, int duration) {
        if(mToast != null){
            mToast.cancel();
            release();
        }
        Context ctx = context.getApplicationContext();
        Toast toast = Toast.makeText(ctx, msg, Toast.LENGTH_SHORT);
        toast.setDuration(duration);
        toast.show();
        mToast = toast;
    }

    public static void release(){
        mToast = null;
    }

    public static void show(@NonNull Context context, @StringRes int messageId, int duration){
        show(context, context.getResources().getString(messageId), duration);
    }

    public static void show(@NonNull Context context, @StringRes int messageId){
        showShort(context, messageId);
    }

    public static void show(@NonNull Context context, @NonNull CharSequence message){
        showShort(context, message);
    }

    /**
     * 短时间显示Toast
     */
    public static void showShort(@NonNull Context context, @NonNull CharSequence message) {
        show(context, message, Toast.LENGTH_SHORT);
    }

    /**
     * 短时间显示Toast
     */
    public static void showShort(@NonNull Context context, @StringRes int messageId) {
        showShort(context, context.getResources().getString(messageId));
    }

    /**
     * 长时间显示Toast
     */
    public static void showLong(@NonNull Context context, @NonNull CharSequence message) {
        show(context, message, Toast.LENGTH_LONG);
    }

    /**
     * 长时间显示Toast
     */
    public static void showLong(@NonNull Context context, @StringRes int messageId) {
        showLong(context, context.getResources().getString(messageId));
    }

    /**
     * 自定义Toast时使用，暂时用不到
     * @param toast
     * @return
     */
    @Nullable
    public static WindowManager.LayoutParams Toast_getWindowParams(@NonNull android.widget.Toast toast) {
        try {
            Method method = toast.getClass().getMethod("getWindowParams", new Class[0]);
            return (WindowManager.LayoutParams)method.invoke(toast, new Object[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

