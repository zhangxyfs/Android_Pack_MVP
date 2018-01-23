package com.z7dream.apm.mvp.view.ui;

import android.os.Bundle
import com.tbruyelle.rxpermissions2.RxPermissions
import com.z7dream.apm.R
import com.z7dream.apm.base.utils.WeakHandler
import com.z7dream.apm.base.utils.cache.SPreference
import com.z7dream.apm.base.utils.constant.Constant.*
import com.z7dream.apm.base.utils.dao.BaseInfoUtils
import com.z7dream.apm.base.utils.tools.Utils
import com.z7dream.apm.mvp.base.PluginBaseActivity
import com.z7dream.apm.mvp.view.contract.WelcomeContract

class WelcomeActivity : PluginBaseActivity<WelcomeContract.Presenter>(), WelcomeContract.View {
    private lateinit var weakHandler: WeakHandler
    private lateinit var mRxPermissions: RxPermissions

    override fun layoutID(): Int = R.layout.activity_welcome
    override fun createPreseneter(): WelcomeContract.Presenter?{
        return null;
    }

    override fun before() {
        super.before()
        setIsNeedGoneNavigationBar(true)//不显示导航条
        weakHandler = WeakHandler()
        if (!BaseInfoUtils.isFirstOpenApp()) {
            //TODO 不是第一次打开做一些事
        } else {
        }
        mRxPermissions = RxPermissions(this)
        mRxPermissions.request(PERMISSION_READ_STORAGE, PERMISSION_LOCATION, PERMISSION_READ_PHONE_STATE, PERMSSION_READ_SMS, PERMISSION_GET_CONTANCTS)
                .subscribe({ b ->
                    if (b) {
                        beforeInit()
                    } else {
                        finish()
                    }
                });

    }

    override fun init(savedInstanceState: Bundle?) {
    }

    private fun beforeInit() {
//        (applicationContext as Appli).getContactContactsUtils().requestContactDataAndUpdateDb()

        if (getPresenter() == null) {
            setPresenter()
        }

        val nowAppVersion = Utils.getVersionCode(this)
        val lastAppVersion = BaseInfoUtils.getLastAppVersion()
        if (lastAppVersion < nowAppVersion) {//代表已经升级
            BaseInfoUtils.saveLastAppVersion()
        }

        getPresenter()?.init()
        getPresenter()?.toUnZip()
        getPresenter()?.initWorkTable()


        weakHandler.postDelayed({
            finish()
            if (!SPreference.isLogin() || SPreference.getLoginUserInfoData() == null) {
                openActivity(LoginActivity::class.java)
            } else {
                openActivity(MainPageBottomActivity::class.java)
            }

        }, 2000)
    }
}