package com.future.toolkit.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.viewpager.widget.ViewPager;

/**
 * Created by yangqc on 2019/10/29
 * 可禁止滑动的viewpaper, 默认禁止滑动
 * @Author yangqc
 */
public class NoScrollableViewPager extends ViewPager {
    private boolean isScrollable = false;

    public NoScrollableViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NoScrollableViewPager(Context context) {
        super(context);
    }

    /**
     * 1.dispatchTouchEvent一般情况不做处理
     * ,如果修改了默认的返回值,子孩子都无法收到事件
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 是否拦截
     * 拦截:会走到自己的onTouchEvent方法里面来
     * 不拦截:事件传递给子孩子
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (isScrollable) {
            return super.onInterceptTouchEvent(ev);
        } else {
            return false;
        }
    }

    /**
     * 是否消费事件
     * 消费:事件就结束
     * 不消费:往父控件传
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (isScrollable) {
            return super.onTouchEvent(ev);
        } else {
            return true;// 可行,消费,拦截事件
        }
    }

    public boolean isScrollable(){
        return isScrollable;
    }

    public void setIsScrollable(boolean scroll) {
        isScrollable = scroll;
    }
}
