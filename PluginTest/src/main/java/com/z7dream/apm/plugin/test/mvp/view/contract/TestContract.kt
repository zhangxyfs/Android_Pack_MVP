package com.z7dream.apm.plugin.test.mvp.view.contract

import com.z7dream.apm.plugin.test.mvp.view.model.TestModel
import com.z7dream.apm.base.mvp.view.BaseContract

/**
 * Created by Z7Dream on 2017/10/26 11:14.
 * Email:zhangxyfs@126.com
 */
interface TestContract : BaseContract {

    interface Presenter : BaseContract.BasePresenter {
        fun getData(isRef: Boolean)
    }

    interface View : BaseContract.BaseView {
        fun getDataSucc(modelList: List<TestModel>, isRef: Boolean)

        fun getDataFail(str: String?)
    }
}