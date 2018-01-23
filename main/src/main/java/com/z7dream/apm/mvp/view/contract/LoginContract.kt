package com.z7dream.apm.mvp.view.contract

import com.z7dream.apm.base.mvp.view.BaseContract
import com.z7dream.apm.mvp.view.model.MainModel

/**
 * Created by Z7Dream on 2017/10/30 18:19.
 * Email:zhangxyfs@126.com
 */
interface LoginContract : BaseContract {
    interface Presenter : BaseContract.BasePresenter {

        fun getCode(name: String, code: String)

        fun login(name: String, pwd: String)

        fun loginCode(name: String, code: String)

        fun loginEmail(email: String, pwd: String)
    }

    interface View : BaseContract.BaseView {
        fun getDataSucc(modelList: List<MainModel>, isRef: Boolean)

        fun getDataFail(str: String?)
    }
}