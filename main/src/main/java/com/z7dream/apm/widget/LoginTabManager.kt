package com.z7dream.apm.widget

import android.os.Bundle
import android.support.v4.app.Fragment

import com.z7dream.apm.R
import com.z7dream.apm.mvp.view.ui.LoginFragment

/**
 * Created by Z7Dream on 2017/11/1 14:17.
 * Email:zhangxyfs@126.com
 */

open class LoginTabManager {
    private var loginMobileFragment: LoginFragment? = null
    private var loginCodeFragment: LoginFragment? = null
    private var loginMailFragment: LoginFragment? = null

    fun getFragmentByIndex(index: Int): Fragment? {
        var fragment: Fragment? = null
        val mBundle = Bundle()
        mBundle.putInt(LoginFragment.WHICH_PAGE, index)
        when (index) {
            R.id.nav_login_phone -> {
                if (loginMobileFragment == null) {
                    loginMobileFragment = LoginFragment()
                }
                fragment = loginMobileFragment
            }
            R.id.nav_login_code -> {
                if (loginCodeFragment == null) {
                    loginCodeFragment = LoginFragment()
                }
                fragment = loginCodeFragment
            }
            R.id.nav_login_mail -> {
                if (loginMailFragment == null) {
                    loginMailFragment = LoginFragment()
                }
                fragment = loginMailFragment
            }
        }
        if (fragment != null)
            fragment.arguments = mBundle
        return fragment
    }

    fun destory() {
        loginMobileFragment = null
        loginCodeFragment = null
        loginMailFragment = null
    }

    companion object {
        private  var mInstance: LoginTabManager? = null

        val instance: LoginTabManager
            get() {
                if(mInstance == null){
                    synchronized(LoginTabManager::class.java) {
                        if (mInstance == null) {
                            mInstance = LoginTabManager()
                        }
                    }
                }
                return mInstance!!
            }
    }
}
