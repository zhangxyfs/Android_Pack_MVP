package com.z7dream.apm.plugin.test.mvp.data

import com.z7dream.apm.plugin.test.mvp.view.model.TestModel
import com.z7dream.apm.base.mvp.model.BaseService
import io.reactivex.Observable

/**
 * Created by Z7Dream on 2017/10/26 10:56.
 * Email:zhangxyfs@126.com
 */
interface TestService : BaseService {

    fun getData(): Observable<List<TestModel>>
}