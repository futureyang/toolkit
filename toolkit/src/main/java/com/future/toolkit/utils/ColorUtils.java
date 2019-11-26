package com.future.toolkit.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;

import androidx.annotation.AttrRes;
import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.FloatRange;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;


/**
 * Created by yangqc on 2019/9/23
 *
 * @Author yangqc
 */
public class ColorUtils {

    @ColorInt
    public static final int SUB_COLOR = 0X00141E00;

    private static final int ENABLE_ATTR = android.R.attr.state_enabled;
    private static final int CHECKED_ATTR = android.R.attr.state_checked;
    private static final int PRESSED_ATTR = android.R.attr.state_pressed;

    private static final int GRAY_COLOR_MIDDLE = 180;
    /**
     * 计算按压色
     * @param color
     * @return
     */
    public static int computeDarkColor(@ColorInt int color){
        return color - SUB_COLOR;
    }

    @NonNull
    public static ColorStateList createColorStateList(@AttrRes int state,
                                                      @ColorInt int negativeStateColor,
                                                      @ColorInt int positiveStateColor) {
        int[][] states = new int[2][];
        states[0] = new int[]{-state};
        states[1] = new int[]{state};
        int[] colors = new int[]{negativeStateColor, positiveStateColor};
        return new ColorStateList(states, colors);
    }

    public static int getColor(@NonNull Context context, @ColorRes int colorId){
        return context.getResources().getColor(colorId);
    }

    @ColorInt
    public static int setAlphaComponent(@ColorInt int color, @IntRange(from = 0L,to = 255L) int alpha) {
        alpha = MathUtils.boundToRange(alpha, 0, 255);
        return color & 0xFFFFFF | alpha << 24;

    }

    @ColorInt
    public static int setAlphaComponent(@ColorInt int color, @FloatRange(from = 0f,to = 1f) float alpha) {
        alpha = MathUtils.boundToRange(alpha, 0f, 1f);
        return color & 0xFFFFFF | (int)(alpha * 0xFF) << 24;
    }


    public static ColorStateList generateThumbColorWithTintColor(final int tintColor) {
        int[][] states = new int[][]{
                {-ENABLE_ATTR, CHECKED_ATTR},
                {-ENABLE_ATTR},
                {PRESSED_ATTR, -CHECKED_ATTR},
                {PRESSED_ATTR, CHECKED_ATTR},
                {CHECKED_ATTR},
                {-CHECKED_ATTR}
        };

        int[] colors = new int[]{
                tintColor - 0xAA000000,
                0xFFBABABA,
                tintColor - 0x99000000,
                tintColor - 0x99000000,
                tintColor | 0xFF000000,
                0xFFEEEEEE
        };
        return new ColorStateList(states, colors);
    }

    public static ColorStateList generateBackColorWithTintColor(final int tintColor) {
        int[][] states = new int[][]{
                {-ENABLE_ATTR, CHECKED_ATTR},
                {-ENABLE_ATTR},
                {CHECKED_ATTR, PRESSED_ATTR},
                {-CHECKED_ATTR, PRESSED_ATTR},
                {CHECKED_ATTR},
                {-CHECKED_ATTR}
        };

        int[] colors = new int[]{
                tintColor - 0xE1000000,
                0x10000000,
                tintColor - 0xD0000000,
                0x20000000,
                tintColor - 0xD0000000,
                0x20000000
        };
        return new ColorStateList(states, colors);
    }

    public static int getGrayColor(int color){
        return  (int) (Color.red(color) * 0.3 + Color.green(color) * 0.59 + Color.blue(color) * 0.11);
    }

    /**
     * 判断是不是深颜色
     * @return
     */
    public static boolean isWhite(int color){
        int grayLevel = getGrayColor(color);
        return grayLevel >= GRAY_COLOR_MIDDLE;
    }
}
