package com.z7dream.apm.base.utils.rx

import android.util.Log
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import java.util.*
import java.util.concurrent.ConcurrentHashMap

/**
 * 无法跨进程
 *
 * 用法：register("test", String::class.java)
 *
 * Created by xiaoyu.zhang on 2015/8/13.
 */
class RxBus private constructor() {

    private val subjectMapper = ConcurrentHashMap<Any, MutableList<Subject<*>>>()

    fun clear() {
        for (obj in subjectMapper.keys) {
            val subjects = subjectMapper[obj]
            subjects?.clear()
        }
        subjectMapper.clear()
    }

    fun isHasObservable(tag: Any): Boolean {
        return if (subjectMapper.size > 0) {
            subjectMapper[tag] != null
        } else false
    }

    fun <T> register(tag: Any, clazz: Class<T>): Observable<T> {
        var subjectList: MutableList<Subject<*>>? = subjectMapper.get(tag)
        if (null == subjectList) {
            subjectList = Collections.synchronizedList(ArrayList())
            subjectMapper.put(tag, subjectList!!)
        }

        val subject: Subject<T>
        subject = PublishSubject.create<T>()
        subjectList.add(subject)
        if (DEBUG) Log.d(TAG, "[register]subjectMapper: " + subjectMapper)
        return subject
    }

    fun <T> unregister(tag: Any, observable: Observable<T>?) {
        val subjectList: MutableList<Subject<*>>? = subjectMapper[tag]
        if (null != subjectList) {
            (subjectList as MutableList<*>).remove(observable)
            //            if (isEmpty(subjects)) {
            //                subjectMapper.remove(tag);
            //            }
            if (observable != null && !observable.subscribe()!!.isDisposed) {
                observable.subscribe()!!.dispose()
            }
        }

        if (DEBUG) Log.d(TAG, "[unregister]subjectMapper: " + subjectMapper)
    }

    fun destory() {
        for (key in subjectMapper.keys) {
            val subjectList = subjectMapper[key]
            if (null != subjectList) {
                for (i in subjectList.indices) {
                    val observable = subjectList[i]
                    if (null != observable) {
                        if (!observable.subscribe().isDisposed) {
                            observable.subscribe().dispose()
                        }
                    }
                }
                subjectList.clear()
            }
        }
        subjectMapper.clear()
    }

    fun post(content: Any) {
        post(content.javaClass.name, content)
    }

    fun <T> post(tag: Any, content: T) {
        val subjectList: MutableList<Subject<*>>? = subjectMapper.get(tag)
        if (subjectList == null) return

        for (subject in subjectList) {
            (subject as Subject<T>).onNext(content)
        }

        if (DEBUG) Log.d(TAG, "[send]subjectMapper: " + subjectMapper)
    }

    companion object {
        private val TAG = RxBus::class.java.simpleName
        @Volatile private var instance: RxBus? = null
        var DEBUG = false


        @Synchronized
        fun get(): RxBus? {
            if (null == instance) {
                synchronized(RxBus::class.java) {
                    if (null == instance) {
                        instance = RxBus()
                    }
                }
            }
            return instance
        }


        fun isEmpty(collection: Collection<*>?): Boolean {
            return null == collection || collection.isEmpty()
        }

        fun isEmpty(map: Map<*, *>?): Boolean {
            return null == map || map.isEmpty()
        }

        fun isEmpty(objs: Array<Any>?): Boolean {
            return null == objs || objs.size <= 0
        }

        fun isEmpty(objs: IntArray?): Boolean {
            return null == objs || objs.size <= 0
        }

        fun isEmpty(charSequence: CharSequence?): Boolean {
            return null == charSequence || charSequence.length <= 0
        }
    }
}
