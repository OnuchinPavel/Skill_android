package ru.prsolution.winstrike.presentation.guides

/*
 * Created by oleg on 31.01.2018.
 */

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.prsolution.winstrike.R

class GuideFragment : Fragment() {
    private var guideNumber: Int? = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        guideNumber = arguments!!.getInt(
                ARGUMENT_GUIDE_NUMBER)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val viewPre1 = inflater.inflate(R.layout.fmt_pres1, null)
        val btn_nextGuide1 = viewPre1.findViewById<View>(R.id.btn_nextGuide)
        val fl_next1 = viewPre1.findViewById<View>(R.id.fl_next)

        val viewPre2 = inflater.inflate(R.layout.fmt_pres2, null)
        val btn_nextGuide2 = viewPre2.findViewById<View>(R.id.btn_nextGuide1)
        val fl_prev2 = viewPre2.findViewById<View>(R.id.fl_prev)
        val fl_next2 = viewPre2.findViewById<View>(R.id.fl_next)

        val viewPre3 = inflater.inflate(R.layout.fmt_pres3, null)
        val btn_nextGuide3 = viewPre3.findViewById<View>(R.id.btn_nextGuide1)
        val fl_prev3 = viewPre3.findViewById<View>(R.id.fl_prev)
        val nextButton = viewPre3.findViewById<View>(R.id.next_button)
//        nextButton.setOnClickListener { it -> startActivity(Intent(activity, LoginActivity::class.java)) }

        when (guideNumber) {
            0 -> {
                btn_nextGuide1.setOnClickListener { it -> (activity as GuideActivity).getViewPager()!!.currentItem = 1 }
                fl_next1.setOnClickListener { it -> (activity as GuideActivity).getViewPager()!!.currentItem = 1 }
                return viewPre1
            }
            1 -> {
                fl_prev2.setOnClickListener { it -> (activity as GuideActivity).getViewPager()!!.currentItem = 0 }
                fl_next2.setOnClickListener { it -> (activity as GuideActivity).getViewPager()!!.currentItem = 2 }
                btn_nextGuide2.setOnClickListener { it -> (activity as GuideActivity).getViewPager()!!.currentItem = 2 }

                return viewPre2
            }
            2 -> {
                btn_nextGuide3.setOnClickListener { it -> (activity as GuideActivity).getViewPager()!!.currentItem = 2 }
                fl_prev3.setOnClickListener { it -> (activity as GuideActivity).getViewPager()!!.currentItem = 1 }

                return viewPre3
            }
        }
        return null
    }

    companion object {
        private val ARGUMENT_GUIDE_NUMBER = "arg_page_number"

        fun newInstance(pageNumber: Int?): GuideFragment {
            val fragment = GuideFragment()
            val argument = Bundle()
            argument.putInt(ARGUMENT_GUIDE_NUMBER, pageNumber!!)
            fragment.arguments = argument
            return fragment
        }
    }
}
