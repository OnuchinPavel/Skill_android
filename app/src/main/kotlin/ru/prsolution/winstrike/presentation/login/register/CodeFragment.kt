package ru.prsolution.winstrike.presentation.login.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.fmt_code.*
import org.jetbrains.anko.support.v4.longToast
import org.koin.androidx.viewmodel.ext.viewModel
import ru.prsolution.winstrike.R
import ru.prsolution.winstrike.data.repository.resouces.ResourceState
import ru.prsolution.winstrike.domain.models.common.MessageResponse
import ru.prsolution.winstrike.presentation.NavigationListener
import ru.prsolution.winstrike.presentation.main.FooterProvider
import ru.prsolution.winstrike.presentation.model.login.SmsInfo
import ru.prsolution.winstrike.presentation.utils.*
import ru.prsolution.winstrike.presentation.utils.pref.PrefUtils
import ru.prsolution.winstrike.viewmodel.SmsViewModel
import timber.log.Timber

/**
 * Created by oleg on 15/03/2018.
 */

class CodeFragment : Fragment() {

    private val mSmsVm: SmsViewModel by viewModel()

    //    private var presenter: UserConfirmPresenter? = null
    private var mPhone: String = ""
    private var mCode: String = ""


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return context?.inflate(R.layout.fmt_code)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        mSmsVm.messageResponse.observe(this@CodeFragment, Observer {
            it?.let {
                if (it.state ==  ResourceState.SUCCESS) {
                    it.data?.let {
                        onConfirmSuccess(it)
                    }
                } else {
                    it.message?.let {
                        onConfirmFailure(it)
                    }
                }
            }
        })


        // Show phone in hint
        arguments?.let {
            val safeArgs = CodeFragmentArgs.fromBundle(it)
            this.mPhone = safeArgs.phone
        }

        initView()
        (activity as FooterProvider).setPhoneHint(hint_tv, mPhone)
        (activity as FooterProvider).setCodePolicyFooter(tv_policy)
    }


    private fun onConfirmSuccess(message: MessageResponse) {
        PrefUtils.isConfirmed = true
        val action = CodeFragmentDirections.actionToNameFragment()
        action.phone = mPhone
        action.code = mCode
        (activity as NavigationListener).navigate(action)
    }


    private fun onConfirmFailure(appErrorMessage: String) {
        Timber.e("Error on auth: %s", appErrorMessage)
        when {
            appErrorMessage.contains("403") ||
                    appErrorMessage.contains("404") ->
                longToast(getString(ru.prsolution.winstrike.R.string.ac_login_error_user_not_found))
            (appErrorMessage.contains("409")) -> longToast("Не верный код.")
            appErrorMessage.contains("502") -> longToast("Ошибка сервера")
            (appErrorMessage.contains("413")) -> longToast("Не верный формат данных")
            appErrorMessage.contains("No Internet Connection!") ->
                longToast("Интернет подключение не доступно!")
        }

    }


    private fun initView() {
//       Next button -  confirm User and navigate to Name
        next_button.setOnClickListener {

            et_code.validate({ et_code.text!!.isCodeValid() }, getString(R.string.fmt_code_error_lengh))

            when {
                et_code.text!!.isCodeValid() -> {
                    mCode = et_code.text.toString()
                    val smsInfo = SmsInfo(mPhone)

                    mSmsVm.confirm(mCode, smsInfo)

                }
            }
        }

    }

}

