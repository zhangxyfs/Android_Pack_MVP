package com.z7dream.apm.mvp.view.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.view.WindowManager
import com.z7dream.apm.R
import com.z7dream.apm.base.widget.draggabledot.DraggableLayout
import com.z7dream.apm.base.widget.navigation.BottomNavigationBar
import com.z7dream.apm.mvp.base.PluginBaseActivity
import com.z7dream.apm.mvp.presenter.MainPageBottomPresenter
import com.z7dream.apm.mvp.view.contract.MainPageBottomContract
import com.z7dream.apm.mvp.view.model.MainModel
import com.z7dream.apm.widget.MainTabManager
import io.reactivex.Observable

/**
 * Created by Z7Dream on 2017/10/30 17:24.
 * Email:zhangxyfs@126.com
 */
class MainPageBottomActivity : PluginBaseActivity<MainPageBottomContract.Presenter>(), MainPageBottomContract.View, BottomNavigationBar.OnCheckedItemListener, BottomNavigationBar.OnDotNumClearListener {
    private var mFragmentManager: FragmentManager? = null
    private var mContentFragment: Fragment? = null

    private var closeMainObservable: Observable<Boolean>? = null
    private var isOnlyClose: Boolean = false
    private var isStartOSSService: Boolean = false
    private var toLogin = 0//0 为不去登录，1为调用登录
    val TO_LOGIN = "to_login"

    override fun layoutID(): Int = R.layout.activity_main_page_bottom
    override fun createPreseneter(): MainPageBottomContract.Presenter = MainPageBottomPresenter(this, this)

    override fun after() {
        super.after()
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
        }
    }

    override fun init(savedInstanceState: Bundle?) {
        mFragmentManager = supportFragmentManager
        val transaction = mFragmentManager?.beginTransaction()
        mContentFragment = MainTabManager.getInstance().getFragmentByIndex(R.id.nav_center)

        if (mContentFragment != null) {
            transaction?.add(R.id.fl_main_content, mContentFragment)
            transaction?.commitAllowingStateLoss()
        }

//        UpdateDialog(this, false)
        DraggableLayout.attachToActivity(this)

        initRxObservable()

        toInit(intent)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        toInit(intent)
    }

    private fun toInit(intent: Intent?) {
        if (intent != null) {
            toLogin = intent.getIntExtra(TO_LOGIN, 0)
        }

        if (toLogin == 1) {
            toLogin = 0
            openActivity(LoginActivity::class.java)
            finish()
        }
    }

    override fun getDataSucc(modelList: List<MainModel>, isRef: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getDataFail(str: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun checkItem(position: Int, resId: Int) {
        var switchID = -1
        when (position) {
            0//左1
            -> {
                switchID = R.id.nav_left_first
//                AppStats.addEventStats(this, StatsConstant.Event.EID_1000)
            }
            1//左2
            -> {
                switchID = R.id.nav_left_second
//                AppStats.addEventStats(this, StatsConstant.Event.EID_2000)
            }
            2//中间
            -> {
                switchID = R.id.nav_center
//                AppStats.addEventStats(this, StatsConstant.Event.EID_3000)
            }
            3//左3
            -> {
                switchID = R.id.nav_right_first
//                AppStats.addEventStats(this, StatsConstant.Event.EID_4000)
            }
            4//左4
            -> {
                switchID = R.id.nav_right_second
//                AppStats.addEventStats(this, StatsConstant.Event.EID_5000)
            }
        }
        todo(position)
        switchFragment(MainTabManager.getInstance().getFragmentByIndex(switchID))
    }

    override fun doubleClickItem(position: Int, resId: Int) {
        when (position) {
//            0 -> RxBus.get()?.post(MAIN_BOTTOM_NAVIGATION_DOUBLE_CLICK_LEFT_FIRST, true)
//            1 -> RxBus.get()?.post(MAIN_BOTTOM_NAVIGATION_DOUBLE_CLICK_LEFT_SEC, true)
//            2 -> {
//            }
//            3 -> RxBus.get()?.post(MAIN_BOTTOM_NAVIGATION_DOUBLE_CLICK_RIGHT_FIRST, true)
//            4 -> RxBus.get()?.post(MAIN_BOTTOM_NAVIGATION_DOUBLE_CLICK_RIGHT_SEC, true)
        }
    }

    override fun dotNumClear(index: Int) {
//        if (index == 0)
//            getPresenter()?.messageNumClear()
    }

    private fun switchFragment(to: Fragment) {
        if (mContentFragment !== to) {
            val transaction = mFragmentManager?.beginTransaction()?.setCustomAnimations(
                    R.anim.home_fade_in, R.anim.home_fade_out)
            if (!to.isAdded) {    // 先判断是否被add过
                transaction?.hide(mContentFragment)?.add(R.id.fl_main_content, to)?.commitAllowingStateLoss() // 隐藏当前的fragment，add下一个到Activity中
            } else {
                transaction?.hide(mContentFragment)?.show(to)?.commitAllowingStateLoss() // 隐藏当前的fragment，显示下一个
            }
            mContentFragment = to
        }
    }

    private fun todo(position: Int) {
//        if (position == 4) {
//            RxBus.get()?.post(CLICK_THINGS_FRAGMENT, true)
//        } else if (position == 1) {
//            RxBus.get()?.post(MAIN_BOTTOM_NAVIGATION_DOUBLE_CLICK_LEFT_SEC, true)
//        }
    }

    private fun initRxObservable() {
//        closeMainObservable = RxBus.get()?.register(RxConstant.CLOSE_MAIN_OBSERVABLE, Boolean::class.java)
//        closeMainObservable.subscribe({ aBoolean ->
//            isOnlyClose = aBoolean!!
//            finish()
//        }, { error -> })
    }

    override fun onPause() {
        super.onPause()
//        SPreference.isCurrentRunningForeground()
    }

    override fun onDestroy() {
//        if (closeMainObservable != null)
//            RxBus.get()?.unregister(RxConstant.CLOSE_MAIN_OBSERVABLE, closeMainObservable)
        super.onDestroy()
        MainTabManager.getInstance().destory()
//
//        if (isOnlyClose) {
//            return
//        }
    }

    override fun exitExecute() {
        super.exitExecute()
//        getPresenter().backupDataToOSS()
    }

    override fun onBackPressed() {
        exitBy2Click()
    }
}