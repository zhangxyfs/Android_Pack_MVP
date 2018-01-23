package com.z7dream.apm.base.utils.exception

import java.io.IOException

/**
 * User: Axl_Jacobs(Axl.Jacobs@gmail.com)
 * Date: 2016-05-06
 * Time: 15:37
 * FIXME
 */
class ApiException(resultCode: String, internal var resultMessage: String) : IOException() {
    var code: String
        internal set

    init {
        code = resultCode
    }
}
