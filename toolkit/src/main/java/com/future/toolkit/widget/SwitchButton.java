package com.future.toolkit.widget;

import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Checkable;

import androidx.annotation.ColorInt;

import com.future.toolkit.R;

/**
 * Created by yangqc on 2019/10/29
 * 开关按钮
 * @Author yangqc
 */
public class SwitchButton extends View implements Checkable {
    private static final String TAG = "SwitchButton";
    private static final int DEFAULT_WIDTH = dp2pxInt(48);
    private static final int DEFAULT_HEIGHT = dp2pxInt(30);
    private static final int ANIMATE_DURATION = 300;
    private static final int SHADOWLAYER = 0x66000000;
    private static final int ENABLED_COLOR = 0x66000000;

    private int mShadowRadius;
    private int mShadowOffset;
    private int mShadowColor;
    private boolean mShadowEffect;

    private float mRadius;
    private float mHeight;
    private float mWidth;
    private float mLeft;
    private float mTop;
    private float mRight;
    private float mBottom;
    private float mCenterX;
    private float mCenterY;

    private Paint mBackgroundPaint;
    private int mBackgroundUncheckColor;
    private int mBackgroundCheckedColor;

    private Paint mButtonPaint;
    private float mButtonMinX;
    private float mButtonMaxX;
    private int mButtonCheckColor;
    private int mButtonUncheckColor;


    private ViewState mViewState;
    private ViewState mEnabledState;

    private RectF mRect = new RectF();

    private ValueAnimator mAnimator;

    private boolean mChecked;
    private boolean mEnableAnimate;
    private boolean mUiInited = false;

    private long mTouchTime = 0L;

    private final android.animation.ArgbEvaluator mArgbEvaluator
            = new android.animation.ArgbEvaluator();

    private OnCheckedChangeListener mOnCheckedChangeListener;

    private ValueAnimator.AnimatorUpdateListener mAnimatorUpdateListener
            = new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            float value = (Float) animation.getAnimatedValue();
            float targetButtonX = isChecked() ? mButtonMaxX : mButtonMinX;
            float currentButtonX = isChecked() ? mButtonMinX : mButtonMaxX;
            mViewState.buttonX = currentButtonX + (targetButtonX - currentButtonX) * value;

            float fraction = (mViewState.buttonX - mButtonMinX) / (mButtonMaxX - mButtonMinX);

            mViewState.radius = fraction * mRadius;

            mViewState.backgroundStateColor = (int) mArgbEvaluator.evaluate(
                    fraction, mBackgroundUncheckColor, mBackgroundCheckedColor);
            mViewState.buttonStateColor = (int) mArgbEvaluator.evaluate(
                    fraction, mButtonUncheckColor, mButtonCheckColor);
            postInvalidate();
        }
    };


    public interface OnCheckedChangeListener {
        void onCheckedChanged(SwitchButton view, boolean isChecked);
    }

    public SwitchButton(Context context) {
        super(context);
        init(context, null);
    }

    public SwitchButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public SwitchButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SwitchButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    @Override
    public final void setPadding(int left, int top, int right, int bottom) {
        super.setPadding(0, 0, 0, 0);
    }

    /**
     * 初始化参数
     */
    private void init(Context context, AttributeSet attrs) {

        TypedArray typedArray = null;
        if (attrs != null) {
            typedArray = context.obtainStyledAttributes(attrs, R.styleable.SwitchButton);
        }

        mShadowEffect = typedArray.getBoolean(R.styleable.SwitchButton_sb_shadow_effect, true);

        mShadowRadius = typedArray.getDimensionPixelOffset(R.styleable.SwitchButton_sb_shadow_radius, dp2pxInt(2.5f));

        mShadowOffset = typedArray.getDimensionPixelOffset(R.styleable.SwitchButton_sb_shadow_offset, dp2pxInt(1.5f));

        mShadowColor = typedArray.getColor(R.styleable.SwitchButton_sb_shadow_color, 0);

        mBackgroundCheckedColor = typedArray.getColor(R.styleable.SwitchButton_sb_back_checked_color,
                getContext().getResources().getColor(R.color.switch_back_checked_color));

        mBackgroundUncheckColor = typedArray.getColor(R.styleable.SwitchButton_sb_back_uncheck_color,
                getContext().getResources().getColor(R.color.switch_back_unchecked_color));

        mButtonCheckColor = typedArray.getColor(R.styleable.SwitchButton_sb_button_check_color,
                getContext().getResources().getColor(R.color.switch_button_checked_color));

        mButtonUncheckColor = typedArray.getColor(R.styleable.SwitchButton_sb_button_uncheck_color,
                getContext().getResources().getColor(R.color.switch_button_unchecked_color));

        int effectDuration = typedArray.getInt(R.styleable.SwitchButton_sb_effect_duration, ANIMATE_DURATION);

        mChecked = typedArray.getBoolean(R.styleable.SwitchButton_sb_checked, false);

        mEnableAnimate = typedArray.getBoolean(R.styleable.SwitchButton_sb_enable_Animate, true);

        if (typedArray != null) {
            typedArray.recycle();
        }
        mBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mButtonPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        mViewState = new ViewState();
        mEnabledState = new ViewState();

        mAnimator = ValueAnimator.ofFloat(0f, 1f);
        mAnimator.setDuration(effectDuration);
        mAnimator.setRepeatCount(0);

        mAnimator.addUpdateListener(mAnimatorUpdateListener);

        this.setPadding(0, 0, 0, 0);

        setLayerType(LAYER_TYPE_SOFTWARE, null);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        if (widthMode == MeasureSpec.UNSPECIFIED
                || widthMode == MeasureSpec.AT_MOST) {
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(DEFAULT_WIDTH, MeasureSpec.EXACTLY);
        }
        if (heightMode == MeasureSpec.UNSPECIFIED
                || heightMode == MeasureSpec.AT_MOST) {
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(DEFAULT_HEIGHT, MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        float viewPadding = mShadowRadius + mShadowOffset;

        mHeight = h - (viewPadding * 2);
        mWidth = w - (viewPadding * 2);

        mRadius = mHeight * .5f;

        mLeft = viewPadding;
        mTop = viewPadding;
        mRight = w - viewPadding;
        mBottom = h - viewPadding;

        mCenterX = (mLeft + mRight) * .5f;
        mCenterY = (mTop + mBottom) * .5f;

        mButtonMinX = mLeft + mRadius;
        mButtonMaxX = mRight - mRadius;

        mUiInited = true;
        setViewState(isChecked());
        postInvalidate();
    }

    @Override
    public final boolean onTouchEvent(MotionEvent event) {
        long timeMillis = System.currentTimeMillis();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mTouchTime = timeMillis;
                break;
            case MotionEvent.ACTION_UP:
                if (timeMillis - mTouchTime <= 300 && (isClickable() || isEnabled())) {
                    toggle();
                    return true;
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (!isEnabled()) {
            mEnabledState.radius = mRadius;
            mEnabledState.buttonX = mViewState.buttonX;
            mEnabledState.buttonStateColor = mButtonUncheckColor;
            mEnabledState.backgroundStateColor = mBackgroundUncheckColor;
        }
        setAlpha(isEnabled() ? 1.0f : 0.6f);
        onStartDraw(canvas, isEnabled() ? mViewState : mEnabledState);
    }

    private void onStartDraw(Canvas canvas, ViewState state) {
        mBackgroundPaint.setStyle(Paint.Style.FILL);
        //绘制背景
        mBackgroundPaint.setColor(state.backgroundStateColor);
        drawBackground(canvas,
                mLeft, mTop, mRight, mBottom,
                mRadius, mBackgroundPaint);
        //绘制按钮
        drawButton(canvas, state.buttonX, mCenterY, state.buttonStateColor);
    }


    @Override
    public final void setChecked(boolean checked) {
        if (checked == isChecked()) {
            postInvalidate();
            return;
        }
        toggle();
    }

    @Override
    public final boolean isChecked() {
        return mChecked;
    }

    @Override
    public final void toggle() {
        toggle(mEnableAnimate);
    }


    public void setChecked(boolean checked, boolean animte) {
        if (checked == isChecked()) {
            postInvalidate();
            return;
        }
        toggle(animte);
    }

    private void toggle(boolean animate) {
        if (!mUiInited) {
            mChecked = !mChecked;
            postInvalidate();
            broadcastEvent();
            return;
        }

        if (!animate) {
            mChecked = !mChecked;
            setViewState(isChecked());
            postInvalidate();
            broadcastEvent();
            return;
        }

        if (mAnimator.isRunning()) {
            mAnimator.cancel();
        }
        mChecked = !mChecked;
        broadcastEvent();
        mAnimator.start();
    }

    private void drawBackground(Canvas canvas, float left, float top, float right, float bottom,
                                float backgroundRadius, Paint paint) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            canvas.drawRoundRect(left, top, right, bottom,
                    backgroundRadius, backgroundRadius, paint);
        } else {
            mRect.set(left, top, right, bottom);
            canvas.drawRoundRect(mRect,
                    backgroundRadius, backgroundRadius, paint);
        }
    }

    private void drawButton(Canvas canvas, float x, float y, int stateColor) {
        if (mShadowEffect) {
            mButtonPaint.setShadowLayer(mShadowRadius, 0, mShadowOffset,
                    stateColor - SHADOWLAYER);
        } else {
            mButtonPaint.setShadowLayer(0, 0, 0, 0);
        }
        mButtonPaint.setColor(stateColor);
        canvas.drawCircle(x, y, mRadius - dp2px(2), mButtonPaint);
    }

    private void broadcastEvent() {
        if (mOnCheckedChangeListener != null) {
            mOnCheckedChangeListener.onCheckedChanged(this, isChecked());
        }
    }

    private void setViewState(boolean checked) {
        mViewState.radius = mRadius;
        mViewState.backgroundStateColor = checked ? mBackgroundCheckedColor : mBackgroundUncheckColor;
        mViewState.buttonStateColor = checked ? mButtonCheckColor : mButtonUncheckColor;
        mViewState.buttonX = checked ? mButtonMaxX : mButtonMinX;
    }

    @Override
    public final void setOnClickListener(OnClickListener l) {

    }

    @Override
    public final void setOnLongClickListener(OnLongClickListener l) {

    }

    public void setShadowEffect(boolean shadowEffect) {
        if (this.mShadowEffect == shadowEffect) {
            return;
        }
        this.mShadowEffect = shadowEffect;
        postInvalidate();
    }

    public void setEnableAnimate(boolean enable) {
        this.mEnableAnimate = enable;
    }

    public void setOnCheckedChangeListener(OnCheckedChangeListener l) {
        mOnCheckedChangeListener = l;
    }

    public void setSwitchButtonColor(@ColorInt int checkedColor, @ColorInt int uncheckColor) {
        this.mButtonCheckColor = checkedColor;
        this.mButtonUncheckColor = uncheckColor;
        setViewState(isChecked());
        postInvalidate();
    }

    public void setSwitchBackgroundColor(@ColorInt int checkedColor, @ColorInt int uncheckColor) {
        this.mBackgroundCheckedColor = checkedColor;
        this.mBackgroundUncheckColor = uncheckColor;
        setViewState(isChecked());
        postInvalidate();
    }


    /*******************************************************/

    /**
     * 保存动画状态
     */
    private static class ViewState {
        /**
         * 按钮x位置[mButtonMinX-mButtonMaxX]
         */
        float buttonX;
        /**
         * 状态背景颜色
         */
        int backgroundStateColor;
        /**
         * 状态背景的半径
         */
        float radius;

        /**
         * 按钮颜色
         */
        int buttonStateColor;


        ViewState() {
        }
    }


    /*******************************************************/
    private static float dp2px(float dp) {
        Resources r = Resources.getSystem();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
    }

    private static int dp2pxInt(float dp) {
        return (int) dp2px(dp);
    }
}
