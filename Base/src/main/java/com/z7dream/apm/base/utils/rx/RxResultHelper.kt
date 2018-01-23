package com.z7dream.apm.base.utils.rx


import android.text.TextUtils
import com.z7dream.apm.base.mvp.model.BaseEntity
import com.z7dream.apm.base.utils.exception.ApiException
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer
import io.reactivex.functions.Function
import okhttp3.ResponseBody
import java.io.IOException

/**
 * User: Axl_Jacobs(Axl.Jacobs@gmail.com)
 * Date: 2016-09-01
 * Time: 20:27
 * FIXME
 * Rx处理服务器返回
 */
object RxResultHelper {


    fun filterResultToString(): ObservableTransformer<ResponseBody, String> {
        return ObservableTransformer { upstream ->
            upstream.flatMap { responseBody ->
                var string = ""
                try {
                    string = responseBody.string()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

                createData(string)
            }
        }
    }


    fun <T> handleResult(): ObservableTransformer<BaseEntity<T>, T> {
        return ObservableTransformer { upstream ->
            upstream.flatMap(Function<BaseEntity<T>, ObservableSource<T>> { entity ->
                var code: String? = "5000"
                var msg: String? = "出错了，请稍后重试"

                if (entity != null && entity.isOK()) {
                    //防止某些接口返回data为null
                    if (entity.data == null) {
                        entity.data = java.lang.Boolean.valueOf(true) as T
                    }

                    return@Function createData(entity.data)
                } else {
                    if (entity != null) {
                        code = entity.code
                        //添加错误消息判定
                        if (!TextUtils.isEmpty(entity.message)) {
                            msg = entity.message
                        } else {
                            if (!TextUtils.isEmpty(entity.result))
                                msg = entity.result
                        }


                    }
                }
                Observable.error(ApiException(code!!, msg!!))
            })
        }
    }

    private fun <T> createData(t: T): Observable<T> {
        return Observable.create { emitter ->
            try {
                emitter.onNext(t)
                emitter.onComplete()
            } catch (e1: Exception) {
                e1.printStackTrace()
            }
        }
    }
}