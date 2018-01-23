package com.z7dream.apm.base.api


/**
 * Created by Z7Dream on 2017/11/3 10:11.
 * Email:zhangxyfs@126.com
 */
open class BaseNetConfig {
    enum class Env {
        //测试环境
        test,
        //开发环境
        develop,
        //暂存环境
        stag
    }

    object DefaultParams {
        val accessToken = "accessToken"
        val appVersion = "appVersion"
        val deviceId = "deviceId"
        val os = "os"
        val osVersion = "osVersion"
        val contentType = "content-Type"

        val authorization = "Authorization"
    }

    companion object {
        val isLocal = false
        val LOCAL_ENV = Env.develop//当isLocal == true的时候 LOCAL_ENV 才有效果
        val isNeedSignOSS = true

        var SERVER_ADD: String

        val SERVER_WORKTABLE_URL: String

        val SERVER_WORKTABLE_HTML_URL: String

        val SERVER_STATISTICS_URL: String

        internal val BASE_URL: String
        internal val BASE_H5_URL: String

        init {
            if (isLocal) {
                var BASE_HEAD = ""
                var BASE_END = ""
                var BASE_H5_END = ""

                if (LOCAL_ENV == Env.develop) {//开发环境
                    BASE_HEAD = "http://192.168.60.189"
                    BASE_END = ":18383/rest"

                    BASE_URL = BASE_HEAD + BASE_END
                    BASE_H5_URL = BASE_HEAD + BASE_H5_END

                    SERVER_WORKTABLE_URL = BASE_URL

                    SERVER_WORKTABLE_HTML_URL = BASE_HEAD + ":25280"

                    SERVER_STATISTICS_URL = BASE_HEAD + ":22290"

                } else if (LOCAL_ENV == Env.stag) {//暂存环境
                    BASE_HEAD = "https://st-webgateway.123eblog.com"
                    BASE_END = "/rest"

                    BASE_URL = BASE_HEAD + BASE_END
                    BASE_H5_URL = BASE_HEAD + BASE_H5_END

                    SERVER_WORKTABLE_URL = BASE_URL

                    SERVER_WORKTABLE_HTML_URL = "https://st-worktable.123eblog.com"

                    SERVER_STATISTICS_URL = "https://st-h5.123eblog.com"

                } else {//永远是测试环境
                    BASE_HEAD = "https://te-webgateway.123eblog.com"
                    BASE_END = "/rest"
                    BASE_H5_END = ""

                    BASE_URL = BASE_HEAD + BASE_END
                    BASE_H5_URL = BASE_HEAD + BASE_H5_END

                    SERVER_WORKTABLE_URL = BASE_URL

                    SERVER_WORKTABLE_HTML_URL = "https://te-worktable.123eblog.com"

                    SERVER_STATISTICS_URL = "https://te-h5.123eblog.com"
                }
            } else {
                val BASE_HEAD = "https://webgateway.123eblog.com/rest"
                val BASE_H5_HEAD = "https://www"
                val BASE_END = ""

                BASE_URL = BASE_HEAD + BASE_END
                BASE_H5_URL = BASE_H5_HEAD + BASE_END

                SERVER_WORKTABLE_URL = "https://webgateway.123eblog.com/rest"

                SERVER_WORKTABLE_HTML_URL = "https://worktable.123eblog.com"

                SERVER_STATISTICS_URL = "https://h5.123eblog.com"
            }
            SERVER_ADD = BASE_URL
        }
    }
}