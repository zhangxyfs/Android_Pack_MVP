package com.z7dream.apm.mvp.base

import com.z7dream.apm.base.Appli
import com.z7dream.apm.base.mvp.ui.BaseActivity
import com.z7dream.apm.base.mvp.view.BaseContract

/**
 * Created by Z7Dream on 2017/10/26 11:33.
 * Email:zhangxyfs@126.com
 */

abstract class PluginBaseActivity<P : BaseContract.BasePresenter> : BaseActivity<P, Appli>() {

}
