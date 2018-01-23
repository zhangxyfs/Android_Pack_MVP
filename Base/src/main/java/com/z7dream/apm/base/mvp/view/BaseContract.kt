package com.z7dream.apm.base.mvp.view

import com.trello.rxlifecycle2.LifecycleTransformer

/**
 * Created by Z7Dream on 2017/9/6 10:11.
 * Email:zhangxyfs@126.com
 */
interface BaseContract {
    interface BaseView {
        fun <T>bindToLifecycle() : LifecycleTransformer<T>;
    }

    interface BasePresenter {
        fun detachView();
    }
}