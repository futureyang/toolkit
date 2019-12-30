package com.future.toolkit.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.future.toolkit.R;

/**
 * Created by yangqc on 2019/11/8
 *
 * @Author yangqc
 */
public class MaxFrameLayout extends FrameLayout {

    private int mMaxHeight = -1;

    public MaxFrameLayout(@NonNull Context context) {
        super(context);
        init(null);
    }

    public MaxFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public MaxFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        if (attrs == null) {
            return;
        }
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.MaxFrameLayout);
        mMaxHeight = ta.getDimensionPixelSize(R.styleable.MaxListView_maxHeight, mMaxHeight);
        ta.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = MeasureSpec.getSize(heightMeasureSpec);
        if (mMaxHeight > 0 && height > mMaxHeight) {
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(mMaxHeight, MeasureSpec.AT_MOST);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


    public void setMaxHeight(int maxHeight){
        this.mMaxHeight = maxHeight;
    }
}

