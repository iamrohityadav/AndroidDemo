package com.mainli.d.d2018.dialog;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.mainli.d.d2018.R;

/**
 * Created by Mainli on 2018-3-29.
 * <p>
 * 简单的DialogFragment封装 支持自定义布局
 * <p>
 * 示例用法(kotlin) - simple
 * AppDialogFragment.Build(this)
 * .icon(R.mipmap.app_icon)
 * .title("nihao")
 * .contentText("啊是打算打算")
 * .addDefaultButton(R.string.crash_cancel)
 * .addDefaultButton("OK", object : View.OnClickListener {
 * override fun onClick(v: View?) {
 * ToastUtils.getInstance().showToast("hello")
 * }
 * }, AppDialogFragment.ButtonOperator { it.setBackgroundColor(Color.RED) })
 * .show(this, "aaa")
 * <p>
 * 示例用法(kotlin) - 自定义内容布局与按钮样式:
 * AppDialogFragment.Build(this)
 * .icon(R.mipmap.app_icon)
 * .title("nihao")
 * .setContentLayout(R.layout.dialog_zdy).setDialogOperator(object : AppDialogFragment.DialogOperator { //设置自定义布局,在DialogOperator中初始化
 * override fun operate(dialog: AppDialogFragment, rootView: View) {
 * rootView.findViewById<TextView>(R.id.tv1).setText("文本1")
 * rootView.findViewById<TextView>(R.id.tv2).setText("文本2")
 * rootView.findViewById<TextView>(R.id.tv3).setText("文本3")
 * }
 * })
 * .addDefaultButton("OK", object : View.OnClickListener {
 * override fun onClick(v: View?) {
 * ToastUtils.getInstance().showToast("hello")
 * }
 * }, AppDialogFragment.ButtonOperator { it.setBackgroundColor(Color.RED) })
 * .show(this, "aaa")
 */
public final class AppDialogFragment extends DialogFragment implements View.OnClickListener {
    private static final String TAG = "AppDialog";

    private DialogData data;//保留创建时Context 初始化时设置主题会被包裹为ContextThemeWrapper

    private void setData(DialogData data) {
        this.data = data;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, R.style.AppDialog);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_app_default, container, true);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        autoCancelable();

        initTop(view);
        initContent(view);
        initBottom(view);

        if (data.operator != null) {
            data.operator.operate(this, view);
        }
    }

    private void autoCancelable() {
        if (TextUtils.isEmpty(data.firstButtonText) && TextUtils.isEmpty(data.secondButtonText) && TextUtils.isEmpty(data.thirdButtonText)) {
            setCancelable(true);
        } else {
            setCancelable(data.cancelable);
        }
    }

    private void initTop(View view) {
        if (data.topLayout != data.INVALID_LAYOUT) {
            ViewStub titleStub = view.findViewById(R.id.dialog_title_stub);
            titleStub.setLayoutResource(data.topLayout);
            titleStub.inflate();
        } else {
            ViewStub titleStub = view.findViewById(R.id.dialog_title_stub);
            View root = titleStub.inflate();
            if (data.icon != null) {
                ImageView icon = root.findViewById(R.id.dialog_icon);
                icon.setImageDrawable(data.icon);
            }
            if (!TextUtils.isEmpty(data.title)) {
                TextView titleView = root.findViewById(R.id.dialog_title);
                titleView.setText(data.title);
            }

        }
    }

    private void initContent(View view) {
        if (data.contentLayout != data.INVALID_LAYOUT) {
            ViewStub contentStub = view.findViewById(R.id.dialog_content_stub);
            contentStub.setLayoutResource(data.contentLayout);
            contentStub.inflate();
        } else if (!TextUtils.isEmpty(data.contentText)) {
            ViewStub contentStub = view.findViewById(R.id.dialog_content_stub);
            View root = contentStub.inflate();
            TextView title = root.findViewById(R.id.dialog_content_root);
            title.setText(data.contentText);
        }
    }

    private void initBottom(View view) {
        if (data.bottomLayout != data.INVALID_LAYOUT) {
            ViewStub bottomStub = view.findViewById(R.id.dialog_bottom_stub);
            bottomStub.setLayoutResource(data.contentLayout);
            bottomStub.inflate();
        } else {
            ViewStub bottomStub = view.findViewById(R.id.dialog_bottom_stub);
            View root = bottomStub.inflate();
            initDefaultButton(root, R.id.dialog_first_btn, data.firstButtonText, data.firstButtonOperator);
            initDefaultButton(root, R.id.dialog_second_btn, data.secondButtonText, data.secondButtonOperator);
            initDefaultButton(root, R.id.dialog_third_btn, data.thirdButtonText, data.thirdButtonOperator);

        }
    }

    private void initDefaultButton(View root, @IdRes int id, CharSequence text, ButtonOperator operator) {
        if (!TextUtils.isEmpty(text)) {
            Button thirdBtn = root.findViewById(id);
            thirdBtn.setVisibility(View.VISIBLE);
            thirdBtn.setOnClickListener(AppDialogFragment.this);
            thirdBtn.setText(text);
            if (operator != null) {
                operator.operate(thirdBtn);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_first_btn:
                if (data.firstButtonOnClick != null) {
                    data.firstButtonOnClick.onClick(v);
                }
                break;
            case R.id.dialog_second_btn:
                if (data.secondButtonOnClick != null) {
                    data.secondButtonOnClick.onClick(v);
                }
                break;
            case R.id.dialog_third_btn:
                if (data.thirdButtonOnClick != null) {
                    data.thirdButtonOnClick.onClick(v);
                }
                break;
        }
        dismiss();
    }


    @Override
    public void setCancelable(boolean flag) {
        super.setCancelable(flag);
        if (data.cancelable != flag) {
            data.cancelable = flag;
        }
    }

    public static class DialogData {
        Context mContext;

        public DialogData(Context context) {
            mContext = context;
        }

        int INVALID_LAYOUT = -99;
        //自定义布局
        @LayoutRes
        int topLayout = INVALID_LAYOUT;
        @LayoutRes
        int contentLayout = INVALID_LAYOUT;
        @LayoutRes
        int bottomLayout = INVALID_LAYOUT;


        CharSequence title = null;
        CharSequence contentText = null;
        Drawable icon = null;
        CharSequence firstButtonText = null;
        CharSequence secondButtonText = null;
        CharSequence thirdButtonText = null;

        ButtonOperator firstButtonOperator = null;
        ButtonOperator secondButtonOperator = null;
        ButtonOperator thirdButtonOperator = null;

        View.OnClickListener firstButtonOnClick;
        View.OnClickListener secondButtonOnClick;
        View.OnClickListener thirdButtonOnClick;

        boolean cancelable = false;

        AppDialogFragment.DialogOperator operator = null;

    }


    public static class Build {

        private AppDialogFragment.DialogData data;

        public Build(Context context) {
            data = new AppDialogFragment.DialogData(context);
        }

        public AppDialogFragment.Build title(String title) {
            data.title = title;
            return this;
        }

        public AppDialogFragment.Build title(@StringRes int titleId) {
            data.title = data.mContext.getString(titleId);
            return this;
        }

        public AppDialogFragment.Build icon(Drawable icon) {
            data.icon = icon;
            return this;
        }

        public AppDialogFragment.Build icon(Bitmap bitmap) {
            data.icon = new BitmapDrawable(data.mContext.getResources(), bitmap);
            return this;
        }

        public AppDialogFragment.Build icon(@DrawableRes int icon) {
            data.icon = ContextCompat.getDrawable(data.mContext, icon);
            return this;
        }

        public AppDialogFragment.Build contentText(CharSequence contentText) {
            data.contentText = contentText;
            return this;
        }
        //---------------------自定义按钮区start--------------------------------------------------------------------------------------------------

        public AppDialogFragment.Build addDefaultButton(@StringRes int buttonTextId) {
            return addDefaultButton(data.mContext.getText(buttonTextId), null, null);
        }


        public AppDialogFragment.Build addDefaultButton(@StringRes int buttonTextId, ButtonOperator operator) {
            return addDefaultButton(data.mContext.getText(buttonTextId), null, operator);
        }


        public AppDialogFragment.Build addDefaultButton(@StringRes int buttonTextId, View.OnClickListener listener) {
            return addDefaultButton(data.mContext.getText(buttonTextId), listener, null);
        }

        public AppDialogFragment.Build addDefaultButton(CharSequence buttonText) {
            return addDefaultButton(buttonText, null, null);
        }


        public AppDialogFragment.Build addDefaultButton(CharSequence buttonText, ButtonOperator operator) {
            return addDefaultButton(buttonText, null, operator);
        }


        public AppDialogFragment.Build addDefaultButton(CharSequence buttonText, View.OnClickListener listener) {
            return addDefaultButton(buttonText, listener, null);
        }


        public AppDialogFragment.Build addDefaultButton(CharSequence buttonText, View.OnClickListener listener, ButtonOperator operator) {
            if (TextUtils.isEmpty(data.firstButtonText)) {
                return setDefaultButton(DefaultDialogButtonType.FIRST_BUTTON, buttonText, listener, operator);
            } else if (TextUtils.isEmpty(data.secondButtonText)) {
                return setDefaultButton(DefaultDialogButtonType.SECOND_BUTTON, buttonText, listener, operator);
            } else if (TextUtils.isEmpty(data.thirdButtonText)) {
                return setDefaultButton(DefaultDialogButtonType.THIRD_BUTTON, buttonText, listener, operator);
            }
            throw new RuntimeException(TAG + " - 默认按钮最多三个,要使用多个按钮请调用setBottomLayout方法自定义布局");
        }

        /*@link DefaultButtonType}*/
        private AppDialogFragment.Build setDefaultButton(@DefaultDialogButtonType int buttonType, CharSequence buttonText, View.OnClickListener listener, ButtonOperator operator) {
            switch (buttonType) {
                case DefaultDialogButtonType.FIRST_BUTTON:
                    data.firstButtonText = buttonText;
                    data.firstButtonOnClick = listener;
                    data.firstButtonOperator = operator;
                    break;
                case DefaultDialogButtonType.SECOND_BUTTON:
                    data.secondButtonText = buttonText;
                    data.secondButtonOnClick = listener;
                    data.secondButtonOperator = operator;
                    break;
                case DefaultDialogButtonType.THIRD_BUTTON:
                    data.thirdButtonText = buttonText;
                    data.thirdButtonOnClick = listener;
                    data.thirdButtonOperator = operator;
                    break;
                default:
                    Log.d(TAG, "button-type: 未知,默认按钮添加失败");
            }
            return this;
        }

        //---------------------自定义按钮区end--------------------------------------------------------------------------------------------------
        public AppDialogFragment.Build cancelable(boolean cancelable) {
            data.cancelable = cancelable;
            return this;
        }

        /**
         * 自定义布局,请使用{@link DialogOperator}进行初始化操作
         *
         * @param titleLayout
         * @return
         */
        public AppDialogFragment.Build setTopLayout(int titleLayout) {
            data.topLayout = titleLayout;
            return this;
        }

        /**
         * 自定义布局,请使用{@link DialogOperator}进行初始化操作
         *
         * @param contentLayout
         * @return
         */
        public AppDialogFragment.Build setContentLayout(int contentLayout) {
            data.contentLayout = contentLayout;
            return this;
        }

        /**
         * 自定义布局,请使用{@link DialogOperator}进行初始化操作
         *
         * @param buttonLayout
         * @return
         */
        public AppDialogFragment.Build setBottomLayout(int buttonLayout) {
            data.bottomLayout = buttonLayout;
            return this;
        }


        public AppDialogFragment.Build setDialogOperator(AppDialogFragment.DialogOperator operator) {
            data.operator = operator;
            return this;
        }

        public AppDialogFragment create() {
            AppDialogFragment AppDialogFragment = new AppDialogFragment();
            AppDialogFragment.setData(data);
            return AppDialogFragment;
        }

        public AppDialogFragment show(FragmentActivity activity, String tag) {
            AppDialogFragment AppDialogFragment = create();
            AppDialogFragment.show(activity.getSupportFragmentManager(), tag);
            return AppDialogFragment;
        }

        public AppDialogFragment show(Fragment fragment, String tag) {
            AppDialogFragment AppDialogFragment = create();
            AppDialogFragment.show(fragment.getChildFragmentManager(), tag);
            return AppDialogFragment;
        }

        public AppDialogFragment show(FragmentManager manager, String tag) {
            AppDialogFragment AppDialogFragment = create();
            AppDialogFragment.show(manager, tag);
            return AppDialogFragment;
        }

        public AppDialogFragment show(FragmentTransaction transaction, String tag) {
            AppDialogFragment AppDialogFragment = create();
            AppDialogFragment.show(transaction, tag);
            return AppDialogFragment;
        }


        public void activitySafetyShow(String tag) {
            if (data.mContext != null && data.mContext instanceof FragmentActivity) {
                FragmentActivity activity = (FragmentActivity) data.mContext;
                if (!((FragmentActivity) data.mContext).isFinishing()) {
                    AppDialogFragment AppDialogFragment = create();
                    AppDialogFragment.show(activity.getSupportFragmentManager(), tag);
                }
            }
        }

    }

    public interface DialogOperator {
        void operate(AppDialogFragment dialog, View rootView);
    }

    public interface ButtonOperator {
        void operate(Button button);
    }


}
