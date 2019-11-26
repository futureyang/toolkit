package com.future.toolkit.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MarginLayoutParamsCompat;

import com.future.toolkit.R;
import com.future.toolkit.utils.DensityUtils;
import com.future.toolkit.utils.SystemBarUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by yangqc on 2019/9/26
 *
 * @Author yangqc
 */
public class MToolbar extends Toolbar {
    private static final String TAG = "MToolbar";

    protected ImageView mHomeIconView;
    protected TextView mTitleTextView;
    protected ImageView mSubTitleIconView;

    protected ImageView mMenuIconView;
    protected ImageView mMenuSubIconView;
    protected TextView mMenuTextView;

    protected TextView mSubTitleTextView;
    private List<View> mMainLineViews = new ArrayList<>();

    private boolean mHasCustom;
    private final int mActionBarSize;
    private final int mMinContentPadding;
    private int mMaxLineHeight;
    private final int mMenuIconPadding;


    public MToolbar(Context context) {
        this(context, null);
    }

    public MToolbar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MToolbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mActionBarSize = SystemBarUtils.getActionBarHeight(context);
        mMinContentPadding = DensityUtils.dip2px(context, 4);
        mMenuIconPadding = DensityUtils.dip2px(context, 6);

        int statusBarHeight = SystemBarUtils.getStatusBarHeight(context);
        setPadding(getPaddingLeft(), statusBarHeight,
                getPaddingRight(), getPaddingBottom());
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mHasCustom = getChildCount() > 0;
        mTitleTextView = findViewById(R.id.title);
        mHomeIconView = findViewById(R.id.home);
    }

    @Override
    public void setTitle(CharSequence title) {
        if (mTitleTextView == null && !mHasCustom) {
            mTitleTextView = (TextView) LayoutInflater.from(getContext())
                    .inflate(R.layout.layout_toolbar_title_text_view, this, false);
            addView(mTitleTextView);
            mMainLineViews.add(mTitleTextView);
        }
        if (mTitleTextView != null) {
            mTitleTextView.setText(title);
        } else {
            super.setTitle(title);
        }
    }

    @Override
    public CharSequence getTitle() {
        if (mTitleTextView != null){
            return mTitleTextView.getText();
        }
        return super.getTitle();
    }

    public void setTitleVisible(boolean visible){
        if (mTitleTextView != null){
            mTitleTextView.setVisibility(visible ? View.VISIBLE : View.GONE);
        }
    }

    public void setTitleGravity(int gravity){
        if (mTitleTextView != null){
            LayoutParams lp = (LayoutParams) mTitleTextView.getLayoutParams();
            lp.gravity = gravity;
            mTitleTextView.setGravity(gravity);
        }
    }

    @Override
    public void setSubtitle(CharSequence subtitle) {
        if (TextUtils.isEmpty(subtitle)) {
            return;
        }
        if (mSubTitleTextView == null) {
            mSubTitleTextView = (TextView) LayoutInflater.from(getContext())
                    .inflate(R.layout.layout_toolbar_sub_title_text_view, this, false);
            addView(mSubTitleTextView);
        }
        mSubTitleTextView.setText(subtitle);
    }

    private void ensureNavigationIcon(){
        if (mHomeIconView == null && !mHasCustom) {
            mHomeIconView = (ImageView) LayoutInflater.from(getContext())
                    .inflate(R.layout.layout_toolbar_nav_image_view, this, false);
            addView(mHomeIconView);
            mMainLineViews.add(mHomeIconView);
        }
    }

    public void setNavigationView(@LayoutRes int layoutResId){
        setNavigationView((ImageView) LayoutInflater.from(getContext())
                .inflate(layoutResId, this, false));
    }

    public void setNavigationView(@NonNull ImageView imageView){
        if (mHomeIconView != null){
            removeView(mHomeIconView);
            mMainLineViews.remove(mHomeIconView);
        }
        mHomeIconView = imageView;
        addView(mHomeIconView);
        mMainLineViews.add(mHomeIconView);
    }

    @Override
    public void setNavigationIcon(@Nullable Drawable icon) {
        ensureNavigationIcon();
        if (mHomeIconView != null) {
            mHomeIconView.setImageDrawable(icon);
            setNavigationIconVisible(icon != null);
        } else {
            super.setNavigationIcon(icon);
        }
    }

    @Override
    public void setNavigationOnClickListener(View.OnClickListener listener) {
        if (mHomeIconView != null) {
            mHomeIconView.setOnClickListener(listener);
        } else {
            super.setNavigationOnClickListener(listener);
        }
    }

    public void setNavigationIconVisible(boolean isVisible) {
        if (isVisible){
            ensureNavigationIcon();
        }
        if (mHomeIconView != null) {
            mHomeIconView.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        }
    }

    public void setMenuText(int resId) {
        setMenuText(getResources().getString(resId), null);
    }

    public void setMenuText(int resId, @Nullable View.OnClickListener listener) {
        setMenuText(getResources().getString(resId), listener);
    }

    public void setMenuText(@NonNull CharSequence menuText, @Nullable View.OnClickListener listener) {
        if (mMenuTextView == null) {
            mMenuTextView = (TextView) LayoutInflater.from(getContext())
                    .inflate(R.layout.layout_toolbar_text_view, this, false);
            addView(mMenuTextView);
            mMainLineViews.add(mMenuTextView);
        }
        mMenuTextView.setText(menuText);
        if (listener != null) {
            mMenuTextView.setOnClickListener(listener);
        }
    }

    public void setMenuIcon(@Nullable Drawable icon, @Nullable View.OnClickListener listener) {
        if (mMenuIconView == null) {
            mMenuIconView = (ImageView) LayoutInflater.from(getContext())
                    .inflate(R.layout.layout_toolbar_image_view, this, false);
            addView(mMenuIconView);
            mMainLineViews.add(mMenuIconView);
        }
        mMenuIconView.setImageDrawable(icon);
        if (listener != null) {
            mMenuIconView.setOnClickListener(listener);
        }
    }

    public void setMenuIcon(@DrawableRes int iconRes, @Nullable View.OnClickListener listener) {
        setMenuIcon(AppCompatResources.getDrawable(getContext(), iconRes), listener);
    }

    public void setMenuSubIcon(@Nullable Drawable icon, @Nullable View.OnClickListener listener) {
        if (mMenuSubIconView == null) {
            mMenuSubIconView = (ImageView) LayoutInflater.from(getContext())
                    .inflate(R.layout.layout_toolbar_image_view, this, false);
            addView(mMenuSubIconView);
            mMainLineViews.add(mMenuSubIconView);
        }
        mMenuSubIconView.setImageDrawable(icon);
        if (listener != null) {
            mMenuSubIconView.setOnClickListener(listener);
        }
    }

    public void setMenuSubIcon(@DrawableRes int iconRes, @Nullable View.OnClickListener listener) {
        setMenuSubIcon(AppCompatResources.getDrawable(getContext(), iconRes), listener);
    }

    public void setSubTitleIcon(@Nullable Drawable icon, @Nullable View.OnClickListener listener) {
        if (mSubTitleIconView == null) {
            mSubTitleIconView = (ImageView) LayoutInflater.from(getContext())
                    .inflate(R.layout.layout_toolbar_image_view, this, false);
            addView(mSubTitleIconView);
            mMainLineViews.add(mSubTitleIconView);
        }
        mSubTitleIconView.setImageDrawable(icon);
        if (listener != null) {
            mSubTitleIconView.setOnClickListener(listener);
        }
    }

    public void setSubTitleIcon(int iconRes, @Nullable View.OnClickListener listener) {
        setSubTitleIcon(AppCompatResources.getDrawable(getContext(), iconRes), listener);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mHasCustom) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        } else {
            if (mTitleTextView != null) {
                int index = mMainLineViews.indexOf(mTitleTextView);
                if (index != mMainLineViews.size() - 1) {//title放在最后测量
                    mMainLineViews.remove(mTitleTextView);
                    mMainLineViews.add(mTitleTextView);
                }
            }

            int widthUsed = 0;
            int height = mActionBarSize;
            for (View view : mMainLineViews) {
                if (shouldLayout(view)) {
                    measureChildConstrained(view, widthMeasureSpec, widthUsed, heightMeasureSpec, 0);
                    widthUsed += view.getMeasuredWidth() + getHorizontalMargins(view);
                    height = Math.max(view.getMeasuredHeight() + mMinContentPadding * 2, height);
                    mMaxLineHeight = Math.max(view.getMeasuredHeight(), mMaxLineHeight);
                }
            }
            if (shouldLayout(mTitleTextView) && shouldLayout(mSubTitleTextView)) {
                measureChildConstrained(mSubTitleTextView, widthMeasureSpec, 0, heightMeasureSpec, 0);
                height += mSubTitleTextView.getMeasuredHeight() + getVerticalMargins(mSubTitleTextView);
            }
            measureCustomView(widthMeasureSpec, heightMeasureSpec);

            setMeasuredDimension(View.MeasureSpec.getSize(widthMeasureSpec),
                    height + getPaddingTop() + getPaddingBottom());
        }
    }

    private boolean isSpecialView(@NonNull View view){
        return mMainLineViews.contains(view) || (mSubTitleTextView != null && mSubTitleTextView == view);
    }

    private void measureCustomView(int widthMeasureSpec, int heightMeasureSpec) {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (!isSpecialView(child) && shouldLayout(child)){
                measureChildConstrained(child, widthMeasureSpec, 0, heightMeasureSpec, 0);
            }
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (mHasCustom) {
            super.onLayout(changed, l, t, r, b);
        } else {
            int contentHeight = getHeight() - getPaddingTop() - getPaddingBottom();
            if (shouldLayout(mTitleTextView) && shouldLayout(mSubTitleTextView)) {
                contentHeight = contentHeight - (mSubTitleTextView.getMeasuredHeight() + getVerticalMargins(mSubTitleTextView));
            }
            int top = getPaddingTop();
            int left = getPaddingLeft();
            if (shouldLayout(mHomeIconView)) {
                left = layoutChildLeft(mHomeIconView, top, left, contentHeight);
            }
            if (shouldLayout(mTitleTextView)) {
                LayoutParams lp = (LayoutParams) mTitleTextView.getLayoutParams();
                if ((lp.gravity & Gravity.CENTER_HORIZONTAL) == 0){
                    left = layoutChildLeft(mTitleTextView, top, left, contentHeight) + mMenuIconPadding;
                }
            }

            if (shouldLayout(mSubTitleIconView)) {
                left = layoutChildLeft(mSubTitleIconView, top, left, contentHeight);
            }
            int right = getWidth() - getPaddingRight();
            if (shouldLayout(mMenuTextView)){
                right = layoutChildRight(mMenuTextView, top, right, contentHeight) - mMenuIconPadding;
            }
            if (shouldLayout(mMenuSubIconView)){
                right = layoutChildRight(mMenuSubIconView, top, right, contentHeight) - mMenuIconPadding;
            }
            if (shouldLayout(mMenuIconView)){
                right = layoutChildRight(mMenuIconView, top, right, contentHeight) - mMenuIconPadding;
            }

            if (shouldLayout(mTitleTextView) && shouldLayout(mSubTitleTextView)){
                ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) mSubTitleTextView.getLayoutParams();
                int subLeft = mTitleTextView.getLeft() + lp.leftMargin;
                int subTop = mTitleTextView.getBottom() + lp.topMargin;
                mSubTitleTextView.layout(subLeft, subTop, subLeft + mSubTitleTextView.getMeasuredWidth(),
                        subTop + mSubTitleTextView.getMeasuredHeight());
            }

            layoutCustomView(getPaddingTop(), getPaddingLeft(), contentHeight);
        }
    }

    private void layoutCustomView(int top, int left, int contentHeight) {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (!isSpecialView(child) && shouldLayout(child)){
                layoutChildLeft(child, top, left, contentHeight);
            }
        }
    }


    private boolean shouldLayout(View view) {
        return view != null && view.getParent() == this && view.getVisibility() == View.VISIBLE;
    }

    private void measureChildConstrained(View child, int parentWidthSpec, int widthUsed,
                                         int parentHeightSpec, int heightUsed) {
        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
        int childWidthSpec = getChildMeasureSpec(parentWidthSpec,
                this.getPaddingLeft() + this.getPaddingRight() + lp.leftMargin + lp.rightMargin + widthUsed, lp.width);
        int childHeightSpec = getChildMeasureSpec(parentHeightSpec,
                mMinContentPadding * 2 + lp.topMargin + lp.bottomMargin + heightUsed, lp.height);
        child.measure(childWidthSpec, childHeightSpec);
    }

    private int getHorizontalMargins(View v) {
        ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
        return MarginLayoutParamsCompat.getMarginStart(mlp) + MarginLayoutParamsCompat.getMarginEnd(mlp);
    }

    private int getVerticalMargins(View v) {
        ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
        return mlp.topMargin + mlp.bottomMargin;
    }

    private int layoutChildLeft(View child, int top, int left, int contentHeight) {
        LayoutParams lp = (LayoutParams) child.getLayoutParams();
        int paddingTop = (contentHeight - child.getMeasuredHeight() - getVerticalMargins(child)) / 2;
        int alignTop = top + paddingTop + lp.topMargin;
        left += lp.leftMargin;
        int childWidth = child.getMeasuredWidth();
        child.layout(left, alignTop, left + childWidth, alignTop + child.getMeasuredHeight());
        left += childWidth + lp.rightMargin;
        return left;
    }


    private int layoutChildRight(View child, int top, int right, int contentHeight) {
        LayoutParams lp = (LayoutParams) child.getLayoutParams();
        int paddingTop = (contentHeight - child.getMeasuredHeight() - getVerticalMargins(child)) / 2;
        int alignTop = top + paddingTop + lp.topMargin;
        right -= lp.rightMargin;
        int childWidth = child.getMeasuredWidth();
        child.layout(right - childWidth, alignTop, right, alignTop + child.getMeasuredHeight());
        right -= childWidth - lp.leftMargin;
        return right;
    }
}
