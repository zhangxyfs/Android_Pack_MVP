package com.z7dream.apm.mvp.data.repository

import com.z7dream.apm.api.ApiClient
import com.z7dream.apm.mvp.data.service.MainPageBottomService
import com.z7dream.apm.mvp.view.model.MainModel
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

/**
 * Created by Z7Dream on 2017/10/26 10:45.
 * Email:zhangxyfs@126.com
 */
class MainPageBottomRepository : MainPageBottomService {

    override fun getData(): Observable<List<MainModel>> =
            ApiClient.getData()
                    .subscribeOn(Schedulers.io())
                    .map { list ->
                        val modelList: MutableList<MainModel> = ArrayList()
                        for (entity in list) {
                            modelList.add(MainModel(entity.title, entity.url))
                        }
                        modelList
                    }


    companion object {
        val SIZE: Int = 20;
    }
}