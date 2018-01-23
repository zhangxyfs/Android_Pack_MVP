package com.z7dream.apm.base.utils.net

/**
 * Created by Z7Dream on 2017/9/7 18:28.
 * Email:zhangxyfs@126.com
 */
object NetConfig {
    val isLocal = false
    internal val BASE_URL: String
    internal val SERVER_ADD: String

    init{
        if(isLocal){
            val BASE_HEAD = "http://xxx.xxx.xx.xxx"
            val BASE_END = ":18383/rest"

            BASE_URL = BASE_HEAD + BASE_END
        }else{
            val BASE_HEAD = "https://github.com"
            val BASE_END = ""

            BASE_URL = BASE_HEAD + BASE_END
        }

        SERVER_ADD = BASE_URL
    }
}