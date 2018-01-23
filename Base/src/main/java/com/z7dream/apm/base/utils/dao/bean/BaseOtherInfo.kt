package com.z7dream.apm.base.utils.dao.bean

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

/**
 * Created by Z7Dream on 2017/11/9 14:51.
 * Email:zhangxyfs@126.com
 */
@Entity
class BaseOtherInfo {
    @Id
    var _id: Long? = null

    var title: String? = null

    var value: String? = null
}