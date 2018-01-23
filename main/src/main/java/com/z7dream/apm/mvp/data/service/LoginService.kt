package com.z7dream.apm.mvp.data.service

import com.z7dream.apm.api.entity.UserEntity
import com.z7dream.apm.base.api.entity.LoginUserInfoEntity
import com.z7dream.apm.base.mvp.model.BaseService
import io.reactivex.Observable

/**
 * Created by Z7Dream on 2017/10/31 9:44.
 * Email:zhangxyfs@126.com
 */
interface LoginService : BaseService {

    /**
     * 用户名密码登陆
     */
    fun toLoginByPwd(loginName: String, pwdMD5: String, data: Map<String, String>?): Observable<LoginUserInfoEntity.ResultBean>


    fun getData(): Observable<List<UserEntity>>
}