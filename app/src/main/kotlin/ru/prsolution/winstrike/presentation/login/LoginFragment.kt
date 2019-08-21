package ru.prsolution.winstrike.presentation.login

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.fmt_login.*
import org.jetbrains.anko.support.v4.longToast
import org.koin.androidx.viewmodel.ext.viewModel
import ru.prsolution.winstrike.domain.models.login.AuthResponse
import ru.prsolution.winstrike.presentation.model.login.LoginInfo
import ru.prsolution.winstrike.presentation.utils.TextFormat.formatPhone
import ru.prsolution.winstrike.presentation.utils.pref.PrefUtils
import ru.prsolution.winstrike.viewmodel.LoginViewModel
import kotlinx.android.synthetic.main.inc_password.*
import kotlinx.android.synthetic.main.inc_phone.*
import ru.prsolution.winstrike.R
import ru.prsolution.winstrike.data.repository.resouces.ResourceState
import ru.prsolution.winstrike.presentation.NavigationListener
import ru.prsolution.winstrike.presentation.main.FooterProvider
import ru.prsolution.winstrike.presentation.model.fcm.FCMPid
import ru.prsolution.winstrike.presentation.model.login.SmsInfo
import ru.prsolution.winstrike.presentation.utils.*
import ru.prsolution.winstrike.viewmodel.FCMViewModel
import ru.prsolution.winstrike.viewmodel.SmsViewModel
import timber.log.Timber


/**
 * Created by Oleg Sitnikov on 2019-02-16
 */

class LoginFragment : Fragment() {

    private val mVm: LoginViewModel by viewModel()
    private val mSmsVm: SmsViewModel by viewModel()
    private val mVmFCM: FCMViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return context?.inflate(R.layout.fmt_login)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        arguments?.let {
            val args = LoginFragmentArgs.fromBundle(it)
            if (args.clearStack) {
                (activity as NavigationListener).mNavController.popBackStack()
            }
        }

        mVm.authResponse.observe(this@LoginFragment, Observer {
            it?.let {
                if (it.state == ResourceState.SUCCESS) {
                    it.data?.let {
                        onAuthSuccess(it)
                    }
                } else {
                    it.message?.let {
                        onAuthFailure(it)
                    }
                }

            }
        })

        mVmFCM.messageResponse.observe(this@LoginFragment, Observer {
            it?.let { resource ->

                if (resource.state == ResourceState.SUCCESS){
                    resource.data?.let {
                        Timber.tag("$$$").d("FCM token sent!")
                    }
                } else {
                    resource.message?.let {
                        Timber.tag("$$$").e("FCM token DIDN'T sent!")
                    }
                }
            }
        })

        initView()
    }

    private fun initView() {
        et_phone.setPhoneMask()
        login_button.isEnabled = true

        login_button.setOnClickListener {

            et_phone.validate({ et_phone.text!!.isPhoneValid() }, getString(R.string.ac_login_error_phone))

            et_password.validate(
                { et_password.text!!.isPasswordValid() },
                getString(R.string.ac_login_error_password_lengh)
            )

            when {
                et_phone.text!!.isPhoneValid() &&
                        et_password.text!!.isPasswordValid() -> {

                    val username = formatPhone(et_phone.text.toString())
                    val password = et_password.text.toString()
                    val loginModel = LoginInfo(username, password)

                    mVm.getUser(loginModel)
                }
            }
        }

        help_link_tv.setOnClickListener {
            val action = LoginFragmentDirections.nextActionHelp()
            (activity as NavigationListener).navigate(action)
        }

        setRegisterFooter()
        (activity as FooterProvider).setLoginPolicyFooter(tv_conditions)
    }

    private fun onAuthSuccess(authResponse: AuthResponse) {
        val confirmed = authResponse.user?.confirmed ?: false

        updateUser(authResponse)

        initFCM() // FCM push notifications

        if (confirmed) {
            val action = LoginFragmentDirections.actionToCityList()
            (activity as NavigationListener).navigate(action)
        } else {
            //TODO: Fix it!!!
            longToast("Пользователь не подтвержден. Отправлена СМС для подверждения номера телефона.")
            val phone = authResponse.user?.phone
            val smsInfo = SmsInfo(phone)
            mSmsVm.send(smsInfo)
            val action = LoginFragmentDirections.actionToCode()
            action.phone = PrefUtils.phone!!
            (activity as NavigationListener).navigate(action)
        }
    }

    private fun onAuthFailure(appErrorMessage: String) {
        Timber.e("Error on auth: %s", appErrorMessage)
        when {
            appErrorMessage.contains("404") ->
                longToast(getString(ru.prsolution.winstrike.R.string.ac_login_error_user_not_found))
            appErrorMessage.contains("403") ->
                longToast(getString(ru.prsolution.winstrike.R.string.fmt_login_error_password_wrong))
            appErrorMessage.contains("502") -> longToast(getString(R.string.fmt_login_server_error))
            appErrorMessage.contains(getString(R.string.fmt_login_noinet)) ->
                longToast(getString(R.string.fmt_login_message_noinet))
        }

    }

    // TODO: Use Cash (RxPaper2).
    private fun updateUser(authResponse: AuthResponse) {
//        longToast("AuthResponse token: ${authResponse.token}")

        PrefUtils.name = authResponse.user?.name ?: ""
        PrefUtils.token = authResponse.token ?: ""
        PrefUtils.phone = authResponse.user?.phone ?: ""
        PrefUtils.isConfirmed = authResponse.user?.confirmed ?: false
        PrefUtils.publicid = authResponse.user?.publicId ?: ""

//        longToast("PrefUtils.token [Login Fragrment]: ${PrefUtils.token}")

    }

    // Send FCM code to server
    private fun initFCM() {
        val fcmToken = PrefUtils.fcmtoken
        fcmToken?.let {
            mVmFCM.sendFCMCode(FCMPid(it))
        }
    }


    fun setRegisterFooter() {
        val register = SpannableString(getString(R.string.fmt_login_title_register))
        val registerClick = object : ClickableSpan() {
            override fun onClick(v: View) {
                val action = LoginFragmentDirections.actionToNavigationRegister()
                (activity as NavigationListener).navigate(action)
            }
        }
        register.setSpan(registerClick, 18, register.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        tv_register.movementMethod = LinkMovementMethod.getInstance()
        tv_register.text = register

    }
}
