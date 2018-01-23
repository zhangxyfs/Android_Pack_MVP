package com.z7dream.apm.plugin.test.api;

import com.z7dream.apm.base.mvp.model.BaseEntity
import com.z7dream.apm.plugin.test.api.entity.UserEntity
import io.reactivex.Observable
import retrofit2.http.GET

/**
 * Created by Z7Dream on 2017/9/8 17:25.
 * Email:zhangxyfs@126.com
 */
internal interface RequestManager {
    @GET("zhangxyfs")
    fun getMainData(): Observable<BaseEntity<List<UserEntity>>>
}