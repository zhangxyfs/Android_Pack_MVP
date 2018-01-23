package com.z7dream.apm.mvp.view.ui

import android.os.Build
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.view.WindowManager
import com.z7dream.apm.R
import com.z7dream.apm.mvp.base.PluginBaseActivity
import com.z7dream.apm.mvp.presenter.LoginPresenter
import com.z7dream.apm.mvp.view.contract.LoginContract
import com.z7dream.apm.mvp.view.model.MainModel
import com.z7dream.apm.widget.LoginTabManager
import kotlinx.android.synthetic.main.activity_login.*

/**
 * Created by Z7Dream on 2017/10/30 18:18.
 * Email:zhangxyfs@126.com
 */
class LoginActivity : PluginBaseActivity<LoginContract.Presenter>(), LoginContract.View {
    private lateinit var mFragmentManager: FragmentManager
    private lateinit var mContentFragment: Fragment

    override fun layoutID(): Int = R.layout.activity_login;
    override fun createPreseneter(): LoginContract.Presenter = LoginPresenter(this, this)

    override fun after() {
        super.after()
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
        }
    }

    override fun init(savedInstanceState: Bundle?) {
        userToolbarNoBack(findViewById(R.id.toolbar), R.string.login_toolbar_title)
        toolbar_tabs.setTabMode(TabLayout.MODE_FIXED)
        toolbar_tabs.addTab(toolbar_tabs.newTab().setText(R.string.login_phone_str))
        toolbar_tabs.addTab(toolbar_tabs.newTab().setText(R.string.login_code_str))
        toolbar_tabs.addTab(toolbar_tabs.newTab().setText(R.string.login_mail_str))

        mFragmentManager = supportFragmentManager
        val transaction = mFragmentManager.beginTransaction()
        mContentFragment = LoginTabManager.instance.getFragmentByIndex(R.id.nav_login_phone)!!

        transaction.add(R.id.fl_login, mContentFragment)
        transaction.commitAllowingStateLoss()

        toolbar_tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                var switchId = -1
                when (tab.position) {
                    0 -> {
                        switchId = R.id.nav_login_phone;
                    }
                    1 -> {
                        switchId = R.id.nav_login_code
                    }
                    2 -> {
                        switchId = R.id.nav_login_mail
                    }
                }
                if (switchId > -1) {
                    switchFragment(LoginTabManager.instance.getFragmentByIndex(switchId)!!)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })
    }

    private fun switchFragment(to: Fragment) {
        if (mContentFragment !== to) {
            val transaction = mFragmentManager.beginTransaction().setCustomAnimations(R.anim.home_fade_in, R.anim.home_fade_out)
            if (!to.isAdded) {    // 先判断是否被add过
                transaction.hide(mContentFragment).add(R.id.fl_login, to).commitAllowingStateLoss() // 隐藏当前的fragment，add下一个到Activity中
            } else {
                transaction.hide(mContentFragment).show(to).commitAllowingStateLoss() // 隐藏当前的fragment，显示下一个
            }
            mContentFragment = to
        }
    }

    override fun getDataSucc(modelList: List<MainModel>, isRef: Boolean) {
    }

    override fun getDataFail(str: String?) {
    }

    override fun onDestroy() {
        super.onDestroy()
        LoginTabManager.instance.destory()
    }
}
