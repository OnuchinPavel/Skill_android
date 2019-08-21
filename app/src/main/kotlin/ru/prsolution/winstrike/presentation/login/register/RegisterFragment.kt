package ru.prsolution.winstrike.presentation.login.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.fmt_register.*
import kotlinx.android.synthetic.main.inc_password.*
import kotlinx.android.synthetic.main.inc_phone.*
import org.jetbrains.anko.support.v4.longToast
import org.koin.androidx.viewmodel.ext.viewModel
import ru.prsolution.winstrike.R
import ru.prsolution.winstrike.data.repository.resouces.ResourceState
import ru.prsolution.winstrike.domain.models.login.AuthResponse
import ru.prsolution.winstrike.presentation.NavigationListener
import ru.prsolution.winstrike.presentation.main.FooterProvider
import ru.prsolution.winstrike.presentation.model.login.NewUserInfo
import ru.prsolution.winstrike.presentation.utils.*
import ru.prsolution.winstrike.presentation.utils.TextFormat.formatPhone
import ru.prsolution.winstrike.presentation.utils.pref.PrefUtils
import ru.prsolution.winstrike.viewmodel.RegisterViewModel
import timber.log.Timber

/*
 * Created by oleg on 31.01.2018.
 */

class RegisterFragment : Fragment() {

    private val mRegisterVm: RegisterViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return context?.inflate(R.layout.fmt_register)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        mRegisterVm.authResponse.observe(this@RegisterFragment, Observer {
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
        initView()
        val action = RegisterFragmentDirections.actionToNavigationLogin()
        (activity as FooterProvider).setRegisterLoginFooter(tv_register_footer, action)
    }

    private fun onAuthSuccess(authResponse: AuthResponse) {
        val confirmed = authResponse.user?.confirmed ?: false

        updateUser(authResponse)

        //TODO go to City Nav Graph
        if (confirmed) {
//            val action = LoginFragmentDirections.actionToMainActivity()
//            (activity as LoginActivity).navigate(action)
        } else {
//            "Пользователь не подтвержден. Отправляем СМС и перенаправляемся на страницу подверждения СМС кода."
//            val smsInfo = SmsInfo(phone)
//            mSmsVm.send(smsInfo)
            val action = RegisterFragmentDirections.actionToNavigationCode()
            val phone = authResponse.user?.phone
            phone?.let { action.phone = it }
            (activity as NavigationListener).navigate(action)

        }

    }

    // TODO: Use Cash (RxPaper2).
    private fun updateUser(authResponse: AuthResponse) {
        PrefUtils.name = authResponse.user?.name ?: ""
        PrefUtils.password = et_password.text.toString()
        PrefUtils.token = authResponse.token ?: ""
        PrefUtils.phone = authResponse.user?.phone ?: ""
        PrefUtils.isConfirmed = authResponse.user?.confirmed ?: false
        PrefUtils.publicid = authResponse.user?.publicId ?: ""
    }


    private fun onAuthFailure(appErrorMessage: String) {
        Timber.e("Error on auth: %s", appErrorMessage)
        when {
            appErrorMessage.contains("403") ||
                    appErrorMessage.contains("404") ->
                longToast(getString(ru.prsolution.winstrike.R.string.ac_login_error_user_not_found))
            (appErrorMessage.contains("409")) -> longToast("Пользователь уже существует")
            (appErrorMessage.contains("422")) -> longToast("Пароль слишком короткий.")
            appErrorMessage.contains("502") -> longToast("Ошибка сервера")
            (appErrorMessage.contains("413")) -> longToast("Не верный формат данных")
            appErrorMessage.contains("No Internet Connection!") ->
                longToast("Интернет подключение не доступно!")
        }

    }


    private fun initView() {
        // Validate phone and password and register user
        et_phone.setPhoneMask()

//       Next button - navigate to Code and send SMS
        register_button.setOnClickListener {

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
                    val newUserModel = NewUserInfo(username, password)

                    mRegisterVm.getUser(newUserModel)

                }
            }
        }


    }


    fun init() {
/*        next_button_phone!!.setOnClickListener {
            // Создание пользователя и переход на страницу подтверждения пароля
            user = LoginModel(
                phone = formatPhone(et_phone?.text.toString()),
                password = et_password?.text.toString()
            )

//            presenter!!.createUser(user!!)
        }

    }

    fun onSendSmsSuccess(messageResponse: MessageResponse) {
        Timber.d("Sms send successfully: %s", messageResponse.message)
        toast("Код выслан")
        val intent = Intent(requireActivity(), CodeFragment::class.java)
        intent.putExtra("phone", user!!.phone)
        startActivity(intent)
    }

    fun onSmsSendFailure(appErrorMessage: String) {
        Timber.d("Sms send failure: %s", appErrorMessage)
    }

    private fun getConfirmSmsModel(phone: String): SmsModel {
        return SmsModel(phone)
    }


    /**
     * Register new user and send him sms with confirm code.
     */
    fun onRegisterSuccess(messageResponse: AuthResponseEntity) {
        with(PrefUtils) {
            token = messageResponse.token
            publicid = messageResponse.user?.publicId!!
            isConfirmed = false
            phone = user?.phone
            name = "NoName"
        }

        val userDb = UserModel(
            id = null,
            confirmed = false,
            phone = user?.phone,
            publickId = messageResponse.user?.publicId,
            token = messageResponse.token
        )

        val intent = Intent(requireActivity(), CodeFragment::class.java)
        intent.putExtra("phone", user!!.phone)
        startActivity(intent)
    }

    fun onRegisterFailure(appErrorMessage: String) {
        Timber.d("Register failure: %s", appErrorMessage)
        if (appErrorMessage.contains("409")) longToast("Пользователь уже существует")
        if (appErrorMessage.contains("422")) longToast("Пароль слишком короткий.")
        if (appErrorMessage.contains("413")) {
            Timber.w("RegisterUser: Передан не правильный формат данных JSON")
            longToast("Пользователь не создан")
        }
    }

*/

    }

}

