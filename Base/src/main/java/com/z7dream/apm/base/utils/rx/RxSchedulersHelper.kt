package com.z7dream.apm.base.utils.rx


import io.reactivex.FlowableTransformer
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * User: Axl_Jacobs(Axl.Jacobs@gmail.com)
 * Date: 2016-09-03
 * Time: 11:16
 * FIXME
 * 处理Rx线程
 */
object RxSchedulersHelper {

    //处理Rx线程
    fun <T> io_main(): ObservableTransformer<T, T> {
        return ObservableTransformer { upstream ->
            upstream.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
        }
    }

    fun <T> main(): ObservableTransformer<T, T> {
        return ObservableTransformer { upstream ->
            upstream.subscribeOn(AndroidSchedulers.mainThread())
                    .observeOn(AndroidSchedulers.mainThread())
        }
    }


    fun <T> io(): ObservableTransformer<T, T> {
        return ObservableTransformer { upstream ->
            upstream.subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io())
        }
    }

    fun <T> fio_main(): FlowableTransformer<T, T> {
        return FlowableTransformer { upstream ->
            upstream.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
        }
    }

    fun <T> fmain(): FlowableTransformer<T, T> {
        return FlowableTransformer { upstream ->
            upstream.subscribeOn(AndroidSchedulers.mainThread())
                    .observeOn(AndroidSchedulers.mainThread())
        }
    }

    fun <T> fio(): FlowableTransformer<T, T> {
        return FlowableTransformer { upstream ->
            upstream.subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io())
        }
    }
}
