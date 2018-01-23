package com.z7dream.apm.mvp.presenter

import android.content.Context
import com.z7dream.apm.base.mvp.presenter.Presenter
import com.z7dream.apm.base.utils.cache.CacheManager
import com.z7dream.apm.base.utils.listener.Callback
import com.z7dream.apm.base.utils.tools.ZipUtils
import com.z7dream.apm.mvp.data.repository.WelcomeRepository
import com.z7dream.apm.mvp.data.service.WelcomeService
import com.z7dream.apm.mvp.view.contract.WelcomeContract

open class WelcomePresenter(context: Context, view: WelcomeContract.View) : Presenter<WelcomeContract.View, WelcomeService>(context, view), WelcomeContract.Presenter {
    private var page: Int = 0;

    override fun createRepository(): WelcomeService = WelcomeRepository()

    override fun init() {
    }

    override fun toUnZip() {
        getService()
                ?.to_unzip_smiley()
                ?.subscribe({ v ->
                    val `in` = getContext()?.getResources()?.openRawResource(v)
                    ZipUtils.unZip(`in`, CacheManager.getCachePath(getContext(), CacheManager.SMILEY), false)
                }, {})
        getService()
                ?.to_unzip_blank()
                ?.subscribe({ v ->
                    val `in` = getContext()?.getResources()?.openRawResource(v)
                    ZipUtils.unZip(`in`, CacheManager.getCachePath(getContext(), CacheManager.FILE), false)
                }, {})
    }

    override fun initWorkTable() {
        getService()
                ?.to_ref_token(Callback {
                    //            getUserComAndDep()
                })
    }

    override fun detachView() {
        super.detachView()
    }

}