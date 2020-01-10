package com.future.toolkit.dialog.prompt;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.CursorAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.future.toolkit.R;
import com.future.toolkit.widget.MaxFrameLayout;
import com.future.toolkit.widget.MaxListView;
import com.future.toolkit.widget.MaxScrollView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by yangqc on 2019/11/8
 *
 * @Author yangqc
 */
public class PromptDialogController {

    private static final int
            LIST_ITEM_LAYOUT = R.layout.prompt_dialog_item_text;
    private static final int
            LIST_ITEM_TEXT_VIEW = R.id.list_item;
    private static final int
            LIST_ITEM_SINGLE_CHOICE_LAYOUT = R.layout.prompt_dialog_single_choice;
    private static final int
            LIST_ITEM_MULTI_CHOICE_LAYOUT = R.layout.prompt_dialog_muti_choice;
    private static final int LIST_ITEM_ID = R.id.dialog_checked_textview;

    private Context mContext;
    private final DialogInterface mDialogInterface;
    private final Window mWindow;
    private final LayoutInflater mInflater;
    private Handler mHandler;

    private View mView;
    private LinearLayout mContainer;
    private TextView mTitleView;
    private TextView mMessageView;
    private MaxScrollView mScrollMessageView;
    private ImageView mImageView;
    private FrameLayout mFrameContainer;
    private MaxListView mListView;
    public int mCheckedItem = -1;
    public boolean[] mCheckedItems;

    private int mButtonLayoutRes;
    private boolean mIsButtonVertical = false;

    private boolean mHasPositiveButton;
    private Button mButtonPositive;
    private CharSequence mButtonPositiveText;
    private Message mButtonPositiveMessage;

    private boolean mHasNegativeButton;
    private Button mButtonNegative;
    private CharSequence mButtonNegativeText;
    private Message mButtonNegativeMessage;

    private boolean mHasNeutralButton;
    private Button mButtonNeutral;
    private CharSequence mButtonNeutralText;
    private Message mButtonNeutralMessage;
    private PromptDialogThemeConfig mThemeConfig;
    private int mMaxHeight = -1;
    private boolean isAutoDismiss;

    private final View.OnClickListener mButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final Message m;
            if (v == mButtonPositive && mButtonPositiveMessage != null) {
                m = Message.obtain(mButtonPositiveMessage);
            } else if (v == mButtonNegative && mButtonNegativeMessage != null) {
                m = Message.obtain(mButtonNegativeMessage);
            } else if (v == mButtonNeutral && mButtonNeutralMessage != null) {
                m = Message.obtain(mButtonNeutralMessage);
            } else {
                m = null;
            }

            if (m != null) {
                m.sendToTarget();
            }

            if (isAutoDismiss) {
                mHandler.obtainMessage(
                        ButtonHandler.MSG_DISMISS_DIALOG, mDialogInterface).sendToTarget();
            }
        }
    };

    public PromptDialogController(Context context, DialogInterface di, Window window) {
        mContext = context;
        mDialogInterface = di;
        mWindow = window;
        mHandler = new ButtonHandler(di);
        mInflater = LayoutInflater.from(context);
        window.requestFeature(Window.FEATURE_NO_TITLE);
        //window.setWindowAnimations(R.style.OsAlertDialogAnimStyle);
        mView = mInflater.inflate(R.layout.prompt_dialog_container, null);
        mContainer = mView.findViewById(R.id.container);
    }

    public void setUseThemeConfig(boolean b) {
        mThemeConfig = b ? PromptDialogThemeConfig.getInstance() : null;
        mThemeConfig = mThemeConfig != null && mThemeConfig.themeColor != 0 ? mThemeConfig : null;
    }

    public void installContent() {
        mWindow.setContentView(mView);
        setupView();
    }

    public void setMaxHeight(int height){
        this.mMaxHeight = height;
    }

    public void setTitleView(CharSequence title) {
        if (!TextUtils.isEmpty(title)) {
            mTitleView = (TextView) mInflater.inflate(R.layout.prompt_dialog_title, mContainer, false);
            mTitleView.setText(title);
        }
    }

    public void setImageView(Drawable drawable) {
        if (mImageView == null) {
            mImageView = (ImageView) mInflater.inflate(R.layout.prompt_dialog_image, mContainer, false);
        }
        mImageView.setImageDrawable(drawable);
    }

    public ImageView getTipImageView(){
        return mImageView;
    }


    public void setMessageView(CharSequence message) {
        if (!TextUtils.isEmpty(message)) {
            mScrollMessageView = (MaxScrollView) mInflater.inflate(R.layout.prompt_dialog_message, mContainer, false);
            mMessageView = mScrollMessageView.findViewById(R.id.dialog_text_message);
            addScrollChangeListener();
            mMessageView.setText(message);
            if (message instanceof Spannable){
                //ClickableSpan需要有LinkMovementMethod才有效果
                ClickableSpan[] cs =((Spannable) message).getSpans(0,
                        message.length(), ClickableSpan.class);
                if (cs != null && cs.length > 0){
                    mMessageView.setMovementMethod(LinkMovementMethod.getInstance());
                }
            }
        }
    }

    public void setView(int resId, View view) {
        if (resId != 0 || view != null) {
            mFrameContainer = (FrameLayout) mInflater.inflate(R.layout.prompt_dialog_view, mContainer, false);
            View tempView = resId != 0 ? mInflater.inflate(resId, mFrameContainer, false) : view;
            mFrameContainer.addView(tempView);
        }
    }

    public void setProgress() {
        View progressBar = mInflater.inflate(R.layout.prompt_dialog_loading_progress, null);
        mFrameContainer = (FrameLayout) mInflater.inflate(R.layout.prompt_dialog_fullscreen_view, mContainer, false);
        mFrameContainer.addView(progressBar);
    }

    public void setButton(int whichButton, CharSequence text,
                          DialogInterface.OnClickListener listener, Message msg) {
        if (TextUtils.isEmpty(text)) {
            return;
        }

        if (msg == null && listener != null) {
            msg = mHandler.obtainMessage(whichButton, listener);
        }

        switch (whichButton) {
            case DialogInterface.BUTTON_POSITIVE:
                mButtonPositiveText = text;
                mButtonPositiveMessage = msg;
                mHasPositiveButton = true;
                break;
            case DialogInterface.BUTTON_NEGATIVE:
                mButtonNegativeText = text;
                mButtonNegativeMessage = msg;
                mHasNegativeButton = true;
                break;
            case DialogInterface.BUTTON_NEUTRAL:
                mButtonNeutralText = text;
                mButtonNeutralMessage = msg;
                mHasNeutralButton = true;
                break;
            default:
                throw new IllegalArgumentException("Button does not exist");
        }
    }

    public void setList(CharSequence[] items, final DialogInterface.OnClickListener listener) {
        ArrayAdapter<CharSequence> adapter =
                new ArrayAdapter<>(mContext, LIST_ITEM_LAYOUT, LIST_ITEM_TEXT_VIEW, items);
        setList(adapter, listener);
    }

    public void setList(ListAdapter adapter, final DialogInterface.OnClickListener listener) {
        mListView = generateListView(adapter, listener != null ? (parent, view, position, id) -> {
            if (listener != null) {
                listener.onClick(mDialogInterface, position);
                mDialogInterface.dismiss();
            }
        } : null);
    }

    public void setList(Cursor cursor, String labelColumn,
                        final DialogInterface.OnClickListener listener) {
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(mContext,
                LIST_ITEM_LAYOUT, cursor, new String[]{labelColumn},
                new int[]{LIST_ITEM_TEXT_VIEW}, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        setList(adapter, listener);
    }

    public void setSingleChoiceList(CharSequence[] items,
                                    final DialogInterface.OnClickListener listener) {
        CheckedItemAdapter adapter = new CheckedItemAdapter(
                mContext, LIST_ITEM_SINGLE_CHOICE_LAYOUT, LIST_ITEM_ID, new ArrayList<>(Arrays.asList(items)));
        setSingleChoiceList(adapter, listener);
    }

    public void setSingleChoiceList(CharSequence[] items, List<Integer> colors, final DialogInterface.OnClickListener listener) {
        CheckedItemAdapter adapter = new CheckedItemAdapter(mContext, LIST_ITEM_SINGLE_CHOICE_LAYOUT,
                LIST_ITEM_ID, new ArrayList<>(Arrays.asList(items)), colors);
        setSingleChoiceList(adapter, listener);
    }

    public void setSingleChoiceList(ListAdapter adapter,
                                    final DialogInterface.OnClickListener listener) {
        mListView = generateListView(adapter, (parent, view, position, id) -> {
            if (listener != null) {
                listener.onClick(mDialogInterface, position);
            }
        });
        mListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    }

    public void setListItems(List<? extends CharSequence> items){
        if (mListView != null && mListView.getAdapter() instanceof ArrayAdapter){
            ArrayAdapter<CharSequence> adapter = (ArrayAdapter<CharSequence>) mListView.getAdapter();
            adapter.clear();
            adapter.addAll(items);
            adapter.notifyDataSetChanged();
        }
    }

    public void setSingleChoiceList(Cursor cursor, String labelColumn,
                                    final DialogInterface.OnClickListener listener) {
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(mContext,
                LIST_ITEM_SINGLE_CHOICE_LAYOUT, cursor, new String[]{labelColumn},
                new int[]{LIST_ITEM_ID}, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        setSingleChoiceList(adapter, listener);
    }

    public void setMultiChoiceList(CharSequence[] items, boolean[] checkedItems,
                                   final DialogInterface.OnMultiChoiceClickListener listener) {
        dealCheckItems(items, checkedItems);
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(
                mContext, LIST_ITEM_MULTI_CHOICE_LAYOUT, LIST_ITEM_ID, new ArrayList<>(Arrays.asList(items))) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                if (mCheckedItems != null && mListView != null) {
                    boolean isItemChecked = mCheckedItems[position];
                    if (isItemChecked) {
                        mListView.setItemChecked(position, true);
                    }
                }
                return view;
            }
        };
        setMultiChoiceList(adapter, listener);
    }

    private void dealCheckItems(CharSequence[] items, boolean[] checkedItems) {
        if (checkedItems == null) {
            return;
        }
        if (items.length == checkedItems.length) {
            mCheckedItems = checkedItems;
        } else {
            mCheckedItems = new boolean[items.length];
            for (int i = 0; i < items.length; i++) {
                mCheckedItems[i] = i < checkedItems.length && checkedItems[i];
            }
        }
    }

    public void setMultiChoiceList(Cursor cursor,
                                   final String labelColumn, final String isCheckedColumn,
                                   final DialogInterface.OnMultiChoiceClickListener listener) {
        CursorAdapter adapter = new CursorAdapter(mContext, cursor, false) {
            private final int mLabelIndex;
            private final int mIsCheckedIndex;

            {
                final Cursor cursor = getCursor();
                mLabelIndex = cursor.getColumnIndexOrThrow(labelColumn);
                mIsCheckedIndex = cursor.getColumnIndexOrThrow(isCheckedColumn);
            }

            @Override
            public void bindView(View view, Context context, Cursor cursor) {
                CheckedTextView text = view.findViewById(LIST_ITEM_ID);
                text.setText(cursor.getString(mLabelIndex));
                if (mListView != null) {
                    mListView.setItemChecked(
                            cursor.getPosition(), cursor.getInt(mIsCheckedIndex) == 1);
                }
            }

            @Override
            public View newView(Context context, Cursor cursor, ViewGroup parent) {
                return mInflater.inflate(LIST_ITEM_MULTI_CHOICE_LAYOUT, parent, false);
            }
        };
        setMultiChoiceList(adapter, listener);
    }

    private void setMultiChoiceList(ListAdapter adapter,
                                    final DialogInterface.OnMultiChoiceClickListener listener) {
        mListView = generateListView(adapter, (parent, view, position, id) -> {
            if (listener != null && mListView != null) {
                if (mCheckedItems != null) {
                    mCheckedItems[position] = mListView.isItemChecked(position);
                }
                listener.onClick(
                        mDialogInterface, position, mListView.isItemChecked(position));
            }
        });
        mListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
    }

    private MaxListView generateListView(ListAdapter adapter, AdapterView.OnItemClickListener l) {
        MaxListView listView = (MaxListView)
                mInflater.inflate(R.layout.prompt_dialog_list, mContainer, false);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(l);
        return listView;
    }

    public void onPrepareListView(PromptDialogParams.OnPrepareListViewListener listener) {
        if (mListView != null) {
            listener.onPrepareListView(mListView);
        }
    }

    public void setCheckedItem(int checkedItem) {
        mCheckedItem = checkedItem;
        if (mListView != null) {
            mListView.setItemChecked(checkedItem, true);
            mListView.setSelection(checkedItem);
        }
    }

    private void setupView() {
        mContainer.removeAllViews();

        final boolean hasTitle = mTitleView != null;
        final boolean hasMessage = mScrollMessageView != null;
        final boolean hasImage = mImageView != null;
        final boolean hasCustomView = mFrameContainer != null;
        final boolean hasListView = mListView != null;

        if (hasTitle) {
            mContainer.addView(mTitleView);
        }

        if (hasImage) {
            mContainer.addView(mImageView);
        }

        if (hasMessage) {
            if (mMaxHeight > -1) {
                mScrollMessageView.setMaxHeight(mMaxHeight);
            }
            LinearLayout.LayoutParams lp  = generateLinearLayoutParams();
            lp.weight = 1;
            mContainer.addView(mScrollMessageView,lp);
        }

        if (hasListView) {
            if (mMaxHeight > -1) {
                mListView.setMaxHeight(mMaxHeight);
            }
            LinearLayout.LayoutParams lp  = generateLinearLayoutParams();
            lp.weight = 1;
            mContainer.addView(mListView,lp);
        }

        if (hasCustomView) {
            if (mMaxHeight > -1 && mFrameContainer instanceof MaxFrameLayout){
                ((MaxFrameLayout) mFrameContainer).setMaxHeight(mMaxHeight);
            }
            LinearLayout.LayoutParams lp  = generateLinearLayoutParams();
            lp.weight = 1;
            mContainer.addView(mFrameContainer,lp);
        }

        setupButtons(hasCustomView);
    }


    public void setMessageGravity(int gravity) {
        if (mMessageView != null) {
            mMessageView.setGravity(gravity);
        }
    }

    private void setupButtons(boolean hasCustomView) {
        if (!mHasPositiveButton && !mHasNegativeButton && !mHasNeutralButton) {
            return;
        }

        if (hasCustomView && canTextInput(mFrameContainer)) {
            mIsButtonVertical = false;
        } else {
            mWindow.setFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM,
                    WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        }

        View buttonsView = mInflater.inflate(mButtonLayoutRes != 0 ? mButtonLayoutRes :
                        (mIsButtonVertical ?
                                R.layout.prompt_dialog_buttons_horizontal :
                                R.layout.prompt_dialog_buttons_horizontal),
                mContainer, false);

        mButtonPositive = buttonsView.findViewById(R.id.btn_positive);
        if (mHasPositiveButton) {
            mButtonPositive.setText(mButtonPositiveText);
            mButtonPositive.setOnClickListener(mButtonListener);
            if (mThemeConfig != null) {
                mButtonPositive.setTextColor(mThemeConfig.themeColor);
            }
        } else {
            mButtonPositive.setVisibility(View.GONE);
        }

        mButtonNegative = buttonsView.findViewById(R.id.btn_negative);
        if (mHasNegativeButton) {
            mButtonNegative.setText(mButtonNegativeText);
            mButtonNegative.setOnClickListener(mButtonListener);
        } else {
            mButtonNegative.setVisibility(View.GONE);
        }

        mButtonNeutral = buttonsView.findViewById(R.id.btn_neutral);
        if (mHasNeutralButton) {
            mButtonNeutral.setText(mButtonNeutralText);
            mButtonNeutral.setOnClickListener(mButtonListener);
        } else {
            mButtonNeutral.setVisibility(View.GONE);
        }

        mContainer.addView(buttonsView);
    }

    static boolean canTextInput(View v) {
        if (v.onCheckIsTextEditor()) {
            return true;
        }

        if (!(v instanceof ViewGroup)) {
            return false;
        }

        ViewGroup vg = (ViewGroup) v;
        int i = vg.getChildCount();
        while (i > 0) {
            i--;
            v = vg.getChildAt(i);
            if (canTextInput(v)) {
                return true;
            }
        }

        return false;
    }

    public Button getButton(int whichButton) {
        switch (whichButton) {
            case DialogInterface.BUTTON_POSITIVE:
                return mButtonPositive;
            case DialogInterface.BUTTON_NEGATIVE:
                return mButtonNegative;
            case DialogInterface.BUTTON_NEUTRAL:
                return mButtonNeutral;
            default:
                return null;
        }
    }

    public ListView getListView() {
        return mListView;
    }

    public void setTitle(CharSequence title) {
        if (mTitleView != null) {
            TextView textTitle = mTitleView.findViewById(R.id.dialog_text_title);
            textTitle.setText(title);
        } else {
            setTitleView(title);
        }
    }

    public void setButtonAutoDismiss(boolean dismiss){
        this.isAutoDismiss = dismiss;
    }

    private void addScrollChangeListener() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        }
    }

    private LinearLayout.LayoutParams generateLinearLayoutParams(){
        return new LinearLayout.
                LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    private static final class ButtonHandler extends Handler {

        private static final int MSG_DISMISS_DIALOG = 1;

        private WeakReference<DialogInterface> mDialog;

        ButtonHandler(DialogInterface dialog) {
            mDialog = new WeakReference<>(dialog);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DialogInterface.BUTTON_POSITIVE:
                case DialogInterface.BUTTON_NEGATIVE:
                case DialogInterface.BUTTON_NEUTRAL:
                    ((DialogInterface.OnClickListener) msg.obj).onClick(mDialog.get(), msg.what);
                    break;
                case MSG_DISMISS_DIALOG:
                    ((DialogInterface) msg.obj).dismiss();
            }
        }
    }

    private static class CheckedItemAdapter extends ArrayAdapter<CharSequence> {

        @ColorInt
        private List<Integer> colors;

        public CheckedItemAdapter(Context context, int resource, int textViewResourceId,
                                  List<CharSequence> objects) {
            super(context, resource, textViewResourceId, objects);
        }

        public CheckedItemAdapter(Context context, int resource, int textViewResourceId,
                                  List<CharSequence> objects,
                                  @ColorInt List<Integer> colors) {
            super(context, resource, textViewResourceId, objects);
            this.colors = colors;

        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View view = super.getView(position, convertView, parent);
            if (view instanceof TextView) {
                TextView textView = (TextView) view;
                if (colors != null && position >= 0 && position < colors.size()) {
                    textView.setTextColor(colors.get(position));
                }
            }
            return view;
        }


        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }
    }
}
