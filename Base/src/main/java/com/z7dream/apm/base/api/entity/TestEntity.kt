package com.z7dream.apm.base.api.entity

/**
 * Created by Z7Dream on 2017/10/30 13:28.
 * Email:zhangxyfs@126.com
 */
class TestEntity {
    var result: String? = null//可以为""不能为null,message和code必须有一个
    var code: String? = null//必须有值不能为空
    var message: String? = null
}