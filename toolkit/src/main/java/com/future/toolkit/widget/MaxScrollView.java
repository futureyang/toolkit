package com.future.toolkit.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;

import com.future.toolkit.R;

/**
 * Created by yangqc on 2019/11/8
 *
 * @Author yangqc
 */
public class MaxScrollView extends NestedScrollView {
    private int mMaxHeight = -1;

    public MaxScrollView(@NonNull Context context) {
        super(context);
        init(null);
    }

    public MaxScrollView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public MaxScrollView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        if (attrs == null) {
            return;
        }
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.MaxScrollView);
        mMaxHeight = ta.getDimensionPixelSize(R.styleable.MaxScrollView_maxHeight, mMaxHeight);
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
