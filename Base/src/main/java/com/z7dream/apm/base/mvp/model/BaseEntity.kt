package com.z7dream.apm.base.mvp.model

import android.text.TextUtils

/**
 * Created by Z7Dream on 2017/9/6 13:26.
 * Email:zhangxyfs@126.com
 */
open class BaseEntity<T> {
    var result: String? = null;
    var code: String? = null;
    var message: String? = null;

    var data: T? = null;

    fun isOK(): Boolean = TextUtils.equals(code, "0000");
}