package com.z7dream.apm.base.mvp.presenter

import android.content.Context
import android.widget.Toast
import com.z7dream.apm.base.mvp.model.BaseService
import com.z7dream.apm.base.mvp.view.BaseContract

/**
 * P 层实现
 *
 * Created by Z7Dream on 2017/9/6 10:26.
 * Email:zhangxyfs@126.com
 */
abstract class Presenter<V : BaseContract.BaseView, M : BaseService>(context: Context, view: V) : BaseContract.BasePresenter {
    private var mView: V? = view;
    private var mContext: Context? = context;
    private var mService: M? = createRepository();

    abstract fun createRepository(): M;

    protected fun getView(): V? = mView

    protected fun getContext(): Context? = mContext

    protected fun getService(): M? = mService

    override fun detachView() {
        mView = null;
        mContext = null;
        mService = null;
    }

    protected fun showToast(resId: Int) {
        showToast(getContext()?.resources?.getString(resId) ?: "");
    }

    protected fun showToast(s: String) {
        if (getContext() == null) return
        Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
    }
}