package com.future.toolkit.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;

import androidx.annotation.AttrRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;

import com.future.toolkit.R;

/**
 * Created by yangqc on 2019/9/26
 *
 * @Author yangqc
 */
public class AppCompatUtils {

    private static final int[] APPCOMPAT_CHECK_ATTRS = {R.attr.colorPrimary};

    public static final int INVALID_ID = 0;
    private static final int[] TEMP_ARRAY = new int[1];

    /**
     * 获取图片，支持vector
     * activity若继承自AppCompatActivity，则可直接使用ContextCompat.getDrawable
     * AppCompatResources.getDrawable是在Api24以下都会使用兼容包中的vectorDrawable，故使用该方法
     *
     * @param context
     * @param idRes
     * @return
     */
    public static Drawable getDrawable(@NonNull Context context, @DrawableRes int idRes) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            return ContextCompat.getDrawable(context, idRes);
        } else {
            return AppCompatResources.getDrawable(context, idRes);
        }
    }

    public static void checkAppCompatTheme(Context context) {
        TypedArray a = context.obtainStyledAttributes(APPCOMPAT_CHECK_ATTRS);
        final boolean failed = !a.hasValue(0);
        a.recycle();
        if (failed) {
            throw new IllegalArgumentException("You need to use ic_asss Theme.AppCompat theme "
                    + "(or descendant) with the design library.");
        }
    }

    public static int getThemeAttrResId(@NonNull Context context, int[] attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs);
        final int resId = a.getResourceId(0, INVALID_ID);
        a.recycle();
        return resId;
    }

    public static int getThemeAttrResId(@NonNull Context context, @AttrRes int attr){
        TEMP_ARRAY[0] = attr;
        return getThemeAttrResId(context, TEMP_ARRAY);
    }

    public static int getThemeAttrColor(@NonNull Context context, @AttrRes int attr){
        int resId = getThemeAttrResId(context, attr);
        return ContextCompat.getColor(context, resId);
    }

    public static Drawable getThemeAttrDrawable(@NonNull Context context, @AttrRes int attr){
        int resId = getThemeAttrResId(context, attr);
        return AppCompatUtils.getDrawable(context, resId);
    }
}
