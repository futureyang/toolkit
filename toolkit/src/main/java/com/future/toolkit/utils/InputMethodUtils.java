package com.future.toolkit.utils;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;

import com.future.toolkit.utils.log.LogUtils;

/**
 * Created by yangqc on 2019/9/23
 * 输入法
 * @Author yangqc
 */
public class InputMethodUtils {

    private static final String TAG = "InputMethodUtils";


    public static InputMethodManager get(@NonNull Context context) {
        return (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    /**
     * 显示输入法
     *
     * @param view
     */
    public static void show(@NonNull View view) {
        try {
            InputMethodManager imm = get(view.getContext());
            if (imm != null) {
                imm.showSoftInput(view, 0);
            }
        } catch (Exception e) {
            LogUtils.e(TAG, "show ", e);
        }

    }

    /**
     * 隐藏输入法
     *
     * @param view
     */
    public static void hide(@NonNull View view) {
        try {
            final InputMethodManager imm = get(view.getContext());
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        } catch (Exception e) {
            LogUtils.e(TAG, "hide : ", e);
        }
    }

    /**
     * 输入法是否打开
     * @param context
     * @return
     */
    public static boolean isOpen(@NonNull Context context) {
        final InputMethodManager imm = get(context);
        return imm != null && imm.isActive();
    }
}

