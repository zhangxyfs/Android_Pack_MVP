package com.z7dream.apm.mvp.view.contract

import com.z7dream.apm.base.mvp.view.BaseContract

interface WelcomeContract : BaseContract {
    interface Presenter : BaseContract.BasePresenter {
        fun init()
        fun toUnZip()
        fun initWorkTable()
    }

    interface View : BaseContract.BaseView {
    }
}