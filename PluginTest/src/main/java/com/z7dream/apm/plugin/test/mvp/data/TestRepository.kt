package com.z7dream.apm.plugin.test.mvp.data

import com.z7dream.apm.plugin.test.api.ApiClient
import com.z7dream.apm.plugin.test.mvp.view.model.TestModel
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

/**
 * Created by Z7Dream on 2017/10/26 10:45.
 * Email:zhangxyfs@126.com
 */
class TestRepository : TestService {

    override fun getData(): Observable<List<TestModel>> =
            ApiClient.getData()
                    .subscribeOn(Schedulers.io())
                    .map { list ->
                        val modelList: MutableList<TestModel> = ArrayList()
                        for (entity in list) {
                            modelList.add(TestModel(entity.title, entity.url))
                        }
                        modelList
                    }


    companion object {
        val SIZE: Int = 20;
    }
}