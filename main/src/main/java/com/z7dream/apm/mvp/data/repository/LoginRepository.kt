package com.z7dream.apm.mvp.data.repository

import com.z7dream.apm.api.ApiClient
import com.z7dream.apm.api.entity.UserEntity
import com.z7dream.apm.base.api.entity.LoginUserInfoEntity
import com.z7dream.apm.base.utils.tools.MD5Utils
import com.z7dream.apm.mvp.data.service.LoginService
import io.reactivex.Observable

/**
 * Created by Z7Dream on 2017/10/31 9:44.
 * Email:zhangxyfs@126.com
 */
class LoginRepository : LoginService {
    override fun getData(): Observable<List<UserEntity>> =
            ApiClient.getData()

    override fun toLoginByPwd(loginName: String, password: String, data: Map<String, String>?): Observable<LoginUserInfoEntity.ResultBean> =
            ApiClient.toLoginByPwd(loginName, MD5Utils.getMD5(password), data).flatMap { data ->
                Observable.just(data)
            }
}