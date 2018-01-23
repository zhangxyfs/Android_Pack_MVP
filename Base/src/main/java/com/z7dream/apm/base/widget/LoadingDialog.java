
package com.z7dream.apm.base.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.z7dream.apm.base.R;
import com.wang.avi.AVLoadingIndicatorView;
import com.wang.avi.Indicator;

/**
 * @author DingLei
 * @version : v1.0
 * @Description : 加载对话框控件
 * @date 2013-10-23
 */
public class LoadingDialog extends Dialog {

    private TextView tv_loading_text;
    private AVLoadingIndicatorView aliv_loading;
    private ImageView iv_loading_success;
    private ImageView iv_loading_fail;
    private final View layout;


    public static LoadingDialog getInstance(Context context, String loadingText) {
        return getInstance(context, loadingText, true, true);
    }

    public static LoadingDialog getInstance(Context context, boolean cancelable, boolean outeSiteCanceled) {
        return getInstance(context, null, cancelable, outeSiteCanceled);
    }

    public static LoadingDialog getInstance(Context context, String loadingText, boolean cancelable, boolean outeSiteCanceled) {
        LoadingDialog dialog = new LoadingDialog(context);

        if (!TextUtils.isEmpty(loadingText)) {
            dialog.setContent(loadingText);
        }
        dialog.setCanceledOnTouchOutside(outeSiteCanceled);
        dialog.setCancelable(cancelable);
        return dialog;
    }

    public LoadingDialog(Context context) {
        super(context, R.style.LoadingDialog);

        layout = LayoutInflater.from(context).inflate(R.layout.widget_loadingdialog, null);
        tv_loading_text = layout.findViewById(R.id.tv_loading_text);
        aliv_loading = layout.findViewById(R.id.aliv_loading);
        iv_loading_success = layout.findViewById(R.id.iv_loading_success);
        iv_loading_fail = layout.findViewById(R.id.iv_loading_fail);
        setContentView(layout);
    }

    public void setLoadingView(Indicator indicator) {

        aliv_loading.setIndicator(indicator);
        ViewGroup.LayoutParams layoutParams = aliv_loading.getLayoutParams();
        layoutParams.width = 100/*((int) Utils.convertPxOrDip(getContext(), 50))*/;
        layoutParams.height = 100/*((int) Utils.convertPxOrDip(getContext(), 50))*/;
        aliv_loading.setLayoutParams(layoutParams);
        layout.setBackgroundColor(Color.parseColor("#00000000"));

    }

    public void setLoadingViewColor(int id) {
        aliv_loading.setIndicatorColor(getContext().getResources().getColor(id));
    }

    public void setContent(final String content) {
        if (tv_loading_text.getVisibility() != View.VISIBLE) {
            tv_loading_text.setVisibility(View.VISIBLE);
        }

        if (!TextUtils.isEmpty(content)) {
            tv_loading_text.setText(content);
        }
    }

    public void setContent(int resId) {
        if (tv_loading_text.getVisibility() != View.VISIBLE) {
            tv_loading_text.setVisibility(View.VISIBLE);
        }
        tv_loading_text.setText(resId);
    }

    public void setLoading(int resId) {
        setLoading(getContext().getString(resId));
    }

    public void setLoading(String text) {
        setContent(text);
        if (aliv_loading.getVisibility() == View.GONE) {
            aliv_loading.setVisibility(View.VISIBLE);
            aliv_loading.show();
        }
        iv_loading_success.setVisibility(View.GONE);
        iv_loading_fail.setVisibility(View.GONE);
    }

    public void startLoading() {
        startLoading("");
    }


    public void startLoading(String txt) {
        if (aliv_loading.getVisibility() == View.GONE) {
            aliv_loading.setVisibility(View.VISIBLE);
            aliv_loading.show();
        }
        iv_loading_success.setVisibility(View.GONE);
        iv_loading_fail.setVisibility(View.GONE);
        tv_loading_text.setText(txt);

        if (!isShowing())
            show();
    }

    public void setLoadingResult(String text, int duration) {
        setLoadingResult(text, duration, null);
    }

    public void setLoadingResult(String text, int duration, OnDissListener onDismissListener) {
        startLoading(text);
        if (duration > 0)
            new Handler().postDelayed(() -> {
                if (isShowing())
                    dismiss();
                if (onDismissListener != null) {
                    onDismissListener.dissComplete();
                }
            }, duration);
    }

    public void setResult(boolean isSucc, String text) {
        setResult(isSucc, text, 0, null);
    }

    public void setResult(boolean isSucc, String text, int duration) {
        setResult(isSucc, text, duration, null);
    }

    public void setResult(boolean isSucc, String text, int duration, OnDissListener onDismissListener) {
        try {
            if (duration <= 0 && isShowing()) {
                dismiss();
            }
            if (!isShowing())
                show();

            aliv_loading.setVisibility(View.GONE);
            aliv_loading.hide();
            iv_loading_success.setVisibility(isSucc ? View.VISIBLE : View.GONE);
            iv_loading_fail.setVisibility(isSucc ? View.GONE : View.VISIBLE);
            setContent(text);

            if (duration > 0)
                new Handler().postDelayed(() -> {
                    if (isShowing())
                        dismiss();
                    if (onDismissListener != null) {
                        onDismissListener.dissComplete();
                    }
                }, duration);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void destory() {
        tv_loading_text = null;
        aliv_loading = null;
        iv_loading_success = null;
        iv_loading_fail = null;
    }

    public interface OnDissListener {
        void dissComplete();
    }
}
