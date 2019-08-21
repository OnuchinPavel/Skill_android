package ru.prsolution.winstrike.presentation.main

import android.app.Activity
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.*
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import com.afollestad.materialdialogs.MaterialDialog
import kotlinx.android.synthetic.main.ac_mainscreen.*
import kotlinx.android.synthetic.main.toolbar.*
import org.koin.androidx.viewmodel.ext.viewModel
import ru.prsolution.winstrike.BuildConfig
import ru.prsolution.winstrike.R
import ru.prsolution.winstrike.data.repository.resouces.ResourceState
import ru.prsolution.winstrike.domain.models.arena.SeatCarousel
import ru.prsolution.winstrike.presentation.NavigationListener
import ru.prsolution.winstrike.presentation.injectFeature
import ru.prsolution.winstrike.presentation.login.LoginFragmentDirections
import ru.prsolution.winstrike.presentation.main.carousel.CarouselFragment
import ru.prsolution.winstrike.presentation.utils.Constants
import ru.prsolution.winstrike.presentation.utils.Constants.URL_CONDITION
import ru.prsolution.winstrike.presentation.utils.Constants.URL_POLITIKA
import ru.prsolution.winstrike.presentation.utils.date.TimeDataModel
import ru.prsolution.winstrike.presentation.utils.hide
import ru.prsolution.winstrike.presentation.utils.pref.PrefUtils
import ru.prsolution.winstrike.presentation.utils.show
import ru.prsolution.winstrike.viewmodel.AppViewModel
import timber.log.Timber

interface ToolbarTitleListener {
    fun updateTitle(title: String)
}

interface OnGooglePlayRedirect {
    fun onGooglePlayButtonClick()
}

interface FooterProvider {
    fun setLoginPolicyFooter(textView: TextView)
    fun setNamePolicyFooter(textView: TextView)
    fun setCodePolicyFooter(textView: TextView)
    fun setPhoneHint(textView: TextView, phone: String?)
    fun setRegisterLoginFooter(textView: TextView, action: NavDirections)
}

class MainActivity : AppCompatActivity(), ToolbarTitleListener,
    CarouselFragment.OnSeatClickListener, NavigationListener, FooterProvider, OnGooglePlayRedirect {

    private val mAppVm: AppViewModel by viewModel()
    override lateinit var mNavController: NavController

    private lateinit var appBarConfiguration: AppBarConfiguration
    private var mDlgMapLegend: Dialog? = null
    var mArenaActiveLayoutPid: String? = ""
    private var mCityMenuVisible: Boolean = false
    private lateinit var mMenuCity: MenuItem
    private var mProfileMenuVisible: Boolean = false
    private lateinit var mMenuProfile: MenuItem
    private var mMapMenuVisible: Boolean = false
    private lateinit var mMenuMap: MenuItem

    // Show SetUpFragment when user click on carousel view selected seat item
    override fun onCarouselClick(seat: SeatCarousel?) {
        val action = HomeFragmentDirections.nextAction()
        action.seat = seat
        action.activeLayoutPid = this.mArenaActiveLayoutPid.toString()
        findNavController(R.id.main_host_fragment).navigate(action)
    }

    override fun onStart() {
        super.onStart()
        // TODO use mListener (Fix logout!!!)
        PrefUtils.isLogout = false
    }


    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Fix bag with restart app when tap on home icon
        // https://stackoverflow.com/questions/16126511/app-completely-restarting-when-launched-by-icon-press-in-launcher
        if (!isTaskRoot) {
            val intent = intent
            val intentAction = intent.action
            if (intent.hasCategory(Intent.CATEGORY_LAUNCHER) && intentAction != null && intentAction == Intent.ACTION_MAIN) {
                finish()
                return
            }
        }

        // Init Koin modules
        injectFeature()

        dlgMapLegend()

        clearData()

        setContentView(R.layout.ac_mainscreen)


//        Navigation
        mNavController = Navigation.findNavController(this@MainActivity, R.id.main_host_fragment)

        mNavController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.navigation_arena -> {
                    hideKeyboard()
                    bottomNavigation.hide()
                    mCityMenuVisible = false
                    mProfileMenuVisible = false
                    mMapMenuVisible = false
                    invalidateOptionsMenu()
                }
                R.id.navigation_login -> {
                    supportActionBar?.hide()
                    bottomNavigation.hide()
                }
                R.id.navigation_home -> {
                   // onBackPressed()
                    supportActionBar?.show()
                    bottomNavigation.show()
                    destination.label = PrefUtils.arenaName
                    mCityMenuVisible = true
                    mProfileMenuVisible = false
                    mMapMenuVisible = false
                    invalidateOptionsMenu()
                }
                R.id.navigation_order -> {
                    bottomNavigation.show()
                    mCityMenuVisible = false
                    mProfileMenuVisible = false
                    mMapMenuVisible = false
                    invalidateOptionsMenu()
                }
                R.id.navigation_profile -> {
                    bottomNavigation.show()
                    mCityMenuVisible = false
                    mProfileMenuVisible = true
                    mMapMenuVisible = false
                    invalidateOptionsMenu()
                }
                R.id.navigation_map -> {
                    bottomNavigation.hide()
                    mCityMenuVisible = false
                    mProfileMenuVisible = false
                    mMapMenuVisible = true
                    invalidateOptionsMenu()
                }
                R.id.navigation_city_list -> {
                    hideKeyboard()
                    bottomNavigation.hide()
                    mCityMenuVisible = false
                    mProfileMenuVisible = false
                    mMapMenuVisible = false
                    invalidateOptionsMenu()

                }
                else -> {
                    bottomNavigation.hide()
                    mCityMenuVisible = false
                    mProfileMenuVisible = false
                    mMapMenuVisible = false
                    invalidateOptionsMenu()
                }
            }
        }

        appBarConfiguration = AppBarConfiguration(mNavController.graph)

        setSupportActionBar(toolbar)
        setupActionBar(mNavController, appBarConfiguration)
        setupBottomNavMenu(mNavController)

        supportActionBar?.hide()
        bottomNavigation.hide()


        // TODO: Check new version of app here
        mAppVm.checkVersion(BuildConfig.VERSION_CODE.toString())

        mAppVm.messageResponse.observe(this@MainActivity, Observer {
            it?.let { resource ->
                // TODO: process error!
                if (resource.state == ResourceState.SUCCESS){
                    resource.data?.let { messageResponse ->
                        Timber.tag("$$$").d("New version checked!")
                        messageResponse.message?.let { message ->
                            if (message.contains("you need to update this app"))
                                showUpdate()
                        }
                    }
                } else {
                        Toast.makeText(this, resource.message, Toast.LENGTH_SHORT).show()
                        Timber.tag("$$$").e("New version checked FAIL!")
                }
            }
        })


    }

    private fun showUpdate() {
        MaterialDialog(this).show {
            title(R.string.ac_main_message_title)
            message(R.string.ac_main_message_app_version_message)
            positiveButton(text = getString(R.string.ac_main_app_version_dialog_ok_btn)) {
                onGooglePlayButtonClick()
            }
            negativeButton(text = getString(R.string.ac_main_app_version_dialog_cancel_btn)) {
                it.dismiss()
            }
            cancelable(false)
            cancelOnTouchOutside(false)
        }
    }


    // Set title in Profile
    override fun updateTitle(title: String) {
        toolbar.title = title
    }

    private fun setupActionBar(
        navController: NavController,
        appBarConfig: AppBarConfiguration
    ) {
        setupActionBarWithNavController(navController, appBarConfig)
    }

    private fun setupBottomNavMenu(navController: NavController) {
        bottomNavigation?.let {
            NavigationUI.setupWithNavController(it, navController)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(mNavController, appBarConfiguration)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        mMenuCity = menu?.findItem(R.id.navigation_city_list)!!
        mMenuProfile = menu.findItem(R.id.action_to_login)
        mMenuMap = menu.findItem(R.id.map_legend)
        mMenuCity.isVisible = mCityMenuVisible
        mMenuProfile.isVisible = mProfileMenuVisible
        mMenuMap.isVisible = mMapMenuVisible
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item?.itemId) {
            R.id.action_to_login -> {
                PrefUtils.token = ""
                while (mNavController.popBackStack()) {
                    mNavController.popBackStack()
                }
                mNavController.navigate(R.id.navigation_login)
                return true
            }

            R.id.map_legend ->
                mDlgMapLegend?.show()


            android.R.id.home -> {
                when (mNavController.currentDestination?.id) {
                     R.id.navigation_home -> {
                        Timber.d("open home")
                        mNavController.navigate(R.id.navigation_arena)
                        return true
                    }
                    R.id.navigation_arena -> {
                        mNavController.navigate(R.id.navigation_city_list)
                        return true
                    }
                    R.id.navigation_city_list ->{
                        finish()
                    }
                    R.id.navigation_profile,
                    R.id.navigation_order
                    -> {
                        Timber.d("open home")
                        mNavController.navigate(R.id.navigation_home)
                        return true
                    }
                }
                return NavigationUI.onNavDestinationSelected(item, mNavController) ||
                        super.onOptionsItemSelected(item)
            }
        }

        return NavigationUI.onNavDestinationSelected(item!!, mNavController) ||
                super.onOptionsItemSelected(item)
    }


    private fun clearData() {
        TimeDataModel.clearPids()
        TimeDataModel.clearDateTime()
    }


    // Fragment navigation
    override fun navigate(action: NavDirections) {
        Navigation.findNavController(this, ru.prsolution.winstrike.R.id.main_host_fragment).navigate(action)
    }


    // TODO: remove this Map actions block:
    private fun dlgMapLegend() {
        mDlgMapLegend = Dialog(this, android.R.style.Theme_Dialog)
        mDlgMapLegend?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        mDlgMapLegend?.setContentView(R.layout.dlg_legend)
        val tvSee = mDlgMapLegend?.findViewById<TextView>(R.id.tv_see)

        tvSee?.setOnClickListener { mDlgMapLegend?.dismiss() }

        mDlgMapLegend?.setCanceledOnTouchOutside(true)
        mDlgMapLegend?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        mDlgMapLegend?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        val window = mDlgMapLegend?.window
        val wlp = window?.attributes

        wlp?.gravity = Gravity.TOP
        wlp?.flags = wlp?.flags?.and(WindowManager.LayoutParams.FLAG_DIM_BEHIND.inv())
        wlp?.y = Constants.LEGEND_MAP_TOP_MARGIN
        window?.attributes = wlp

        mDlgMapLegend?.setCanceledOnTouchOutside(false)
        mDlgMapLegend?.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        mDlgMapLegend?.dismiss()
    }


    //    Login policy footer
    override fun setLoginPolicyFooter(textView: TextView) {

        val textCondAndPolicy = SpannableString(getString(R.string.fmt_login_politika_footer))
        val conditionClick = object : ClickableSpan() {
            override fun onClick(v: View) {
                val action = LoginFragmentDirections.nextActionPolitika(URL_CONDITION)
                action.title = getString(ru.prsolution.winstrike.R.string.fmt_title_condition)
                Navigation.findNavController(this@MainActivity, ru.prsolution.winstrike.R.id.main_host_fragment)
                    .navigate(action)
            }
        }
        val policyClick = object : ClickableSpan() {
            override fun onClick(v: View) {
                val action = LoginFragmentDirections.nextActionPolitika(URL_POLITIKA)
                action.title = getString(ru.prsolution.winstrike.R.string.fmt_login_title_politika)
                Navigation.findNavController(this@MainActivity, ru.prsolution.winstrike.R.id.main_host_fragment)
                    .navigate(action)
            }
        }
        textCondAndPolicy.setSpan(conditionClick, 0, 9, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        textCondAndPolicy.setSpan(policyClick, 12, textCondAndPolicy.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        textView.movementMethod = LinkMovementMethod.getInstance()
        textView.text = textCondAndPolicy
    }

    //    Code policy footer
    override fun setCodePolicyFooter(textView: TextView) {

        val textCondAndPolicy = SpannableString(getString(ru.prsolution.winstrike.R.string.fmt_login_politika_footer))
        val conditionClick = object : ClickableSpan() {
            override fun onClick(v: View) {
                //TODO: fix it
            }
        }
        val policyClick = object : ClickableSpan() {
            override fun onClick(v: View) {
                //TODO: fix it
            }
        }
        textCondAndPolicy.setSpan(conditionClick, 0, 9, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        textCondAndPolicy.setSpan(policyClick, 12, textCondAndPolicy.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        textView.movementMethod = LinkMovementMethod.getInstance()
        textView.text = textCondAndPolicy
    }

    //    Name policy footer
    override fun setNamePolicyFooter(textView: TextView) {

        val textCondAndPolicy = SpannableString(getString(ru.prsolution.winstrike.R.string.fmt_login_politika_footer))
        val conditionClick = object : ClickableSpan() {
            override fun onClick(v: View) {
                //TODO: fix it
            }
        }
        val policyClick = object : ClickableSpan() {
            override fun onClick(v: View) {
                //TODO: fix it
            }
        }
        textCondAndPolicy.setSpan(conditionClick, 0, 9, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        textCondAndPolicy.setSpan(policyClick, 12, textCondAndPolicy.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        textView.movementMethod = LinkMovementMethod.getInstance()
        textView.text = textCondAndPolicy
    }

    // Phone hint and phone
    override fun setPhoneHint(textView: TextView, phone: String?) {
        val phoneHint = SpannableString(
            "Введите 6-значный код, который был\n" +
                    "отправлен на номер $phone"
        )
        phoneHint.setSpan(
            ForegroundColorSpan(Color.BLACK), phoneHint.length - 12, phoneHint.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        textView.text = phoneHint
    }

    //    Login Footer
    override fun setRegisterLoginFooter(textView: TextView, action: NavDirections) {
//       Уже есть аккуунт? Войдите
        val register = SpannableString(getString(ru.prsolution.winstrike.R.string.fmt_register_message_enter))
        val registerClick = object : ClickableSpan() {
            override fun onClick(v: View) {
//                val action = RegisterFragmentDirections.actionToNavigationLogin()
                navigate(action)
            }
        }
        register.setSpan(registerClick, 18, register.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        textView.movementMethod = LinkMovementMethod.getInstance()
        textView.text = register
    }


    override fun onGooglePlayButtonClick() {
        val uri = Uri.parse("market://details?id=" + this.packageName)
        val goToMarket = Intent(Intent.ACTION_VIEW, uri)
        // To count with Play market backstack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        goToMarket.addFlags(
            Intent.FLAG_ACTIVITY_NO_HISTORY or
                    Intent.FLAG_ACTIVITY_NEW_DOCUMENT or
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK
        )
        try {
            startActivity(goToMarket)
        } catch (e: ActivityNotFoundException) {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + this.packageName)
                )
            )
        }

    }

    fun hideKeyboard() {
        val imm: InputMethodManager = this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        //Find the currently focused view, so we can grab the correct window token from it.
        var view = this.currentFocus
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = View(this)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun  getVisibleFragment(): Fragment? {
    val fragmentManager = supportFragmentManager
    val fragments = fragmentManager.fragments
    if(fragments != null){
        for(fragment in fragments){
            if(fragment != null && fragment.isVisible)
                return fragment
        }
    }
    return null
}

}
