package com.future.toolkit.utils;

import android.content.Context;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import androidx.annotation.NonNull;

/**
 * Created by yangqc on 2019/11/26
 * 获取屏幕信息
 * @Author yangqc
 */
public class ScreenInfoUtil {

    public static final float DEFAULT_LARGE_RATIO = 2.0F;

    private static final Point SCREEN_SIZE = new Point();

    public static Point getScreenSize(@NonNull Context context){
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point point = SCREEN_SIZE;

        DisplayMetrics dm = new DisplayMetrics();
        display.getRealMetrics(dm);
        point.set(dm.widthPixels, dm .heightPixels);
        return point;
    }

    public static Point getScreenSizeAbs(@NonNull Context context){
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point point = SCREEN_SIZE;

        DisplayMetrics dm = new DisplayMetrics();
        display.getRealMetrics(dm);
        point.x = Math.min(dm.widthPixels, dm.heightPixels);
        point.y = Math.max(dm.widthPixels, dm.heightPixels);
        return point;
    }


    public static int getScreenWidth(Context context) {
        return getScreenSize(context).x;
    }

    public static int getScreenHeight(Context context) {
        return getScreenSize(context).y;
    }


    public static int getScreenCenterX(Context context){
        return getScreenWidth(context) / 2;
    }

    public static int getScreenCenterY(Context context){
        return getScreenHeight(context) / 2;
    }

    public static float getScreenRatio(@NonNull Context context){
        Point size = getScreenSizeAbs(context);
        return size.y / (float)size.x;
    }

    public static boolean isLargeRatio(@NonNull Context context){
        return getScreenRatio(context) >= DEFAULT_LARGE_RATIO;
    }
}
