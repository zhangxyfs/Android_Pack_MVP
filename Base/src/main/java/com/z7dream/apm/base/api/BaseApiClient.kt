package com.z7dream.apm.base.api

import android.os.Build
import android.support.annotation.CallSuper
import android.text.TextUtils
import com.z7dream.apm.base.Appli
import com.z7dream.apm.base.utils.cache.SPreference
import com.z7dream.apm.base.utils.net.OKHTTP
import com.z7dream.apm.base.utils.tools.Utils

/**
 * Created by Z7Dream on 2017/10/30 13:26.
 * Email:zhangxyfs@126.com
 */
open class BaseApiClient<T>(cls: Class<T>) {
    private var REQUEST: T;

    @CallSuper
    private fun createHeadMap(): HashMap<String, String> {
        val map: HashMap<String, String> = HashMap()
        var token = ""
        val userTokenInfo = SPreference.getUserToken()
        if (userTokenInfo != null) {
            token = userTokenInfo.accessToken
        }
        token = if (TextUtils.isEmpty(token)) "" else token

        map.put(BaseNetConfig.DefaultParams.accessToken, token)
        map.put(BaseNetConfig.DefaultParams.appVersion, Utils.getVersionCode(Appli.context).toString())
        map.put(BaseNetConfig.DefaultParams.deviceId, Utils.getIMEI(Appli.context))
        map.put(BaseNetConfig.DefaultParams.os, "android")
        map.put(BaseNetConfig.DefaultParams.osVersion, Build.MODEL)
        map.put(BaseNetConfig.DefaultParams.contentType, "application/json")

        Utils.log("\naccessToken:" + token + "\n" +
                "deviceId:" + Utils.getIMEI(Appli.context) + "\n" +
                "version:" + Utils.getVersionCode(Appli.context) + "\n" +
                "osVersion" + Build.MODEL, "d")
        return map;
    }

    fun getREQUEST(): T = REQUEST;

    companion object {
        fun <T> get(clazz: Class<T>) = BaseApiClient(clazz)
    }

    init {
        REQUEST = OKHTTP.get().setDebug(true).init(createHeadMap(), BaseNetConfig.SERVER_ADD + "/").getRetrofit().create(cls)
    }
}