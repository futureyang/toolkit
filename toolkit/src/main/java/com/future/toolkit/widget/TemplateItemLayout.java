package com.future.toolkit.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;

import com.future.toolkit.R;

/**
 * Created by yangqc on 2019/10/15
 *
 * @Author yangqc
 */
public class TemplateItemLayout extends FrameLayout {

    private static final String TEXT_TEMP = "一";

    private Paint mPaint;
    private int mLineWidth = 1;
    private boolean mLineVisible;

    private LayoutInflater mInflater;

    private ViewStub mLeftStub;
    private ViewStub mCenterStub;
    private ViewStub mRightStub;

    private ViewGroup mViewGroup = null;
    private View mLeftView;
    private View mContentView;
    private View mRightView;

    private ImageView mIvLeft;
    private TextView mTvContent;
    private ImageView mIvRight;

    private int mDividerColorResId;
    private int mContentTextColorResId;
    private int mHintTextColorResId;
    private boolean leftVisible;



    public TemplateItemLayout(Context context) {
        this(context, null);
    }

    public TemplateItemLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TemplateItemLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context);
        initView(context);
        initAttrs(context, attrs);

    }

    private void init(Context context) {
        mDividerColorResId = R.color.dividerColorGray;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(mLineWidth);
    }

    private void initView(Context context) {
        mInflater = LayoutInflater.from(context);
        View itemView = mInflater.inflate(R.layout.layout_template_item, this);
        mViewGroup = itemView.findViewById(R.id.layout_custom_item);
        mLeftStub = itemView.findViewById(R.id.template_left);
        mCenterStub = itemView.findViewById(R.id.template_center);
        mRightStub = itemView.findViewById(R.id.template_right);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        Resources r = context.getResources();
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TemplateItemLayout);

        float labelTextSize = a.getDimension(R.styleable.TemplateItemLayout_cil_labelSize,
                r.getDimension(R.dimen.textSizeNormal));

        String content = a.getString(R.styleable.TemplateItemLayout_cil_content);
        mContentTextColorResId = a.getResourceId(R.styleable.TemplateItemLayout_cil_contentColor,
                R.color.textColorPrimary);
        float contentTextSize = a.getDimension(R.styleable.TemplateItemLayout_cil_labelSize,
                r.getDimension(R.dimen.textSizeNormal));

        String hint = a.getString(R.styleable.TemplateItemLayout_cil_hint);
        mHintTextColorResId = a.getResourceId(R.styleable.TemplateItemLayout_cil_hintColor,
                R.color.textColorTertiary);

        boolean editable = a.getBoolean(R.styleable.TemplateItemLayout_cil_editable, false);
        leftVisible = a.getBoolean(R.styleable.TemplateItemLayout_cil_leftVisible, true);
        boolean rightVisible = a.getBoolean(R.styleable.TemplateItemLayout_cil_rightVisible, true);
        boolean lineVisible = a.getBoolean(R.styleable.TemplateItemLayout_cil_lineVisible, true);

        int leftLayoutResId = a.getResourceId(R.styleable.TemplateItemLayout_cil_leftLayoutId,
                R.layout.layout_template_item_left);
        int contentLayoutResId = a.getResourceId(R.styleable.TemplateItemLayout_cil_contentLayoutId,
                editable ? R.layout.layout_template_item_content_editable
                        : R.layout.layout_template_item_content_read);
        int rightLayoutResId = a.getResourceId(R.styleable.TemplateItemLayout_cil_rightLayoutId,
                editable ? R.layout.layout_template_item_right
                        : R.layout.layout_template_item_content_read);

        int leftImageId = a.getResourceId(R.styleable.TemplateItemLayout_cil_leftImageId, 0);
        int rightImageId = a.getResourceId(R.styleable.TemplateItemLayout_cil_rightImageId, 0);

        boolean inputCipher = a.getBoolean(R.styleable.TemplateItemLayout_cil_inputCipher, false);

        a.recycle();


        mLeftStub.setLayoutResource(leftLayoutResId);
        mLeftView = mLeftStub.inflate();
        mIvLeft = mLeftView.findViewById(R.id.iv_template_item_left);

        mCenterStub.setLayoutResource(contentLayoutResId);
        mContentView = mCenterStub.inflate();
        mTvContent = mContentView.findViewById(editable ?
                R.id.et_templeate_item_content_editable :
                R.id.tv_template_item_content_read);

        mRightStub.setLayoutResource(rightLayoutResId);
        mRightView = mRightStub.inflate();
        mIvRight = mRightView.findViewById(R.id.iv_template_item_right);

        if (!TextUtils.isEmpty(content)) {
            setContentText(content);
        }

        if (!TextUtils.isEmpty(hint)) {
            setContentHintText(hint);
        }

        if (mTvContent != null) {
            mTvContent.setTextSize(TypedValue.COMPLEX_UNIT_PX, contentTextSize);
        }

        if (mIvLeft != null) {
            mIvLeft.setVisibility(leftVisible ? VISIBLE : GONE);
            mIvLeft.setImageResource(leftImageId);
        }

        if (mIvRight != null) {
            mIvRight.setVisibility(rightVisible ? VISIBLE : GONE);
            mIvRight.setImageResource(rightImageId);
        }

        setContentInputCipher(inputCipher);

        mLineVisible = lineVisible;

        mPaint.setTextSize(labelTextSize);
        float singleWidth = mPaint.measureText(TEXT_TEMP);

        applyColors();
    }

    private void applyColors() {
        if (mDividerColorResId != 0) {
            mPaint.setColor(getColor(mDividerColorResId));
        }
        if (mContentTextColorResId != 0) {
            setContentTextColorRes(mContentTextColorResId);
        }
        if (mHintTextColorResId != 0) {
            setContentHintTextColorRes(mHintTextColorResId);
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (mLineVisible && mPaint != null) {
            int startX = Math.round((leftVisible ? mLeftView : mTvContent).getX());
            int startY = mViewGroup.getMeasuredHeight() - mLineWidth;
            canvas.drawLine(startX, startY, mViewGroup.getMeasuredWidth(), startY, mPaint);
        }
    }

    public void replaceContentView(View view) {
        int index = mViewGroup.indexOfChild(mContentView);
        if (index != -1) {
            mViewGroup.removeViewAt(index);
            mViewGroup.addView(view, index);
            mContentView = view;
        }
    }

    public void replaceRightView(View view) {
        int index = mViewGroup.indexOfChild(mRightView);
        if (index != -1) {
            mViewGroup.removeViewAt(index);
            mViewGroup.addView(view, index);
            mRightView = view;
        }
    }

    public View getRealLeftView() {
        return mLeftView;
    }

    public View getRealContentView() {
        return mContentView;
    }

    public View getRealRightView() {
        return mRightView;
    }

    public void setContentText(String text) {
        if (mTvContent != null) {
            mTvContent.setText(text);
            if (mTvContent instanceof EditText) {
                ((EditText) mTvContent).setSelection(mTvContent.getText().length());
            }
        }
    }

    public void setContentHintText(String hint) {
        if (mTvContent != null) {
            mTvContent.setHint(hint);
        }
    }

    public void setContentTextColorRes(@ColorRes int color) {
        if (mTvContent != null) {
            mTvContent.setTextColor(getColor(color));
        }
    }

    public void setContentTextColor(@ColorInt int color) {
        if (mTvContent != null) {
            mTvContent.setTextColor(color);
        }
    }

    public void setContentHintTextColorRes(@ColorRes int color) {
        if (mTvContent != null) {
            mTvContent.setHintTextColor(getColor(color));
        }
    }

    public void setContentHintTextColor(@ColorInt int color) {
        if (mTvContent != null) {
            mTvContent.setHintTextColor(color);
        }
    }

    public void setContentInputCipher(boolean cipher) {
        if (mTvContent != null) {
            mTvContent.setInputType(cipher ? EditorInfo.TYPE_CLASS_TEXT | EditorInfo.TYPE_TEXT_VARIATION_PASSWORD
                    : EditorInfo.TYPE_CLASS_TEXT);
        }
    }

    public void setContentInputTextWatcher(TextWatcher watch) {
        if (mTvContent != null) {
            mTvContent.addTextChangedListener(watch);
        }
    }

    public String getContentText() {
        if (mTvContent == null) {
            return "";
        }
        return mTvContent.getText().toString().trim();
    }

    public void resetRightView() {
        View arrowView = mInflater.inflate(R.layout.layout_template_item_right, mViewGroup, false);
        replaceRightView(arrowView);
    }

    public void setRightViewVisible(boolean visible) {
        if (mRightView != null) {
            mRightView.setVisibility(visible ? VISIBLE : GONE);
        }
    }

    private int getColor(@ColorRes int color) {
        return getResources().getColor(color);
    }
}
