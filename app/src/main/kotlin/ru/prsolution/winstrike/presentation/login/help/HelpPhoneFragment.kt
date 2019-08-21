package ru.prsolution.winstrike.presentation.login.help

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fmt_help_phone.*
import kotlinx.android.synthetic.main.inc_help_phone.*
import org.koin.androidx.viewmodel.ext.viewModel
import ru.prsolution.winstrike.R
import ru.prsolution.winstrike.presentation.NavigationListener
import ru.prsolution.winstrike.presentation.main.FooterProvider
import ru.prsolution.winstrike.presentation.model.login.SmsInfo
import ru.prsolution.winstrike.presentation.utils.TextFormat.formatPhone
import ru.prsolution.winstrike.presentation.utils.inflate
import ru.prsolution.winstrike.presentation.utils.isPhoneValid
import ru.prsolution.winstrike.presentation.utils.pref.PrefUtils
import ru.prsolution.winstrike.presentation.utils.setPhoneMask
import ru.prsolution.winstrike.presentation.utils.validate
import ru.prsolution.winstrike.viewmodel.SmsViewModel

/**
 * Created by Oleg Sitnikov on 2019-02-24
 */

class HelpPhoneFragment : Fragment() {
    private val mSmsVm: SmsViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return context?.inflate(R.layout.fmt_help_phone)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set footer
        val action = HelpPhoneFragmentDirections.actionToLogin()
        (activity as FooterProvider).setRegisterLoginFooter(textView = login_footer , action = action)

        // Set phone mask
        et_phone_help.setPhoneMask()
        et_phone_help.requestFocus()

        phone_btn.setOnClickListener {

            // Validate phone
            et_phone_help.validate({ et_phone_help.text!!.isPhoneValid() }, getString(R.string.ac_login_error_phone))

            when {
                et_phone_help.text!!.isPhoneValid() -> {

                    // Update phone
                    PrefUtils.phone = formatPhone(et_phone_help.text.toString())
                    val smsInfo = SmsInfo(PrefUtils.phone)
                    // Send Sms
                    mSmsVm.send(smsInfo)

                    // Go to Code screen
                    val action = HelpPhoneFragmentDirections.actionToHelpCode()
                    (activity as NavigationListener).navigate(action)

                }
            }

        }
    }
}