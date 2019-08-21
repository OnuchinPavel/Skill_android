package ru.prsolution.winstrike.presentation.login.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.fmt_name.*
import org.jetbrains.anko.support.v4.longToast
import org.koin.androidx.viewmodel.ext.viewModel
import ru.prsolution.winstrike.R
import ru.prsolution.winstrike.data.repository.resouces.ResourceState
import ru.prsolution.winstrike.domain.models.common.MessageResponse
import ru.prsolution.winstrike.presentation.NavigationListener
import ru.prsolution.winstrike.presentation.main.FooterProvider
import ru.prsolution.winstrike.presentation.model.login.ProfileInfo
import ru.prsolution.winstrike.presentation.utils.inflate
import ru.prsolution.winstrike.presentation.utils.isNameValid
import ru.prsolution.winstrike.presentation.utils.pref.PrefUtils
import ru.prsolution.winstrike.presentation.utils.validate
import ru.prsolution.winstrike.viewmodel.ProfileViewModel
import timber.log.Timber

/**
 * Created by Oleg Sitnikov on 2019-02-19
 */

class NameFragment : Fragment() {

    private val mVm: ProfileViewModel by viewModel()


    var mPhone = ""
    var mCode = ""
    lateinit var mUserInfo: ProfileInfo

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return context?.inflate(R.layout.fmt_name)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        mVm.messageResponse.observe(this@NameFragment, Observer {
            it?.let {
                if (it.state == ResourceState.SUCCESS) {
                    it.data?.let {
                        onUpdateSuccess(it)
                    }
                } else {
                    it.message?.let {
                        onUpdateFailure(it)
                    }
                }
            }
        })

        arguments?.let {
            val safeArgs = NameFragmentArgs.fromBundle(it)
            mPhone = safeArgs.phone
            mCode = safeArgs.code
        }

        et_sms_code.text = mCode

        (activity as FooterProvider).setPhoneHint(phone_hint_tv, mPhone)
        (activity as FooterProvider).setNamePolicyFooter(tv_name_policy)

        // Поехали!
        //TODO go to City Nav Graph
        start_button.setOnClickListener {

            et_name.validate({ et_name.text!!.isNameValid() }, getString(R.string.fmt_name_error_lengh))

            when {
                et_name.text!!.isNameValid() -> {

                    mUserInfo = ProfileInfo(mPhone, et_name.text.toString())
                    mVm.updateUserProfile(PrefUtils.publicid!!, mUserInfo)

                }

            }

        }

    }

    private fun onUpdateSuccess(message: MessageResponse) {
        PrefUtils.name = mUserInfo.name
        val action = NameFragmentDirections.actionToCityList()
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
