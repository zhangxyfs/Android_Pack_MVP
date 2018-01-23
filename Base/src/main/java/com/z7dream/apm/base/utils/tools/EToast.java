package com.z7dream.apm.base.utils.tools;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;


/**
 * Created by Z7Dream on 2017/2/13 16:36.
 * Email:zhangxyfs@126.com
 */

public class EToast {
    private Toast toast = null;

    private static EToast instance;

    public static EToast get() {
        if (instance == null) {
            synchronized (EToast.class) {
                if (instance == null) {
                    instance = new EToast();
                }
            }
        }
        return instance;
    }

    public void showToast(Context context, int s) {
        showToast(context, context.getString(s));
    }

    public void showToast(Context context, String s) {
        if (s == null || TextUtils.isEmpty(s) || context == null)
            return;

        s = Utils.handlerPromptContent(s);

        if (toast == null) {
            toast = Toast.makeText(context, s, Toast.LENGTH_SHORT);
            toast.show();
        } else {
            toast.setText(s);
            toast.show();
        }
    }

    public void destory() {
        toast = null;
    }
}
