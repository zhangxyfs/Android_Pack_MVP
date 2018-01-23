package com.z7dream.apm.base.mvp.ui

import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.trello.rxlifecycle2.components.support.RxFragment
import com.z7dream.apm.base.mvp.view.BaseContract
import com.z7dream.apm.base.utils.WeakHandler

/**
 * Created by Z7Dream on 2017/9/6 15:22.
 * Email:zhangxyfs@126.com
 */
abstract class BaseFragment<P : BaseContract.BasePresenter, APP : Application> : RxFragment() {
    private var mApplication: APP? = null;
    private var mWeakHandler: WeakHandler? = null;
    private var mPresenter: P? = null;
    private var mFragmentView: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        before()
        if (mFragmentView == null && layoutID() > 0) {
            mFragmentView = inflater!!.inflate(layoutID(), container, false)
        }
        after(mFragmentView)
        init(mFragmentView, savedInstanceState)
        return mFragmentView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        data()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (mPresenter != null) mPresenter!!.detachView()
        if (mWeakHandler != null) {
            mWeakHandler!!.removeCallbacksAndMessages(null)
            mWeakHandler = null
        }
    }

    override fun onResume() {
        super.onResume()
    }

    protected fun before() {
        mApplication = activity.application as APP
    }

    protected fun after(view: View?) {
        mWeakHandler = WeakHandler()
        mPresenter = createPresenter()
    }

    protected abstract fun layoutID(): Int

    protected abstract fun init(view: View?, savedInstanceState: Bundle?)

    protected abstract fun createPresenter(): P

    protected abstract fun data()

    /**
     * 获取application
     *
     * @return
     */
    protected fun getAppli(): APP? {
        return mApplication
    }

    /**
     * 获取presenter
     *
     * @return
     */
    protected fun getPresenter(): P? {
        return mPresenter
    }

    /**
     * 获取handler
     *
     * @return
     */
    protected fun getHandler(): WeakHandler? {
        return mWeakHandler
    }

    /**
     * 打开activity
     *
     * @param pClass
     */
    protected fun openActivity(pClass: Class<*>) {
        openActivity(pClass, null)
    }

    protected fun openActivity(pClass: Class<*>, pBundle: Bundle?) {
        val intent = Intent(context, pClass)
        if (pBundle != null) {
            intent.putExtras(pBundle)
        }
        startActivity(intent)
    }

    protected fun openActivity(classFullName: String) {
        openActivity(classFullName, null)
    }

    protected fun openActivity(classFullName: String, pBundle: Bundle?) {
        val intent = Intent()
        intent.setClassName(context, classFullName)
        if (pBundle != null) {
            intent.putExtras(pBundle)
        }
        startActivity(intent)
    }

    protected fun userToolbar(toolbar: Toolbar, title: String?, clickListener: View.OnClickListener?) {
        getCompatActivity().setSupportActionBar(toolbar)
        if (getCompatActivity().getSupportActionBar() != null) {
            getCompatActivity().getSupportActionBar()!!.setDisplayHomeAsUpEnabled(true)
            getCompatActivity().getSupportActionBar()!!.setTitle(title)
        }
        if (clickListener == null) {
            toolbar.setNavigationOnClickListener { activity.finish() }
        } else {
            toolbar.setNavigationOnClickListener(clickListener)
        }
    }

    protected fun closeToolbarBack() {
        if (getCompatActivity().getSupportActionBar() != null) {
            getCompatActivity().getSupportActionBar()!!.setDisplayHomeAsUpEnabled(false)
        }
    }

    protected fun userToolbar(toolbar: Toolbar, resId: Int, clickListener: View.OnClickListener?) {
        getCompatActivity().setSupportActionBar(toolbar)
        if (getCompatActivity().getSupportActionBar() != null) {
            getCompatActivity().getSupportActionBar()!!.setDisplayHomeAsUpEnabled(true)
            getCompatActivity().getSupportActionBar()!!.setTitle(resId)
        }
        if (clickListener == null) {
            toolbar.setNavigationOnClickListener { activity.finish() }
        } else {
            toolbar.setNavigationOnClickListener(clickListener)
        }
    }

    protected fun userToolbar(toolbar: Toolbar, title: String) {
        userToolbar(toolbar, title, null)
    }

    protected fun userToolbar(toolbar: Toolbar, resId: Int) {
        userToolbar(toolbar, resId, null)
    }

    protected fun userToolbar(toolbar: Toolbar) {
        userToolbar(toolbar, null, null)
    }

    protected fun setToolbarTitle(title: String) {
        if (getCompatActivity().getSupportActionBar() != null) {
            getCompatActivity().getSupportActionBar()!!.setTitle(title)
        }
    }

    protected fun setToolbarTitle(resId: Int) {
        if (getCompatActivity().getSupportActionBar() != null) {
            getCompatActivity().getSupportActionBar()!!.setTitle(resId)
        }
    }

    protected fun getCompatActivity(): AppCompatActivity {
        return activity as AppCompatActivity
    }


}