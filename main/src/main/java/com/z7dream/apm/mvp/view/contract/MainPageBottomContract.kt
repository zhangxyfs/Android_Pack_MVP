package com.z7dream.apm.mvp.view.contract

import com.z7dream.apm.base.mvp.view.BaseContract
import com.z7dream.apm.mvp.view.model.MainModel

/**
 * Created by Z7Dream on 2017/10/26 11:14.
 * Email:zhangxyfs@126.com
 */
interface MainPageBottomContract : BaseContract {

    interface Presenter : BaseContract.BasePresenter {
        fun getData(isRef: Boolean)


    }

    interface View : BaseContract.BaseView {
        fun getDataSucc(modelList: List<MainModel>, isRef: Boolean)

        fun getDataFail(str: String?)
    }
}