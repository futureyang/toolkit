package com.future.toolkit.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ListView;

import com.future.toolkit.R;

/**
 * Created by yangqc on 2019/11/8
 *
 * @Author yangqc
 */
public class MaxListView extends ListView {
    private int maxHeight = -1;

    public MaxListView(Context context) {
        super(context);
        init(null);
    }

    public MaxListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public MaxListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        if (attrs == null) {
            return;
        }
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.MaxListView);
        maxHeight = ta.getDimensionPixelSize(R.styleable.MaxListView_maxHeight, maxHeight);
        ta.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = MeasureSpec.getSize(heightMeasureSpec);
        if (maxHeight > 0 && height > maxHeight) {
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(maxHeight, MeasureSpec.AT_MOST);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


    public void setMaxHeight(int maxHeight){
        this.maxHeight = maxHeight;
    }
}
