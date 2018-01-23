package com.z7dream.apm.mvp.view.ui

import android.content.Intent
import android.os.Bundle
import android.text.*
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.Toast
import com.z7dream.apm.R
import com.z7dream.apm.base.utils.constant.JumpAct
import com.z7dream.apm.base.utils.constant.JumpAct.Companion.main.MAIN_CHOOSECOUNTRY_ACTIVITY
import com.z7dream.apm.base.utils.tools.EToast
import com.z7dream.apm.base.widget.LoadingDialog
import com.z7dream.apm.mvp.base.PluginBaseFragment
import com.z7dream.apm.mvp.presenter.LoginPresenter
import com.z7dream.apm.mvp.view.contract.LoginContract
import com.z7dream.apm.mvp.view.model.MainModel
import kotlinx.android.synthetic.main.activity_login_phone.*
import java.util.regex.Pattern

/**
 * Created by Z7Dream on 2017/11/1 15:03.
 * Email:zhangxyfs@126.com
 */
open class LoginFragment : PluginBaseFragment<LoginContract.Presenter>(), LoginContract.View {
    private var whichPage: Int = -1;
    private var isUsernameInput: Boolean = false
    private var isPasswordInput: Boolean = false
    private var isEmailInput: Boolean = false
    private var isCheckInput: Boolean = false
    private var isPwdSee = true
    private var recLen = 60
    private val COUNTRY = 201
    private val USERNAME_KEY = 1
    private val PASSWORD_KEY = 2
    private val CHECK_KEY = 3
    private val EMAIL_KEY = 4

    private lateinit var loadingDialog: LoadingDialog;


    override fun layoutID(): Int = R.layout.activity_login_phone
    override fun createPresenter(): LoginContract.Presenter = LoginPresenter(context, this)

    override fun init(view: View?, savedInstanceState: Bundle?) {
        if (arguments != null) {
            whichPage = arguments.getInt(WHICH_PAGE)
        }
        loadingDialog = LoadingDialog.getInstance(context, null, false, false)
    }

    override fun data() {
        iv_login_del_ln.visibility = View.INVISIBLE
        login_email_clear.visibility = View.INVISIBLE
        et_login_name.addTextChangedListener(LoginTextWatcher(USERNAME_KEY))
        et_login_pwd.addTextChangedListener(LoginTextWatcher(PASSWORD_KEY))
        et_login_check.addTextChangedListener(LoginTextWatcher(CHECK_KEY))
        login_email_name.addTextChangedListener(LoginTextWatcher(EMAIL_KEY))

        if (!isPwdSee) {
            iv_login_see.setImageResource(R.drawable.ic_see)
            et_login_pwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance())
        } else {
            iv_login_see.setImageResource(R.drawable.ic_unsee)
            et_login_pwd.setTransformationMethod(PasswordTransformationMethod.getInstance())
        }

        when (whichPage) {
            R.id.nav_login_phone -> {
                //国家
                login_countrylayout.visibility = View.VISIBLE
                loginbymobile_countryline.visibility = View.VISIBLE

                //手机号登录输入框
                login_mobilelayout.visibility = View.VISIBLE
                loginbymobile_lnline.visibility = View.VISIBLE

                //邮箱
                login_emaillayout.visibility = View.GONE
                loginbymobile_emailline.visibility = View.GONE

                //密码输入框
                login_pwdlayout.visibility = View.VISIBLE
                loginbymobile_pwdline.visibility = View.VISIBLE
                et_login_pwd.setText("")

                //验证码输入框
                login_vercodelayout.visibility = View.GONE
                loginbymobile_vercodeline.visibility = View.GONE
                et_login_name.setText("")
            }
            R.id.nav_login_code -> {
                //国家
                login_countrylayout.visibility = View.VISIBLE
                loginbymobile_countryline.visibility = View.VISIBLE

                //手机号登录输入框
                login_mobilelayout.visibility = View.VISIBLE
                loginbymobile_lnline.visibility = View.VISIBLE

                //邮箱
                login_emaillayout.visibility = View.GONE
                loginbymobile_emailline.visibility = View.GONE

                //密码输入框
                login_pwdlayout.visibility = View.GONE
                loginbymobile_pwdline.visibility = View.GONE

                //验证码输入框
                login_vercodelayout.visibility = View.VISIBLE
                loginbymobile_vercodeline.visibility = View.VISIBLE
                et_login_check.setText("")
            }
            R.id.nav_login_mail -> {
                //国家
                login_countrylayout.visibility = View.GONE
                loginbymobile_countryline.visibility = View.GONE

                //手机号登录输入框
                login_mobilelayout.visibility = View.GONE
                loginbymobile_lnline.visibility = View.GONE

                //邮箱
                login_emaillayout.visibility = View.VISIBLE
                loginbymobile_emailline.visibility = View.VISIBLE

                //密码输入框
                login_pwdlayout.visibility = View.VISIBLE
                loginbymobile_pwdline.visibility = View.VISIBLE
                et_login_pwd.setText("")

                //验证码输入框
                login_vercodelayout.visibility = View.GONE
                loginbymobile_vercodeline.visibility = View.GONE
            }
        }

        //删除邮箱登录名称
        iv_login_del_ln.setOnClickListener({
            if (login_email_name.text.toString().length > 0) {
                login_email_name.setText("")
            }
            login_email_clear.visibility = View.INVISIBLE
        })

        //删除邮箱登录名称
        login_email_clear.setOnClickListener({
            if (login_email_name.text.toString().length > 0) {
                login_email_name.setText("")
            }
            login_email_clear.visibility = View.INVISIBLE
        })

        //密码是否显示
        fl_login_see.setOnClickListener({
            if (isPwdSee) {
                iv_login_see.setImageResource(R.drawable.ic_see)
                et_login_pwd.transformationMethod = HideReturnsTransformationMethod.getInstance()
            } else {
                iv_login_see.setImageResource(R.drawable.ic_unsee)
                et_login_pwd.transformationMethod = PasswordTransformationMethod.getInstance()
            }
            isPwdSee = !isPwdSee
            et_login_pwd.postInvalidate()
            val charSequence = et_login_pwd.text
            if (charSequence != null) {
                val spanText = charSequence as Spannable
                Selection.setSelection(spanText, charSequence.length)
            }
        })

        login_countrylayout.setOnClickListener({
            JumpAct.openActivityForResult(activity, MAIN_CHOOSECOUNTRY_ACTIVITY, COUNTRY, null);
        })

        //登录验证
        tv_login_get_check.setOnClickListener({
            if (login_countrycode.text.toString() == "+86") {
                if (et_login_name.text.toString().length == 11 && isMobileNO(et_login_name.text.toString())) {
                    getHandler()?.post(runnable)
                    getPresenter()?.getCode(et_login_name.text.toString(), login_countrycode.text.toString())
                } else {
                    EToast.get().showToast(context, getString(R.string.login_phone_num_error_str))
                }
            } else {
                getHandler()?.post(runnable)
                getPresenter()?.getCode(et_login_name.text.toString(), login_countrycode.text.toString())
            }
        })

        //登录
        btn_login.setOnClickListener({
            when (whichPage) {
                R.id.nav_login_phone -> {
                    if (!isUsernameInput || TextUtils.isEmpty(et_login_name.text.toString())) {
                        Toast.makeText(context, R.string.login_input_name_str, Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }
                    if (!isPasswordInput || TextUtils.isEmpty(et_login_pwd.text.toString())) {
                        Toast.makeText(context, R.string.login_input_pwd_str, Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }
                    if (login_countrycode.text.toString() == "+86") {
                        if (et_login_name.text.toString().length == 11 && isMobileNO(et_login_name.text.toString())) {
                            loadingDialog.startLoading()
                            getPresenter()?.login(et_login_name.text.toString(), et_login_pwd.text.toString())
                        } else {
                            EToast.get().showToast(context, getString(R.string.login_enter_phonenum_error_str))
                        }
                    } else {
                        loadingDialog.startLoading()
                        getPresenter()?.login(et_login_name.text.toString(), et_login_pwd.text.toString())
                    }
                }

                R.id.nav_login_code -> {
                    if (!isUsernameInput || TextUtils.isEmpty(et_login_name.text.toString())) {
                        Toast.makeText(context, R.string.login_input_name_str, Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }
                    if (!isCheckInput || TextUtils.isEmpty(et_login_check.text.toString())) {
                        Toast.makeText(context, R.string.login_input_check_str, Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }
                    //TODO:验证码登录
                    loadingDialog.startLoading()
                    getPresenter()?.loginCode(et_login_name.text.toString(), et_login_check.text.toString())
                }

                R.id.nav_login_mail -> {
                    if (!isEmailInput || TextUtils.isEmpty(login_email_name.text.toString())) {
                        Toast.makeText(context, R.string.login_input_name_str, Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }
                    if (!isPasswordInput || TextUtils.isEmpty(et_login_pwd.text.toString())) {
                        Toast.makeText(context, R.string.login_input_pwd_str, Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }
                    //TODO:邮箱登录
                    val email = login_email_name.text.toString()
                    if (isEmail(email)) {
                        loadingDialog.startLoading()
                        getPresenter()?.loginEmail(login_email_name.text.toString(), et_login_pwd.text.toString())

                    } else {
                        EToast.get().showToast(context, getString(R.string.login_email_error_str))
                    }

                }
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
//        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data)
//        if (resultCode != RESULT_OK) return
//        when (requestCode) {
//            COUNTRY -> {
//                val country = data.getSerializableExtra("country") as ChooseCountryModel
//                loginbymobile_countryname.setText(country.getCountryName())
//                login_countrycode.setText(country.getCountryCode())
//            }
//
//            else -> {
//            }
//        }
    }

    override fun getDataSucc(modelList: List<MainModel>, isRef: Boolean) {
    }

    override fun getDataFail(str: String?) {
    }

    private fun isEmail(email: String?): Boolean {
        if (null == email || "" == email) return false
        //Pattern p = Pattern.compile("\\w+@(\\w+.)+[a-z]{2,3}"); //简单匹配
        val p = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*")//复杂匹配
        val m = p.matcher(email)
        return m.matches()
    }

    /**
     * 验证手机格式
     */
    private fun isMobileNO(mobiles: String): Boolean {
        /*
    移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
    联通：130、131、132、152、155、156、185、186
    电信：133、153、180、189、（1349卫通）
    总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
    */
        val telRegex = "[1][3578]\\d{9}"//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        return if (TextUtils.isEmpty(mobiles))
            false
        else
            mobiles.matches(telRegex.toRegex())
    }

    private var runnable: Runnable = object : Runnable {
        override fun run() {
            if (recLen == 0) {
                if (tv_login_getcode_text != null)
                    tv_login_getcode_text.text = "获取验证码"

                if (tv_login_get_check != null)
                    tv_login_get_check.isEnabled = true

                if (getHandler() != null)
                    getHandler()?.removeCallbacks(this)

                recLen = 60
                return
            }

            recLen--
            if (tv_login_get_check != null)
                tv_login_get_check.isEnabled = false
            if (tv_login_getcode_text != null)
                tv_login_getcode_text.setText(getString(R.string.login_ns_send_str, recLen))
            if (getHandler() != null)
                getHandler()?.postDelayed(this, 1000)
        }
    }

    private inner class LoginTextWatcher internal constructor(private val which: Int) : TextWatcher {

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            val isTextHasLength = s.length > 0
            when (which) {
                USERNAME_KEY -> {
                    isUsernameInput = isTextHasLength
                    iv_login_del_ln.visibility = if (isUsernameInput) View.VISIBLE else View.INVISIBLE
                }
                PASSWORD_KEY -> isPasswordInput = s.length >= 6
                CHECK_KEY -> isCheckInput = isTextHasLength
                EMAIL_KEY -> {
                    isEmailInput = isTextHasLength
                    login_email_clear.visibility = if (isEmailInput) View.VISIBLE else View.INVISIBLE
                }
                else -> {
                }
            }

            when (whichPage) {
                R.id.nav_login_phone -> {
                    btn_login.isEnabled = isUsernameInput && isPasswordInput;
                }
                R.id.nav_login_code -> {
                    btn_login.isEnabled = isUsernameInput && isCheckInput
                }
                R.id.nav_login_mail -> {
                    btn_login.isEnabled = isEmailInput && isPasswordInput
                }
            }
        }

        override fun afterTextChanged(s: Editable) {

        }
    }

    companion object {
        val WHICH_PAGE: String = "which_page"
    }
}