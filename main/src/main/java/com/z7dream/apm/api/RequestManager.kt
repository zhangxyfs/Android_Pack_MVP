package com.z7dream.apm.api;

import com.z7dream.apm.api.entity.UserEntity
import com.z7dream.apm.api.entity.request.LoginRegBody
import com.z7dream.apm.base.api.entity.LoginUserInfoEntity
import com.z7dream.apm.base.api.entity.UserToken
import com.z7dream.apm.base.mvp.model.BaseEntity
import io.reactivex.Observable
import retrofit2.http.*

/**
 * Created by Z7Dream on 2017/9/8 17:25.
 * Email:zhangxyfs@126.com
 */
interface RequestManager {
    @GET("zhangxyfs")
    fun getMainData(): Observable<BaseEntity<List<UserEntity>>>

    /**
     * 刷新token
     *
     * @param refreshToken
     * @return
     */
    @POST(NetConfig.USER.REF_TOKEN)
    fun toRefToken(@Query("refreshToken") refreshToken: String): Observable<BaseEntity<UserToken>>

    /**
     * 登录
     */
    @POST(NetConfig.USER.LOGIN)
    fun toLoginByPwd(@Body body: LoginRegBody): Observable<BaseEntity<LoginUserInfoEntity.ResultBean>>

    @POST(NetConfig.USER.LOGIN)
    fun toLoginByPwd(@Body body: LoginRegBody, @QueryMap data: Map<String, String>): Observable<BaseEntity<LoginUserInfoEntity.ResultBean>>


}