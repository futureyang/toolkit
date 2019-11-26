package com.future.toolkit.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import com.future.toolkit.R;

/**
 * TabItem is a special 'view' which allows you to declare tab items for a {@link MTabLayout}
 * within a layout. This view is not actually added to TabLayout, it is just a dummy which allows
 * setting of a tab items's text, icon and custom layout. See TabLayout for more information on how
 * to use it.
 *
 * @attr ref android.support.design.R.styleable#TabItem_android_icon
 * @attr ref android.support.design.R.styleable#TabItem_android_text
 * @attr ref android.support.design.R.styleable#TabItem_android_layout
 *
 * @see MTabLayout
 */
public final class MTabItem extends View {
    final CharSequence mText;
    final Drawable mIcon;
    final int mCustomLayout;

    public MTabItem(Context context) {
        this(context, null);
    }

    public MTabItem(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TabItem);
        mText = a.getText(R.styleable.TabItem_android_text);
        mIcon = a.getDrawable(R.styleable.TabItem_android_icon);
        mCustomLayout = a.getResourceId(R.styleable.TabItem_android_layout, 0);
        a.recycle();
    }
}
