package com.z7dream.apm.api

import com.z7dream.apm.base.api.BaseNetConfig

/**
 * Created by Z7Dream on 2017/11/3 10:59.
 * Email:zhangxyfs@126.com
 */

open class NetConfig : BaseNetConfig() {

     companion object USER {
        private const val user = "usercenter/u"
        //用RefreshToken刷新Token
        const val REF_TOKEN = user + "/token/referesh"

        //登录
        const val LOGIN = user + "/login"
    }
}
