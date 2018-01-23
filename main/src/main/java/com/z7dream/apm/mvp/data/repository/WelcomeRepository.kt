package com.z7dream.apm.mvp.data.repository

import com.google.gson.Gson
import com.z7dream.apm.R
import com.z7dream.apm.api.ApiClient
import com.z7dream.apm.base.Appli
import com.z7dream.apm.base.utils.cache.SPreference
import com.z7dream.apm.base.utils.constant.JumpAct
import com.z7dream.apm.base.utils.listener.Callback
import com.z7dream.apm.base.utils.rx.RxSchedulersHelper
import com.z7dream.apm.mvp.data.service.WelcomeService
import com.z7dream.apm.mvp.view.model.WelcomeModel
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

class WelcomeRepository : WelcomeService {
    override fun to_get_user_cd() {
//        ApiClient.getUserCompanyDep(SPreference.getUserId().toString())
//                .subscribe({ dataBean ->
//                    val comDepList = CompanyDepartInfoForUser.convertByCompanyAndDepEntity(dataBean)
//                    addressDataManager.updateDbByUserComAndDep(comDepList)
//                    delExitCompany()
//                }, object : RxSubscribeError() {
//                    fun error(t: Throwable) {
//                        //获取失败
//                    }
//                }
//                )
    }

    override fun to_ref_token(callback: Callback<Boolean>) {
        if (!SPreference.isRealLogin()) {
            JumpAct.toLogin(Appli.context)
            callback.event(false)
        } else {
            val userToken = SPreference.getUserToken()
            if (userToken != null) {
                ApiClient.toRefToken(userToken.refreshToken)
                        .subscribe({ token ->
                            val bean = SPreference.getLoginUserInfoData()
                            if (bean != null) {
                                bean.userToken = token
                                SPreference.saveUserInfoData(Gson().toJson(bean))
                            }
                            callback.event(true)
                        }, {})
            }
        }
    }

    override fun to_unzip_smiley(): Observable<Int> = Observable.just(R.raw.smiley).compose(RxSchedulersHelper.io())

    override fun to_unzip_blank(): Observable<Int> = Observable.just(R.raw.blank).compose(RxSchedulersHelper.io())

    override fun getData(): Observable<List<WelcomeModel>> =
            ApiClient.getData()
                    .subscribeOn(Schedulers.io())
                    .map { list ->
                        val modelList: MutableList<WelcomeModel> = ArrayList()
                        for (entity in list) {
                            modelList.add(WelcomeModel())
                        }
                        modelList
                    }


    companion object {
        val SIZE: Int = 20;
    }
}