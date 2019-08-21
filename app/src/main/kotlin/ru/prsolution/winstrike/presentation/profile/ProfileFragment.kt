package ru.prsolution.winstrike.presentation.profile

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import ru.prsolution.winstrike.R
import ru.prsolution.winstrike.presentation.main.ToolbarTitleListener
import ru.prsolution.winstrike.presentation.utils.pref.PrefUtils
import timber.log.Timber

class ProfileFragment : Fragment() {

    private var mDlgSingOut: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dlgProfileSingOut()
    }

    private lateinit var adapter: TabAdapter

    private var tabLayout: TabLayout? = null
    private var vp: ViewPager? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.frm_profile, container, false)

         tabLayout = view.findViewById(R.id.tabs)
         vp = view.findViewById(R.id.viewPager)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!PrefUtils.name?.isEmpty()!!) {
            (activity as ToolbarTitleListener).updateTitle(PrefUtils.name!!)
        }


        initAdapter()
    }

    private fun initAdapter() {
        Timber.tag("$$$").d("Update profile adapter.")
        adapter = TabAdapter(fragmentManager)
        adapter.addFragments(ProfileTabFragment(), "Профиль")
        adapter.addFragments(AppTabFragment(), "Приложение")
        (vp as ViewPager).adapter = adapter
        (tabLayout as TabLayout).setupWithViewPager(vp)
    }

    override fun onResume() {
        super.onResume()
//        initAdapter()
        val handler = Handler()
        handler.post {
            adapter.notifyDataSetChanged()
        }
    }

    // SignOut Dialog
    private fun dlgProfileSingOut() {
        mDlgSingOut = Dialog(activity, android.R.style.Theme_Dialog)
        mDlgSingOut!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        mDlgSingOut!!.setContentView(R.layout.dlg_logout)

        val cvBtnOk = mDlgSingOut!!.findViewById<TextView>(R.id.btn_ok)
        val cvCancel = mDlgSingOut!!.findViewById<TextView>(R.id.btn_cancel)

        cvCancel.setOnClickListener { mDlgSingOut!!.dismiss() }

        cvBtnOk.setOnClickListener {
            PrefUtils.isLogout = true
            PrefUtils.token = ""
//            startActivity(Intent(activity, StartActivity::class.java))
        }

        mDlgSingOut!!.setCanceledOnTouchOutside(true)
        mDlgSingOut!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        mDlgSingOut!!.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        val window = mDlgSingOut!!.window
        val wlp = window!!.attributes

        wlp.gravity = Gravity.CENTER
        wlp.flags = wlp.flags and WindowManager.LayoutParams.FLAG_DIM_BEHIND.inv()
        window.attributes = wlp

        mDlgSingOut!!.window!!.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        mDlgSingOut!!.window!!.setDimAmount(0.5f)

        mDlgSingOut!!.setCanceledOnTouchOutside(false)
        mDlgSingOut!!.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        mDlgSingOut!!.dismiss()
    }

}
