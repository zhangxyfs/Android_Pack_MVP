package com.z7dream.apm.base.mvp.ui

import android.app.Application
import android.os.Bundle
import com.z7dream.apm.base.mvp.view.BaseContract

/**
 * Created by Z7Dream on 2017/9/6 13:34.
 * Email:zhangxyfs@126.com
 */
abstract class BaseActivity<P : BaseContract.BasePresenter, APP : Application> : BaseCompatActivity<P, APP>() {

    override fun onStart() {
        super.onStart()
        toOnStart()
    }

    override fun onResume() {
        super.onResume()
        toOnResume()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        toOnCreate(savedInstanceState)
    }

    override fun onPause() {
        toOnPause()
        super.onPause()
    }

    override fun onStop() {
        toOnStop()
        super.onStop()
    }

    override fun onDestroy() {
        toOnDestroy()
        super.onDestroy()
    }
}