package com.z7dream.apm.plugin.test.mvp.presenter

import android.content.Context
import com.z7dream.apm.base.mvp.presenter.Presenter
import com.z7dream.apm.plugin.test.mvp.data.TestRepository
import com.z7dream.apm.plugin.test.mvp.data.TestService
import com.z7dream.apm.plugin.test.mvp.view.contract.TestContract

/**
 * Created by Z7Dream on 2017/10/26 11:15.
 * Email:zhangxyfs@126.com
 */
open class TestPresenter(context: Context, view: TestContract.View) : Presenter<TestContract.View, TestService>(context, view), TestContract.Presenter {
    private var page: Int = 0;

    override fun createRepository(): TestService = TestRepository()

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