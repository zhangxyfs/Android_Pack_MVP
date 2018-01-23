package com.z7dream.apm.base

import android.content.Context
import android.support.multidex.MultiDexApplication
import com.z7dream.apm.base.utils.dao.bean.MyObjectBox
import io.objectbox.BoxStore

/**
 * Created by Z7Dream on 2017/10/30 10:37.
 * Email:zhangxyfs@126.com
 */
open class Appli : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        boxStore = MyObjectBox.builder().androidContext(context).build()
    }

    companion object {
        lateinit var context: Context
        lateinit var boxStore : BoxStore
    }
}