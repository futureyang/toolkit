package com.future.toolkit.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

import com.future.toolkit.R;

/**
 * Created by yangqc on 2019/10/29
 * 固定图片比例View
 * @Author yangqc
 */
public class StaticRatioImageView extends AppCompatImageView {

    private float ratio = 1;

    public StaticRatioImageView(Context context) {
        this(context, null);
    }

    public void setRatio(float ratio) {
        this.ratio = ratio;
    }

    public StaticRatioImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StaticRatioImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.StaticRatioImageView);
        ratio = typedArray.getFloat(R.styleable.StaticRatioImageView_ratio, ratio);
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec,heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = (int) (width * ratio);
        setMeasuredDimension(width, height);
    }
}
