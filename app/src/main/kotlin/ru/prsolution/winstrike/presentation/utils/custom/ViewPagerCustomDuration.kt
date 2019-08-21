package ru.prsolution.winstrike.presentation.utils.custom

/*
 * Created by oleg on 31.01.2018.
 */

import android.content.Context
import android.util.AttributeSet
import android.view.animation.Interpolator

import androidx.viewpager.widget.ViewPager

class ViewPagerCustomDuration : ViewPager {

    private var mScroller: ScrollerCustomDuration? = null

    constructor(context: Context) : super(context) {
        postInitViewPager()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        postInitViewPager()
    }

    /**
	 * Override the Scroller instance with our own class so we can change the
	 * duration
	 */
    private fun postInitViewPager() {
        try {
            val scroller = ViewPager::class.java.getDeclaredField("mScroller")
            scroller.isAccessible = true
            val interpolator = ViewPager::class.java.getDeclaredField("sInterpolator")
            interpolator.isAccessible = true

            mScroller = ScrollerCustomDuration(context,
                                                                                                 interpolator.get(
                                                                                                         null) as Interpolator)
            scroller.set(this, mScroller)
        } catch (e: Exception) {
        }
    }

    /**
	 * Set the factor by which the duration will change
	 */
    fun setScrollDurationFactor(scrollFactor: Double) {
        mScroller!!.setScrollDurationFactor(scrollFactor)
    }
}
