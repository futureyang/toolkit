package com.future.toolkit.utils.log;

/**
 * Created by yangqc on 2019/9/23
 *
 * @Author yangqc
 */
public interface ILog {
    void v(String TAG, String msg);
    void d(String TAG, String msg);
    void i(String TAG, String msg);
    void w(String TAG, String msg);
    void e(String TAG, String msg);
    void e(String TAG, String msg, Throwable e);
}
