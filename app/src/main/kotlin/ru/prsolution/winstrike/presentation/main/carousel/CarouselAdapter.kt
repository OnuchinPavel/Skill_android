package ru.prsolution.winstrike.presentation.main.carousel

import android.view.View

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import ru.prsolution.winstrike.R
import ru.prsolution.winstrike.presentation.utils.pref.PrefUtils

/**
 * Created by oleg on 02/04/2018.
 */

class CarouselAdapter(fm: FragmentManager?) : FragmentStatePagerAdapter(
    fm!!
), ViewPager.PageTransformer {

    private var root: CarouselLinearLayout? = null

    private val mFragmentList = mutableListOf<Fragment>()


    var dpHeight: Float = 0f
    var dpWidth: Float = 0f

    private var bigScale: Float = 0f
    private var smallScale: Float = 0f
    private var diffScale: Float = 0f

    init {

        this.mFragmentList.clear()

        dpHeight = PrefUtils.displayHeightDp
        dpWidth = PrefUtils.displayWidhtDp

        if (dpWidth <= 360.0) {
            bigScale = 0.9f
            smallScale = 0.5f
        } else {
            bigScale = 1.0f
            smallScale = 0.6f
        }

        diffScale = bigScale - smallScale

    }

    override fun getItem(position: Int): Fragment {
        return mFragmentList[position]
    }

    // fix bag with not visible carousel
    override fun getItemPosition(`object`: Any): Int {
        return PagerAdapter.POSITION_NONE
    }

    fun addFragment(fragment: Fragment) {
        mFragmentList.add(fragment)
    }

    override fun getCount(): Int {
        return mFragmentList.size
    }


    override fun transformPage(page: View, position: Float) {
         root = page.findViewById(R.id.root)
         var scale = bigScale

        if (position > 0) {
            scale -= position * diffScale
        } else {
            scale += position * diffScale
        }

        root?.setScaleBoth(scale)
    }

    fun isClear(): Boolean {
        mFragmentList.clear()
        return mFragmentList.isEmpty()
    }
}
