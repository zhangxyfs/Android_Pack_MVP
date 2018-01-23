package com.z7dream.apm.plugin.test.api

import com.z7dream.apm.base.api.BaseApiClient
import com.z7dream.apm.base.utils.rx.RxResultHelper
import com.z7dream.apm.plugin.test.api.entity.UserEntity
import io.reactivex.Observable

/**
 * Created by Z7Dream on 2017/9/8 18:02.
 * Email:zhangxyfs@126.com
 */
class ApiClient {

    companion object {
        private val REQUEST = BaseApiClient.get(RequestManager::class.java).getREQUEST();

        fun getData(): Observable<List<UserEntity>> = REQUEST.getMainData().compose(RxResultHelper.handleResult())
    }
}