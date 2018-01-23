package com.z7dream.apm.mvp.data.service

import com.z7dream.apm.base.mvp.model.BaseService
import com.z7dream.apm.mvp.view.model.MainModel
import io.reactivex.Observable

/**
 * Created by Z7Dream on 2017/10/26 10:56.
 * Email:zhangxyfs@126.com
 */
interface MainPageBottomService : BaseService {

    fun getData(): Observable<List<MainModel>>
}