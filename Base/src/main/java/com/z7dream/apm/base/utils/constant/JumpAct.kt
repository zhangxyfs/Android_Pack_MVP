package com.z7dream.apm.base.utils.constant

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.z7dream.apm.base.utils.constant.JumpAct.Companion.main.MAIN_LOGIN_ACTIVITY

/**
 * Created by Z7Dream on 2017/10/30 17:52.
 * Email:zhangxyfs@126.com
 */
class JumpAct {
    companion object {
        object main {
            val MAIN_BOTTOM_ACTIVITY = "com.eblog.mvp.view.ui.MainPageBottomActivity"
            val MAIN_LOGIN_ACTIVITY = "com.eblog.mvp.view.ui.LoginActivity"
            val MAIN_CHOOSECOUNTRY_ACTIVITY = "com.eblog.mvp.view.ui.ChooseCountryActivity"
        }

        object address {
            val ADDRESS_ACTIVITY = "com.eblog.plugin.address.mvp.view.ui.AddressActivity"
        }


        //去登陆
        fun toLogin(activity: Activity) {
            val intent = Intent()
            intent.setClassName(activity, MAIN_LOGIN_ACTIVITY)
            activity.startActivity(intent)
        }

        fun toLogin(context: Context) {
            val intent = Intent()
            intent.setClassName(context, MAIN_LOGIN_ACTIVITY)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }

        fun openActivity(context: Context, classFullName: String) {
            openActivity(context, classFullName, null)
        }

        fun openActivity(context: Context, classFullName: String, pBundle: Bundle?) {
            val intent = Intent()
            intent.setClassName(context, classFullName)
            if (pBundle != null) {
                intent.putExtras(pBundle)
            }
            context.startActivity(intent)
        }

        fun openActivityForResult(activity: Activity, classFullName: String, requestCode: Int, pBundle: Bundle?) {
            val intent = Intent()
            intent.setClassName(activity, classFullName)
            if (pBundle != null) {
                intent.putExtras(pBundle)
            }
            activity.startActivityForResult(intent, requestCode)
        }
    }
}