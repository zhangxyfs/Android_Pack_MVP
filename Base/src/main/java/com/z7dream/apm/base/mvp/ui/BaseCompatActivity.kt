package com.z7dream.apm.base.mvp.ui

import android.app.Application
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.v4.content.ContextCompat
import android.support.v7.widget.ActionMenuView
import android.support.v7.widget.Toolbar
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import com.z7dream.apm.base.BuildConfig
import com.z7dream.apm.base.R
import com.z7dream.apm.base.mvp.view.BaseContract
import com.z7dream.apm.base.utils.WeakHandler
import com.z7dream.apm.base.utils.rx.RxBus
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import java.util.*

/**
 * yo
 *
 * Created by Z7Dream on 2017/10/27 10:22.
 * Email:zhangxyfs@126.com
 */
abstract class BaseCompatActivity<P : BaseContract.BasePresenter, APP : Application> : RxAppCompatActivity() {
    private var mApplication: APP? = null;
    private var mWeakHandler: WeakHandler? = null;
    private var mPresenter: P? = null;

    private var mIsNeedGoneNavigationBar: Boolean = false
    private var mExitPressedTime: Long = 0

    protected fun toOnCreate(savedInstanceState: Bundle?) {
        before()
        if (layoutID() > 0) setContentView(layoutID())
        after()
        init(savedInstanceState)
        data()
        if (BuildConfig.DEBUG) Log.d(this::class.java.name, "toOnCreate() layout id=" + layoutID())
    }

    fun toOnStart() {
        if (BuildConfig.DEBUG) Log.d(this::class.java.name, "toOnStart()")
    }

    fun toOnResume() {
        if (BuildConfig.DEBUG) Log.d(this::class.java.name, "toOnResume()")
    }

    fun toOnPause() {
        if (BuildConfig.DEBUG) Log.d(this::class.java.name, "toOnPause()")
    }

    fun toOnStop() {
        if (BuildConfig.DEBUG) Log.d(this::class.java.name, "toOnStop()")
    }

    fun toOnDestroy() {
        try {
            if (mWeakHandler != null) {
                mWeakHandler!!.removeCallbacksAndMessages(null)
                mWeakHandler = null
            }
            if (mPresenter != null) mPresenter!!.detachView()
            if (mApplication != null) mApplication = null
        } catch (e: Exception) {
            e.printStackTrace()
        }
        if (BuildConfig.DEBUG) Log.d(this::class.java.name, "toOnDestroy()")
    }

    @CallSuper
    open protected fun before() {
        mApplication = application as APP;
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        if (BuildConfig.DEBUG) Log.d(this::class.java.name, "before()")
    }

    @CallSuper
    open protected fun after() {
        mWeakHandler = WeakHandler();
        if (mPresenter == null) mPresenter = createPreseneter();
        if (mIsNeedGoneNavigationBar) toHideNav()
        if (BuildConfig.DEBUG) Log.d(this::class.java.name, "after()")

    }

    protected abstract fun init(savedInstanceState: Bundle?);

    @CallSuper
    protected fun data() {
        if (BuildConfig.DEBUG) Log.d(this::class.java.name, "data()")
    }

    protected abstract fun createPreseneter(): P?;

    protected abstract fun layoutID(): Int;

    /**
     * 获取presenter
     *
     * @return
     */
    protected fun getPresenter(): P? {
        return mPresenter
    }

    protected fun setPresenter() {
        if (mPresenter == null)
            mPresenter = createPreseneter()
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
     * 设置是否需要不显示导航条（必须写子before里，默认为false）
     * @return
     */
    protected fun setIsNeedGoneNavigationBar(b: Boolean) {
        mIsNeedGoneNavigationBar = b
    }

    protected fun userToolbar(toolbar: Toolbar, title: String) {
        userToolbar(toolbar, title, null)
    }

    protected fun userToolbar(toolbar: Toolbar, resId: Int) {
        userToolbar(toolbar, resId, null)
    }

    protected fun userToolbar(toolbar: Toolbar, clickListener: View.OnClickListener?) {
        userToolbar(toolbar, null, clickListener)
    }

    protected fun userToolbar(toolbar: Toolbar, resId: Int, clickListener: View.OnClickListener?) {
        setSupportActionBar(toolbar)
        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setTitle(resId)
        }
        if (clickListener == null) {
            toolbar.setNavigationOnClickListener { finish() }
        } else {
            toolbar.setNavigationOnClickListener(clickListener)
        }
    }

    protected fun userToolbar(toolbar: Toolbar, title: String?, clickListener: View.OnClickListener?) {
        setSupportActionBar(toolbar)
        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)

            supportActionBar!!.title = title
        }
        if (clickListener == null) {
            toolbar.setNavigationOnClickListener { finish() }
        } else {
            toolbar.setNavigationOnClickListener(clickListener)
        }
    }

    protected fun userToolbarNoBack(toolbar: Toolbar, title: String?) {
        setSupportActionBar(toolbar)
        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(false)

            supportActionBar!!.title = title
        }
    }

    protected fun userToolbarNoBack(toolbar: Toolbar, resId: Int) {
        setSupportActionBar(toolbar)
        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(false)

            supportActionBar!!.setTitle(resId)
        }
    }

    /**
     * 绑定actionBar
     */
    protected fun userToolbar(toolbar: Toolbar) {
        userToolbar(toolbar, null, null)
    }

    /**
     * 设置toolbar文本
     */
    protected fun setToolbarTitle(title: String) {
        if (supportActionBar != null) {
            supportActionBar!!.title = title
        }
    }

    /**
     * 设置toolbar文本
     */
    protected fun setToolbarTitle(resId: Int) {
        if (supportActionBar != null) {
            supportActionBar!!.setTitle(resId)
        }
    }

    /**
     * 设置toolbar 文本的文字省略方式
     */
    protected fun setToolbarTextViewEllipsize(toolbar: Toolbar, truncateAt: TextUtils.TruncateAt?) {
        val cls = toolbar.javaClass
        try {
            val field = cls.getDeclaredField("mTitleTextView")
            field.isAccessible = true
            val tv = field.get(toolbar) as TextView
            if (truncateAt != null)
                tv.ellipsize = truncateAt
            else
                tv.ellipsize = TextUtils.TruncateAt.MIDDLE;
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }
    }

    // 判断权限集合
    protected fun needPermissions(vararg permissions: String): Boolean {
        //判断版本是否兼容
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return false
        }
        var isNeed: Boolean
        for (permission in permissions) {
            isNeed = needsPermission(permission)
            if (isNeed) {
                return true
            }
        }
        return false
    }

    protected fun noPermissions(vararg permissions: String): List<String> {
        val list = ArrayList<String>()
        for (permission in permissions) {
            if (needPermissions(permission)) {
                list.add(permission)
            }
        }
        return list
    }

    // 判断是否缺少权限
    protected fun needsPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(applicationContext, permission) != PackageManager.PERMISSION_GRANTED
    }

    protected fun getToolbarMenuView(toolbar: Toolbar): ActionMenuView? {
        var view: ActionMenuView? = null
        val cls = toolbar.javaClass
        try {
            val field = cls.getDeclaredField("mMenuView")
            field.isAccessible = true
            view = field.get(toolbar) as ActionMenuView
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }

        return view
    }

    protected fun getAppli(): APP? {
        return mApplication;
    }

    /**
     * 打开activity
     *
     * @param pClass
     * @param key
     * @param value
     */
    protected fun openActivity(pClass: Class<*>, key: String, value: Any) {
        val bundle = Bundle()
        if (value is String) {
            bundle.putString(key, value)
        } else if (value is Int) {
            bundle.putInt(key, value)
        } else if (value is Boolean) {
            bundle.putBoolean(key, value)
        }
        openActivity(pClass, bundle)
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
        val intent = Intent(this, pClass)
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
        intent.setClassName(baseContext, classFullName)
        if (pBundle != null) {
            intent.putExtras(pBundle)
        }
        startActivity(intent)
    }

    /**
     * 双击退出。
     */
    protected fun exitBy2Click(): Boolean {
        val mNowTime = System.currentTimeMillis()//获取第一次按键时间
        if (mNowTime - mExitPressedTime > 2000) {//比较两次按键时间差
            Toast.makeText(this, getString(R.string.nav_back_again_finish), Toast.LENGTH_SHORT).show()
            mExitPressedTime = mNowTime
        } else {
            RxBus.get()?.destory()
            finish()
            exitExecute()
            return true
        }
        return false
    }

    /**
     * 退出时候执行
     */
    open protected fun exitExecute() {

    }


    private fun toHideNav() {
        mWeakHandler!!.post(mHideRunnable)
        val decorView = window.decorView
        decorView.setOnSystemUiVisibilityChangeListener {
            mWeakHandler!!.post(mHideRunnable) // hide the navigation bar
        }
    }

    private var mHideRunnable: Runnable = Runnable { window.decorView.systemUiVisibility = getHideFlags() }

    private fun getHideFlags(): Int {
        val flags: Int
        val curApiVersion = Build.VERSION.SDK_INT
        // This work only for android 4.4+
        if (curApiVersion >= Build.VERSION_CODES.KITKAT) {
            // This work only for android 4.4+
            // hide navigation bar permanently in android activity
            // touch the screen, the navigation bar will not show
            flags = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE
            //                    | View.SYSTEM_UI_FLAG_FULLSCREEN;
        } else {
            // touch the screen, the navigation bar will show
            flags = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        }
        return flags
    }
}