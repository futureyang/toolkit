package com.future.toolkit.utils;

import com.future.toolkit.utils.log.LogUtils;

import java.io.Closeable;

/**
 * Created by yangqc on 2019/9/23
 *
 * @Author yangqc
 */
public class IoUtils {

    private static final String TAG = "IoUtils";

    /**
     * 关闭流
     * @param c
     */
    public static void closeSilently(Closeable c) {
        if (c != null) {
            try {
                c.close();
            } catch (Exception e) {
                LogUtils.e(TAG, "closeSilently", e);
            }
        }
    }
}
