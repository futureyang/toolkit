package com.future.toolkit.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

/**
 * Created by yangqc on 2019/10/29
 * 处理与ViewPager处理的touche事件
 * @Author yangqc
 */
public class FixSwipeRefreshLayout extends SwipeRefreshLayout {

    private int mTouchSlop;
    private int mLastX;
    private int mLastY;

    private boolean mToFixTouch = false;

    public FixSwipeRefreshLayout(@NonNull Context context) {
        this(context, null);
    }

    public FixSwipeRefreshLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        final ViewConfiguration vc = ViewConfiguration.get(context.getApplicationContext());
        mTouchSlop = vc.getScaledTouchSlop();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            if (getChildAt(i) instanceof ViewPager){
                mToFixTouch = true;
            }
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (!mToFixTouch){
            return super.onInterceptTouchEvent(ev);
        }
        boolean intercept = true;
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN: {
                mLastX = (int) ev.getX();
                mLastY = (int) ev.getY();
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                final float x = ev.getX();
                final float xDiff = Math.abs(x - mLastX);
                final float y = ev.getY();
                final float yDiff = Math.abs(y - mLastY);
                if (xDiff > mTouchSlop && xDiff * 5 >= yDiff) {
                    //当x轴上移动的距离大于y轴的移动距离时，不拦截
                    intercept = false;
                }
                break;
            }
            default:break;
        }
        return intercept && super.onInterceptTouchEvent(ev);
    }
}
