package com.z7dream.apm.mvp.data.service

import com.z7dream.apm.base.mvp.model.BaseService
import com.z7dream.apm.base.utils.listener.Callback
import com.z7dream.apm.mvp.view.model.WelcomeModel
import io.reactivex.Observable

interface WelcomeService : BaseService {

    fun getData(): Observable<List<WelcomeModel>>

    fun to_unzip_smiley(): Observable<Int>

    fun to_unzip_blank(): Observable<Int>

    fun to_ref_token(callback: Callback<Boolean>)

    fun to_get_user_cd()
}