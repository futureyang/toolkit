package com.future.toolkit.utils.bar;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.lang.reflect.Field;
import java.lang.reflect.Method;


public final class StateAppBar {

    /**
     * 设置状态栏颜色
     * @param activity                      activity
     * @param statusColor                   颜色
     */
    public static void setStatusBarColor(Activity activity, @ColorInt int statusColor) {
        checkNull(activity);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //21
            BarStatusLollipop.setStatusBarColor(activity, statusColor);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //19
            BarStatusKitKat.setStatusBarColor(activity, statusColor);
        }
    }

    /**
     * 设置透明状态栏
     * @param activity                      activity
     */
    public static void translucentStatusBar(Activity activity) {
        checkNull(activity);
        translucentStatusBar(activity, false);
    }

    /**
     * 设置透明状态栏
     * @param activity                      activity
     * @param hideStatusBarBackground       是否隐藏状态栏
     */
    public static void translucentStatusBar(Activity activity, boolean hideStatusBarBackground) {
        checkNull(activity);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            BarStatusLollipop.translucentStatusBar(activity, hideStatusBarBackground);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            BarStatusKitKat.translucentStatusBar(activity);
        }
    }

    /**
     * 设置AppBarLayout折叠布局的状态栏颜色
     * @param activity                      activity
     * @param appBarLayout                  appBar
     * @param collapsingToolbarLayout       collapsingToolbarLayout
     * @param toolbar                       toolbar
     * @param statusColor                   颜色
     */
    public static void setStatusBarColorForCollapsingToolbar(
            @NonNull Activity activity, AppBarLayout appBarLayout,
            CollapsingToolbarLayout collapsingToolbarLayout,
            Toolbar toolbar, @ColorInt int statusColor) {
        checkNull(activity);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            BarStatusLollipop.setStatusBarColorForCollapsingToolbar(activity,
                    appBarLayout, collapsingToolbarLayout, toolbar, statusColor);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            BarStatusKitKat.setStatusBarColorForCollapsingToolbar(activity,
                    appBarLayout, collapsingToolbarLayout, toolbar, statusColor);
        }
    }

    @SuppressLint("NewApi")
    public static void setStatusBarLightMode(Activity activity, @ColorInt int color) {
        checkNull(activity);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //判断是否为小米或魅族手机，如果是则将状态栏文字改为黑色
            if (setStatusBarLightMode(activity, true) ||
                    FlymeSetStatusBarLightMode(activity, true)) {
                //设置状态栏为指定颜色
                //5.0
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    activity.getWindow().setStatusBarColor(color);
                    //4.4
                } else {
                    //调用修改状态栏颜色的方法
                    setStatusBarColor(activity, color);
                }
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                //如果是6.0以上将状态栏文字改为黑色，并设置状态栏颜色
                activity.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                activity.getWindow().getDecorView().
                        setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                                View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                activity.getWindow().setStatusBarColor(color);

                //fitsSystemWindow 为 false, 不预留系统栏位置.
                ViewGroup mContentView = (ViewGroup) activity.getWindow()
                        .findViewById(Window.ID_ANDROID_CONTENT);
                View mChildView = mContentView.getChildAt(0);
                if (mChildView != null) {
                    mChildView.setFitsSystemWindows(true);
                    ViewCompat.requestApplyInsets(mChildView);
                }
            }
        }
    }


    public static void setStatusBarLightForCollapsingToolbar(
            Activity activity, AppBarLayout appBarLayout,
            CollapsingToolbarLayout collapsingToolbarLayout,
            Toolbar toolbar, @ColorInt int statusBarColor) {
        checkNull(activity);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            BarStatusLollipop.setStatusBarWhiteForCollapsingToolbar(activity,
                    appBarLayout, collapsingToolbarLayout, toolbar, statusBarColor);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            BarStatusKitKat.setStatusBarWhiteForCollapsingToolbar(activity,
                    appBarLayout, collapsingToolbarLayout, toolbar, statusBarColor);
        }
    }


    /**
     * MIUI的沉浸支持透明白色字体和透明黑色字体
     * https://dev.mi.com/console/doc/detail?pId=1159
     */
    public static boolean setStatusBarLightMode(Activity activity, boolean darkmode) {
        checkNull(activity);
        try {
            @SuppressLint("PrivateApi")
            Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
            Window window = activity.getWindow();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            }
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }

            Class<? extends Window> clazz = activity.getWindow().getClass();
            Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
            int darkModeFlag = field.getInt(layoutParams);
            Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
            extraFlagField.invoke(activity.getWindow(), darkmode ? darkModeFlag : 0, darkModeFlag);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 设置状态栏图标为深色和魅族特定的文字风格，Flyme4.0以上
     */
    public static boolean FlymeSetStatusBarLightMode(Activity activity, boolean darkmode) {
        checkNull(activity);
        try {
            WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
            Field darkFlag = WindowManager.LayoutParams.class
                    .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
            Field meizuFlags = WindowManager.LayoutParams.class.getDeclaredField("meizuFlags");
            darkFlag.setAccessible(true);
            meizuFlags.setAccessible(true);
            int bit = darkFlag.getInt(null);
            int value = meizuFlags.getInt(lp);
            if (darkmode) {
                value |= bit;
            } else {
                value &= ~bit;
            }
            meizuFlags.setInt(lp, value);
            activity.getWindow().setAttributes(lp);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    static void setContentTopPadding(Activity activity, int padding) {
        checkNull(activity);
        ViewGroup mContentView = activity.getWindow().findViewById(Window.ID_ANDROID_CONTENT);
        mContentView.setPadding(0, padding, 0, 0);
    }

    static int getPxFromDp(Context context, float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dp, context.getResources().getDisplayMetrics());
    }

    private static void checkNull(Object object){
        if (object == null){
            throw new NullPointerException("object is not null");
        }
    }
}
