package com.z7dream.apm.api

import com.z7dream.apm.api.entity.UserEntity
import com.z7dream.apm.api.entity.request.LoginRegBody
import com.z7dream.apm.base.api.BaseApiClient
import com.z7dream.apm.base.api.entity.LoginUserInfoEntity
import com.z7dream.apm.base.api.entity.UserToken
import com.z7dream.apm.base.utils.rx.RxResultHelper
import com.z7dream.apm.base.utils.rx.RxSchedulersHelper
import io.reactivex.Observable

/**
 * Created by Z7Dream on 2017/9/8 18:02.
 * Email:zhangxyfs@126.com
 */
class ApiClient {

    companion object {
        private val REQUEST = BaseApiClient.get(RequestManager::class.java).getREQUEST();

        fun getData(): Observable<List<UserEntity>> =
                REQUEST.getMainData().compose(RxSchedulersHelper.io_main()).compose(RxResultHelper.handleResult())

        fun toRefToken(refToken: String): Observable<UserToken> =
                REQUEST.toRefToken(refToken).compose(RxSchedulersHelper.io_main()).compose(RxResultHelper.handleResult())

        fun toLoginByPwd(userName: String, pwdMd5: String, data: Map<String, String>?): Observable<LoginUserInfoEntity.ResultBean> {
            return if (null == data) {
                REQUEST.toLoginByPwd(LoginRegBody(userName, pwdMd5)).compose(RxSchedulersHelper.io_main()).compose(RxResultHelper.handleResult())
            } else
                REQUEST.toLoginByPwd(LoginRegBody(userName, pwdMd5, data["unionid"],
                        data["scope"],
                        data["expires_in"],
                        data["access_token"],
                        data["refresh_token"],
                        data["openid"])).compose(RxSchedulersHelper.io_main()).compose(RxResultHelper.handleResult())
        }
    }
}