package com.future.toolkit.dialog.prompt;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.List;

/**
 * Created by yangqc on 2019/11/8
 *
 * @Author yangqc
 */
public class PromptDialogParams {

    public final Context mContext;

    public CharSequence mTitle;
    public CharSequence mHint;
    public CharSequence mMessage;
    public CharSequence mPositiveButtonText;
    public DialogInterface.OnClickListener mPositiveButtonListener;
    public CharSequence mNegativeButtonText;
    public DialogInterface.OnClickListener mNegativeButtonListener;
    public CharSequence mNeutralButtonText;
    public DialogInterface.OnClickListener mNeutralButtonListener;
    public boolean mCancelable;
    public DialogInterface.OnCancelListener mOnCancelListener;
    public DialogInterface.OnDismissListener mOnDismissListener;
    public DialogInterface.OnKeyListener mOnKeyListener;
    public int mViewLayoutResId;
    public int mMessageGravity = Gravity.CENTER;
    public Drawable mTipImage;
    public View mView;
    public boolean isShowProgressView;
    public CharSequence[] mItems;
    public List<Integer> mColors;
    public ListAdapter mAdapter;
    public DialogInterface.OnClickListener mOnClickListener;
    public boolean mIsMultiChoice;
    public boolean mIsSingleChoice;
    public boolean mHasImage;
    public boolean[] mCheckedItems;
    public DialogInterface.OnMultiChoiceClickListener mOnCheckboxClickListener;
    public int mCheckedItem = -1;
    public Cursor mCursor;
    public String mLabelColumn;
    public String mIsCheckedColumn;
    public AdapterView.OnItemSelectedListener mOnItemSelectedListener;
    public OnPrepareListViewListener mOnPrepareListViewListener;
    public boolean mUseThemeConfig = true;
    public int mMaxHeight = -1;
    public boolean isAutoDismiss = true;

    public PromptDialogParams(Context context) {
        mContext = context;
        mCancelable = true;
    }

    public void apply(PromptDialogController controller) {
        controller.setButtonAutoDismiss(isAutoDismiss);
        controller.setMaxHeight(mMaxHeight);
        controller.setTitleView(mTitle);
        controller.setMessageView(mMessage);
        controller.setMessageGravity(mMessageGravity);
        controller.setView(mViewLayoutResId, mView);
        controller.setButton(DialogInterface.BUTTON_POSITIVE,
                mPositiveButtonText, mPositiveButtonListener, null);
        controller.setButton(DialogInterface.BUTTON_NEGATIVE,
                mNegativeButtonText, mNegativeButtonListener, null);
        controller.setButton(DialogInterface.BUTTON_NEUTRAL,
                mNeutralButtonText, mNeutralButtonListener, null);
        controller.setUseThemeConfig(mUseThemeConfig);
        if (isShowProgressView) {
            controller.setProgress();
        }
        if (mHasImage) {
            controller.setImageView(mTipImage);
        } else if (mIsMultiChoice) {
            if (mItems != null) {
                controller.setMultiChoiceList(mItems, mCheckedItems, mOnCheckboxClickListener);
            } else if (mCursor != null) {
                controller.setMultiChoiceList(mCursor,
                        mLabelColumn, mIsCheckedColumn, mOnCheckboxClickListener);
            }
        } else if (mIsSingleChoice) {
            if (mItems != null) {
                if (mColors != null) {
                    controller.setSingleChoiceList(mItems, mColors, mOnClickListener);
                } else {
                    controller.setSingleChoiceList(mItems, mOnClickListener);
                }
            } else if (mAdapter != null) {
                controller.setSingleChoiceList(mAdapter, mOnClickListener);
            } else if (mCursor != null) {
                controller.setSingleChoiceList(mCursor, mLabelColumn, mOnClickListener);
            }
        } else {
            if (mItems != null) {
                controller.setList(mItems, mOnClickListener);
            } else if (mAdapter != null) {
                controller.setList(mAdapter, mOnClickListener);
            } else if (mCursor != null) {
                controller.setList(mCursor, mLabelColumn, mOnClickListener);
            }
        }
        if (mOnPrepareListViewListener != null) {
            controller.onPrepareListView(mOnPrepareListViewListener);
        }
        if (mIsSingleChoice) {
            controller.setCheckedItem(mCheckedItem);
        }
    }

    public interface OnPrepareListViewListener {
        void onPrepareListView(ListView listView);
    }

}
