package ru.prsolution.winstrike.presentation.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.inc_prof_city.*
import ru.prsolution.winstrike.presentation.utils.inflate
import ru.prsolution.winstrike.presentation.utils.onRightDrawableClicked
import ru.prsolution.winstrike.presentation.utils.pref.PrefUtils
import com.google.android.material.textfield.TextInputLayout
import android.view.inputmethod.InputConnection
import android.view.inputmethod.EditorInfo
import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatAutoCompleteTextView
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.fmt_profile_prof.*
import kotlinx.android.synthetic.main.inc_password.*
import kotlinx.android.synthetic.main.inc_prof_name.*
import org.jetbrains.anko.support.v4.longToast
import org.koin.androidx.viewmodel.ext.viewModel
import ru.prsolution.winstrike.R
import ru.prsolution.winstrike.data.repository.resouces.ResourceState
import ru.prsolution.winstrike.domain.models.common.MessageResponse
import ru.prsolution.winstrike.presentation.NavigationListener
import ru.prsolution.winstrike.presentation.main.ToolbarTitleListener
import ru.prsolution.winstrike.presentation.model.login.ProfileInfo
import ru.prsolution.winstrike.presentation.utils.isNameValid
import ru.prsolution.winstrike.presentation.utils.validate
import ru.prsolution.winstrike.viewmodel.ProfileViewModel
import timber.log.Timber


/*
 * Created by oleg on 03.02.2018.
 */

class ProfileTabFragment : Fragment() {
    private val mVmProfile: ProfileViewModel by viewModel()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return context?.inflate(ru.prsolution.winstrike.R.layout.fmt_profile_prof)
    }

    lateinit var mUserInfo: ProfileInfo


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        save_btn.setOnClickListener {

            et_name_prof.validate(
                { et_name_prof.text?.isNameValid()!! },
                getString(ru.prsolution.winstrike.R.string.fmt_name_error_lengh)
            )

            when {
                et_name_prof.text!!.isNameValid() -> {

                    mUserInfo = ProfileInfo(PrefUtils.phone!!, et_name_prof.text.toString())
                    mVmProfile.updateUserProfile(PrefUtils.publicid!!, mUserInfo)

                }

            }
        }
        mVmProfile.messageResponse.observe(this@ProfileTabFragment, Observer {
            it?.let {
                if (it.state == ResourceState.SUCCESS){
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


        et_name_prof.setText(PrefUtils.name)
        et_password.setText(PrefUtils.password)

        et_city.setText(PrefUtils.cityName)

        et_city.onRightDrawableClicked {
            val action = ProfileFragmentDirections.actionToCityList()
            (activity as NavigationListener).navigate(action)
        }

    }


    private fun onUpdateSuccess(message: MessageResponse) {
        longToast("Информация обновлена")
        (activity as ToolbarTitleListener).updateTitle(mUserInfo.name)
        PrefUtils.name = mUserInfo.name
        PrefUtils.cityName = et_city.text.toString()
    }


    private fun onUpdateFailure(appErrorMessage: String) {
        Timber.e("Error on auth: %s", appErrorMessage)
        when {
            appErrorMessage.contains("403") ||
                    appErrorMessage.contains("404") ->
                longToast(getString(ru.prsolution.winstrike.R.string.ac_login_error_user_not_found))
            (appErrorMessage.contains("409")) -> longToast("Не верный код.")
            appErrorMessage.contains("502") -> longToast("Ошибка сервера")
            appErrorMessage.contains("401") -> longToast(getString(R.string.message_error_auth))
            (appErrorMessage.contains("413")) -> longToast("Не верный формат данных")
            appErrorMessage.contains("No Internet Connection!") ->
                longToast("Интернет подключение не доступно!")
        }
    }
}

