package com.z7dream.apm.mvp.presenter

import android.content.Context
import com.z7dream.apm.base.mvp.presenter.Presenter
import com.z7dream.apm.mvp.data.repository.LoginRepository
import com.z7dream.apm.mvp.data.service.LoginService
import com.z7dream.apm.mvp.view.contract.LoginContract

/**
 * Created by Z7Dream on 2017/10/31 9:43.
 * Email:zhangxyfs@126.com
 */
class LoginPresenter(context: Context, view: LoginContract.View) : Presenter<LoginContract.View, LoginService>(context, view), LoginContract.Presenter {
    override fun getCode(name: String, code: String) {

    }

    override fun login(name: String, pwd: String) {
        getService()
                ?.toLoginByPwd(name, pwd, null)
                ?.compose(getView()?.bindToLifecycle())
                ?.subscribe({ data ->

                }, {})
    }

    override fun loginCode(name: String, code: String) {

    }

    override fun loginEmail(email: String, pwd: String) {

    }

    override fun createRepository(): LoginService = LoginRepository()
}