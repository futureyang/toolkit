package com.future.toolkit.dialog.prompt;

import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.annotation.ArrayRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.content.res.AppCompatResources;

import com.future.toolkit.R;

import java.util.List;

/**
 * Created by yangqc on 2019/11/8
 *
 * @Author yangqc
 */
public class PromptDialog extends Dialog {

    private PromptDialogController mPromptController;

    private Context mContext;

    public PromptDialog(Context context) {
        this(context, 0);
    }

    public PromptDialog(Context context, int themeResId) {
        super(context, resolveDialogTheme(themeResId));
        this.mContext = context;
        mPromptController = new PromptDialogController(getContext(), this, getWindow());
    }

    protected PromptDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        this(context, 0);
        setCancelable(cancelable);
        setOnCancelListener(cancelListener);
    }

    private static int resolveDialogTheme(int themeResId) {
        if (themeResId >= 0x01000000) {
            return themeResId;
        } else {
            return R.style.OsDialogAlert;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPromptController.installContent();
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        mPromptController.setTitle(title);
    }

    public ImageView getTipImageView(){
        return mPromptController.getTipImageView();
    }

    public Button getButton(int which) {
        return mPromptController.getButton(which);
    }

    public ListView getListView() {
        return mPromptController.getListView();
    }

    public void setListItems(@NonNull List<? extends CharSequence> items){
        mPromptController.setListItems(items);
    }

    public static class Builder {

        private final PromptDialogParams mPromptParams;

        public Builder(Context context) {
            this(context, 0);
        }

        public Builder(Context context, int themeResId) {
            mPromptParams = new PromptDialogParams(
                    new ContextThemeWrapper(context, resolveDialogTheme(themeResId)));
        }

        public Context getContext() {
            return mPromptParams.mContext;
        }

        public Builder setTitle(@StringRes int titleId) {
            mPromptParams.mTitle = mPromptParams.mContext.getText(titleId);
            return this;
        }

        public Builder setTitle(CharSequence title) {
            mPromptParams.mTitle = title;
            return this;
        }

        public Builder setHint(@StringRes int messageId) {
            mPromptParams.mHint = mPromptParams.mContext.getText(messageId);
            return this;
        }

        public Builder setHint(CharSequence message) {
            mPromptParams.mHint = message;
            return this;
        }

        public Builder setMessage(@StringRes int messageId) {
            mPromptParams.mMessage = mPromptParams.mContext.getText(messageId);
            return this;
        }

        public Builder setMessage(CharSequence message) {
            mPromptParams.mMessage = message;
            return this;
        }

        public Builder setMessageGravity(int gravity){
            mPromptParams.mMessageGravity = gravity;
            return this;
        }

        public Builder setImage(@Nullable Drawable drawable){
            mPromptParams.mTipImage = drawable;
            mPromptParams.mHasImage = drawable != null;
            return this;
        }

        public Builder setMaxHeight(int height){
            mPromptParams.mMaxHeight = height;
            return this;
        }

        public Builder setProgress(){
            mPromptParams.isShowProgressView = true;
            return this;
        }

        public Builder setImage(@DrawableRes int resId){
            return setImage(AppCompatResources.getDrawable(mPromptParams.mContext, resId));
        }

        public Builder setButtonAutoDismiss(boolean dismiss){
            mPromptParams.isAutoDismiss = dismiss;
            return this;
        }

        public Builder setPositiveButton(@StringRes int textId, final OnClickListener listener) {
            mPromptParams.mPositiveButtonText = mPromptParams.mContext.getText(textId);
            mPromptParams.mPositiveButtonListener = listener;
            return this;
        }

        public Builder setPositiveButton(CharSequence text, final OnClickListener listener) {
            mPromptParams.mPositiveButtonText = text;
            mPromptParams.mPositiveButtonListener = listener;
            return this;
        }

        public Builder setNegativeButton(@StringRes int textId, final OnClickListener listener) {
            mPromptParams.mNegativeButtonText = mPromptParams.mContext.getText(textId);
            mPromptParams.mNegativeButtonListener = listener;
            return this;
        }

        public Builder setNegativeButton(CharSequence text, final OnClickListener listener) {
            mPromptParams.mNegativeButtonText = text;
            mPromptParams.mNegativeButtonListener = listener;
            return this;
        }

        public Builder setNeutralButton(@StringRes int textId, final OnClickListener listener) {
            mPromptParams.mNeutralButtonText = mPromptParams.mContext.getText(textId);
            mPromptParams.mNeutralButtonListener = listener;
            return this;
        }

        public Builder setNeutralButton(CharSequence text, final OnClickListener listener) {
            mPromptParams.mNeutralButtonText = text;
            mPromptParams.mNeutralButtonListener = listener;
            return this;
        }

        public Builder setCancelable(boolean cancelable) {
            mPromptParams.mCancelable = cancelable;
            return this;
        }

        public Builder setOnCancelListener(OnCancelListener onCancelListener) {
            mPromptParams.mOnCancelListener = onCancelListener;
            return this;
        }

        public Builder setOnDismissListener(OnDismissListener onDismissListener) {
            mPromptParams.mOnDismissListener = onDismissListener;
            return this;
        }

        public Builder setOnKeyListener(OnKeyListener onKeyListener) {
            mPromptParams.mOnKeyListener = onKeyListener;
            return this;
        }

        public Builder setItems(@ArrayRes int itemsId, final OnClickListener listener) {
            mPromptParams.mItems = mPromptParams.mContext.getResources().getTextArray(itemsId);
            mPromptParams.mOnClickListener = listener;
            return this;
        }

        public Builder setItems(CharSequence[] items, final OnClickListener listener) {
            mPromptParams.mItems = items;
            mPromptParams.mOnClickListener = listener;
            return this;
        }

        public Builder setItems(List<? extends CharSequence> items, final OnClickListener listener) {
            return setItems(items.toArray(new CharSequence[items.size()]),listener);
        }

        public Builder setAdapter(final ListAdapter adapter, final OnClickListener listener) {
            mPromptParams.mAdapter = adapter;
            mPromptParams.mOnClickListener = listener;
            return this;
        }

        public Builder setCursor(final Cursor cursor, final OnClickListener listener,
                                 String labelColumn) {
            mPromptParams.mCursor = cursor;
            mPromptParams.mLabelColumn = labelColumn;
            mPromptParams.mOnClickListener = listener;
            return this;
        }

        public Builder setMultiChoiceItems(@ArrayRes int itemsId, boolean[] checkedItems,
                                           final OnMultiChoiceClickListener listener) {
            mPromptParams.mItems = mPromptParams.mContext.getResources().getTextArray(itemsId);
            mPromptParams.mCheckedItems = checkedItems;
            mPromptParams.mOnCheckboxClickListener = listener;
            mPromptParams.mIsMultiChoice = true;
            return this;
        }

        public Builder setMultiChoiceItems(CharSequence[] items, boolean[] checkedItems,
                                           final OnMultiChoiceClickListener listener) {
            mPromptParams.mItems = items;
            mPromptParams.mCheckedItems = checkedItems;
            mPromptParams.mOnCheckboxClickListener = listener;
            mPromptParams.mIsMultiChoice = true;
            return this;
        }

        public Builder setMultiChoiceItems(List<String> items, boolean[] checkedItems,
                                           final OnMultiChoiceClickListener listener) {
            mPromptParams.mItems = items.toArray(new String[items.size()]);
            mPromptParams.mCheckedItems = checkedItems;
            mPromptParams.mOnCheckboxClickListener = listener;
            mPromptParams.mIsMultiChoice = true;
            return this;
        }

        public Builder setMultiChoiceItems(Cursor cursor, String isCheckedColumn, String labelColumn,
                                           final OnMultiChoiceClickListener listener) {
            mPromptParams.mCursor = cursor;
            mPromptParams.mIsCheckedColumn = isCheckedColumn;
            mPromptParams.mLabelColumn = labelColumn;
            mPromptParams.mOnCheckboxClickListener = listener;
            mPromptParams.mIsMultiChoice = true;
            return this;
        }

        public Builder setSingleChoiceItems(@ArrayRes int itemsId, int checkedItem,
                                            final OnClickListener listener) {
            mPromptParams.mItems = mPromptParams.mContext.getResources().getTextArray(itemsId);
            mPromptParams.mCheckedItem = checkedItem;
            mPromptParams.mOnClickListener = listener;
            mPromptParams.mIsSingleChoice = true;
            return this;
        }

        public Builder setSingleChoiceItems(Cursor cursor, int checkedItem, String labelColumn,
                                            final OnClickListener listener) {
            mPromptParams.mCursor = cursor;
            mPromptParams.mCheckedItem = checkedItem;
            mPromptParams.mLabelColumn = labelColumn;
            mPromptParams.mOnClickListener = listener;
            mPromptParams.mIsSingleChoice = true;
            return this;
        }

        public Builder setSingleChoiceItems(CharSequence[] items, int checkedItem,
                                            final OnClickListener listener) {
            mPromptParams.mItems = items;
            mPromptParams.mCheckedItem = checkedItem;
            mPromptParams.mOnClickListener = listener;
            mPromptParams.mIsSingleChoice = true;
            return this;
        }

        public Builder setSingleChoiceItems(List<String> items, int checkedItem,
                                            final OnClickListener listener) {

            mPromptParams.mItems = items.toArray(new CharSequence[items.size()]);
            mPromptParams.mCheckedItem = checkedItem;
            mPromptParams.mOnClickListener = listener;
            mPromptParams.mIsSingleChoice = true;
            return this;
        }

        public Builder setSingleChoiceItems(List<String> items, List<Integer> colors,int checkedItem, final OnClickListener listener) {
            mPromptParams.mColors = colors;
            mPromptParams.mItems = items.toArray(new CharSequence[items.size()]);
            mPromptParams.mCheckedItem = checkedItem;
            mPromptParams.mOnClickListener = listener;
            mPromptParams.mIsSingleChoice = true;
            return this;
        }

        public Builder setSingleChoiceItems(ListAdapter adapter, int checkedItem,
                                            final OnClickListener listener) {
            mPromptParams.mAdapter = adapter;
            mPromptParams.mCheckedItem = checkedItem;
            mPromptParams.mOnClickListener = listener;
            mPromptParams.mIsSingleChoice = true;
            return this;
        }

        public Builder setOnItemSelectedListener(
                final AdapterView.OnItemSelectedListener listener) {
            mPromptParams.mOnItemSelectedListener = listener;
            return this;
        }

        public Builder setView(@LayoutRes int layoutResId) {
            mPromptParams.mView = null;
            mPromptParams.mViewLayoutResId = layoutResId;
            return this;
        }

        public Builder setView(View view) {
            mPromptParams.mView = view;
            mPromptParams.mViewLayoutResId = 0;
            return this;
        }

        public PromptDialog create() {
            PromptDialog dialog;
            if(mPromptParams.isShowProgressView){
                dialog = new PromptDialog(mPromptParams.mContext,R.style.OsFullscreenPrompt);
            }else{
                dialog = new PromptDialog(mPromptParams.mContext);
                dialog.setCanceledOnTouchOutside(mPromptParams.mCancelable);
            }
            dialog.setOnCancelListener(mPromptParams.mOnCancelListener);
            dialog.setCancelable(mPromptParams.mCancelable);
            mPromptParams.apply(dialog.mPromptController);
            dialog.setOnDismissListener(mPromptParams.mOnDismissListener);
            if (mPromptParams.mOnKeyListener != null) {
                dialog.setOnKeyListener(mPromptParams.mOnKeyListener);
            }
            return dialog;
        }

        public PromptDialog show() {
            final PromptDialog dialog = create();
            dialog.show();
            return dialog;
        }
    }
}
