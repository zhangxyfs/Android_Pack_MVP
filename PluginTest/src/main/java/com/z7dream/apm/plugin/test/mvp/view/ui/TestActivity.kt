package com.z7dream.apm.plugin.test.mvp.view.ui

import android.os.Bundle
import android.widget.Button
import com.z7dream.apm.base.utils.rx.RxBus
import com.z7dream.apm.plugin.test.R
import com.z7dream.apm.plugin.test.mvp.base.PluginBaseActivity
import com.z7dream.apm.plugin.test.mvp.presenter.TestPresenter
import com.z7dream.apm.plugin.test.mvp.view.contract.TestContract
import com.z7dream.apm.plugin.test.mvp.view.model.TestModel

class TestActivity : PluginBaseActivity<TestContract.Presenter>(), TestContract.View {

    override fun layoutID(): Int = R.layout.activity_plugin_test
    override fun createPreseneter(): TestContract.Presenter = TestPresenter(this, this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun init(savedInstanceState: Bundle?) {
        val mButton: Button = findViewById(R.id.button)
        val mButton1: Button = findViewById(R.id.button1)

        getHandler()?.postDelayed({
            RxBus.get()?.post("RxBus", 1);
        }, 5000)
    }

    override fun getDataSucc(modelList: List<TestModel>, isRef: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getDataFail(str: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
