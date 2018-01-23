package com.z7dream.apm.api.entity.request

/**
 * Created by Z7Dream on 2017/3/1 14:22.
 * Email:zhangxyfs@126.com
 */

class LoginRegBody constructor(
        var mobile: String,
        var password: String,
        var unionid: String?,
        var scope: String?,
        var expires_in: String?,
        var access_token: String?,
        var refresh_token: String?,
        var openid: String?
) {

    constructor(mobile: String, password: String) : this(mobile, password, null, null, null, null, null, null) {
        this.mobile = mobile;
        this.password = password;
    }
}
