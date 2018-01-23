package com.z7dream.apm.plugin.test.mvp.base

import com.z7dream.apm.base.Appli
import com.z7dream.apm.base.mvp.ui.BaseFragment
import com.z7dream.apm.base.mvp.view.BaseContract

/**
 * Created by Z7Dream on 2017/10/26 11:59.
 * Email:zhangxyfs@126.com
 */
open abstract class PluginBaseFragment<P : BaseContract.BasePresenter> : BaseFragment<P, Appli>() {
}