package ru.prsolution.winstrike.presentation.login.help

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.fmt_change_passw.*
import kotlinx.android.synthetic.main.inc_help_password_bottom.*
import kotlinx.android.synthetic.main.inc_help_password_top.*
import org.jetbrains.anko.support.v4.longToast
import org.koin.androidx.viewmodel.ext.viewModel
import ru.prsolution.winstrike.R
import ru.prsolution.winstrike.data.repository.resouces.ResourceState
import ru.prsolution.winstrike.domain.models.common.MessageResponse
import ru.prsolution.winstrike.presentation.NavigationListener
import ru.prsolution.winstrike.presentation.model.login.Password
import ru.prsolution.winstrike.presentation.utils.inflate
import ru.prsolution.winstrike.presentation.utils.isPasswordValid
import ru.prsolution.winstrike.presentation.utils.pref.PrefUtils
import ru.prsolution.winstrike.presentation.utils.validate
import ru.prsolution.winstrike.viewmodel.ProfileViewModel
import timber.log.Timber

/**
 * Created by Oleg Sitnikov on 2019-02-24
 */

class HelpPasswordFragment : Fragment() {

    private val mVm: ProfileViewModel by viewModel()
    private var mConfirmCode: String = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return context?.inflate(R.layout.fmt_change_passw)
    }

    private lateinit var mPassword: Password

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            val safeArg = HelpPasswordFragmentArgs.fromBundle(it)
            mConfirmCode = safeArg.code
        }

        save_btn.setOnClickListener {

            et_password_top.validate(
                { et_password_bottom.text!!.isPasswordValid() },
                getString(ru.prsolution.winstrike.R.string.ac_login_error_password_lengh)
            )
            et_password_bottom.validate(
                { et_password_bottom.text!!.isPasswordValid() },
                getString(ru.prsolution.winstrike.R.string.ac_login_error_password_lengh)
            )

            when {
                et_password_top.text.toString() == et_password_bottom.text.toString()
                -> {

                    mPassword = Password(PrefUtils.phone!!, et_password_top.text.toString())
                    mVm.changePassword(mConfirmCode, mPassword)
                }
            }

            mVm.messageResponse.observe(this@HelpPasswordFragment, Observer {
                it?.let { resource ->
                    if (resource.state == ResourceState.SUCCESS) {
                        resource.data?.let {
                            onUpdateSuccess(it)
                        }
                    } else {
                        resource.message?.let {
                            onUpdateFailure(it)
                        }
                    }


                }
            })
        }
    }

    private fun onUpdateSuccess(message: MessageResponse) {
        longToast("Пароль успешно изменен")
        // Update password
        PrefUtils.password = mPassword.password
        // Go to Login
        val action = HelpPasswordFragmentDirections.actionToNavigationLogin()
        action.clearStack = true
        (activity as NavigationListener).navigate(action)
    }


    private fun onUpdateFailure(appErrorMessage: String) {
        Timber.e("Error on auth: %s", appErrorMessage)
        when {
            appErrorMessage.contains("403") ||
                    appErrorMessage.contains("404") ->
                longToast(getString(ru.prsolution.winstrike.R.string.ac_login_error_user_not_found))
            (appErrorMessage.contains("409")) -> longToast("Не верный код.")
            appErrorMessage.contains("502") -> longToast("Ошибка сервера")
            appErrorMessage.contains("401") -> longToast("Ошибка авторизации")
            (appErrorMessage.contains("413")) -> longToast("Не верный формат данных")
            appErrorMessage.contains("No Internet Connection!") ->
                longToast("Интернет подключение не доступно!")
        }

    }

}