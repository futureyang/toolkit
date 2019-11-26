package com.future.toolkit.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.future.toolkit.R;

/**
 * Created by yangqc on 2019/8/9
 * 阴影背景布局
 * @Author yangqc
 */

public class ShadowLayout extends FrameLayout {

    public static final int ALL = 0x1111;

    public static final int LEFT = 0x0001;

    public static final int TOP = 0x0010;

    public static final int RIGHT = 0x0100;

    public static final int BOTTOM = 0x1000;

    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private Path mPath = new Path();

    private RectF mRectF = new RectF();
    /**
     * 背景颜色
     */
    private int mBackgroundColor;
    /**
     * 阴影颜色
     */
    private int mShadowColor;
    /**
     * 阴影大小范围
     */
    private float mShadowRadius;
    /**
     * 阴影 x 轴的偏移量
     */
    private float mShadowDx;
    /**
     * 阴影 y 轴的偏移量
     */
    private float mShadowDy;
    /**
     * 阴影显示的边界
     */
    private int mShadowSide;
    /**
     * 圆角半径
     */
    private float mRadius;
    /**
     * 圆角左上角半径
     */
    private float mTopLeftRadius;
    /**
     * 圆角右上角半径
     */
    private float mTopRightRadius;
    /**
     * 圆角左下角半径
     */
    private float mBottomLeftRadius;
    /**
     * 圆角右下角半径
     */
    private float mBottomRightRadius;
    /**
     * 圆角四角半径数据
     */
    private float[] mRadii = new float[8];

    public ShadowLayout(Context context) {
        this(context, null);
    }

    public ShadowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShadowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
        setUpShadowPaint();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int shadowOffset = getShadowOffset();
        mRectF.left = (mShadowSide & LEFT) == LEFT ? shadowOffset : 0;
        mRectF.top = (mShadowSide & TOP) == TOP ? shadowOffset : 0;
        mRectF.right = this.getMeasuredWidth() - ((mShadowSide & RIGHT) == RIGHT ? shadowOffset : 0);
        mRectF.bottom = this.getMeasuredHeight() - ((mShadowSide & BOTTOM) == BOTTOM ? shadowOffset : 0);
        int paddingLeft = (mShadowSide & LEFT) == LEFT ? shadowOffset : 0;
        int paddingTop = (mShadowSide & TOP) == TOP ? shadowOffset : 0;
        int paddingRight = (mShadowSide & RIGHT) == RIGHT ? shadowOffset : 0;
        int paddingBottom = (mShadowSide & BOTTOM) == BOTTOM ? shadowOffset : 0;
        this.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mRadii[0] = mRadii[1] = mTopLeftRadius;
        mRadii[2] = mRadii[3] = mTopRightRadius;
        mRadii[4] = mRadii[5] = mBottomRightRadius;
        mRadii[6] = mRadii[7] = mBottomLeftRadius;

        mPath.addRoundRect(mRectF, mRadii, Path.Direction.CW);
        canvas.drawPath(mPath, mPaint);
    }

    private void init(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ShadowLayout);
        mShadowColor = typedArray.getColor(R.styleable.ShadowLayout_shadowColor, Color.TRANSPARENT);
        mShadowRadius = typedArray.getDimension(R.styleable.ShadowLayout_shadowRadius, 0);
        mShadowDx = typedArray.getDimension(R.styleable.ShadowLayout_shadowDx, 0);
        mShadowDy = typedArray.getDimension(R.styleable.ShadowLayout_shadowDy, 0);
        mShadowSide = typedArray.getInt(R.styleable.ShadowLayout_shadowSide, ALL);
        mBackgroundColor = typedArray.getColor(R.styleable.ShadowLayout_backgroundColor, mShadowColor);
        mRadius = typedArray.getDimension(R.styleable.ShadowLayout_radius,0);
        mTopLeftRadius = typedArray.getDimension(R.styleable.ShadowLayout_topLeftRadius, mRadius);
        mTopRightRadius = typedArray.getDimension(R.styleable.ShadowLayout_topRightRadius, mRadius);
        mBottomRightRadius = typedArray.getDimension(R.styleable.ShadowLayout_bottomRightRadius, mRadius);
        mBottomLeftRadius = typedArray.getDimension(R.styleable.ShadowLayout_bottomLeftRadius, mRadius);
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        this.setWillNotDraw(false);
        typedArray.recycle();
    }

    private void setUpShadowPaint() {
        mPaint.reset();
        mPaint.setFilterBitmap(true);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(mBackgroundColor);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setShadowLayer(mShadowRadius, mShadowDx, mShadowDy, mShadowColor);
    }

    private int getShadowOffset() {
        return 0 >= mShadowRadius ? 0 : (int) (Math.max(mShadowDx, mShadowDy) + mShadowRadius);
    }

    public void setBackgroundColor(int backgroundColor) {
        mBackgroundColor = backgroundColor;
    }

    public void setBackgroundColorId(int backgroundColorId) {
        mBackgroundColor = getResources().getColor(backgroundColorId);
    }

    public void setShadowColor(int shadowColor) {
        mShadowColor = shadowColor;
        mBackgroundColor = shadowColor;
        invalidate();
    }

    public void setShadowColorId(int shadowColorId) {
        mShadowColor = getResources().getColor(shadowColorId);
        mBackgroundColor = getResources().getColor(shadowColorId);
        invalidate();
    }

    public void setShadowRadius(float shadowRadius) {
        mShadowRadius = shadowRadius;
        invalidate();
    }

    public void setRadius(float radius) {
        mTopLeftRadius = radius;
        mTopRightRadius = radius;
        mBottomRightRadius = radius;
        mBottomLeftRadius = radius;
        invalidate();
    }

    public void setRadius(float topLeftRadius, float topRightRadius, float bottomRightRadius, float bottomLeftRadius) {
        mTopLeftRadius = topLeftRadius;
        mTopRightRadius = topRightRadius;
        mBottomRightRadius = bottomRightRadius;
        mBottomLeftRadius = bottomLeftRadius;
        invalidate();
    }
}
