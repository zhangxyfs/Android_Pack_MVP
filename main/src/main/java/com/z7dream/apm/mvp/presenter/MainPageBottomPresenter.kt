package com.z7dream.apm.mvp.presenter

import android.content.Context
import com.z7dream.apm.base.mvp.presenter.Presenter
import com.z7dream.apm.mvp.data.repository.MainPageBottomRepository
import com.z7dream.apm.mvp.data.service.MainPageBottomService
import com.z7dream.apm.mvp.view.contract.MainPageBottomContract

/**
 * Created by Z7Dream on 2017/10/26 11:15.
 * Email:zhangxyfs@126.com
 */
open class MainPageBottomPresenter(context: Context, view: MainPageBottomContract.View) :
        Presenter<MainPageBottomContract.View, MainPageBottomService>(context, view), MainPageBottomContract.Presenter {
    private var page: Int = 0;

    override fun createRepository(): MainPageBottomService = MainPageBottomRepository()

    override fun getData(isRef: Boolean) {
        page = if (isRef) 0 else page++
        getService()
                ?.getData()
                ?.compose(getView()?.bindToLifecycle())
                ?.subscribe({ list ->
                    getView()?.getDataSucc(list, isRef);
                }, {
                    getView()?.getDataFail("fail")
                })
    }

    override fun detachView() {
        super.detachView()
    }

}