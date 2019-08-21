package ru.prsolution.winstrike.presentation.utils.custom

/*
 * Created by oleg on 31.01.2018.
 */

import android.annotation.SuppressLint
import android.content.Context
import android.view.animation.Interpolator
import android.widget.Scroller

class ScrollerCustomDuration : Scroller {
    private var mScrollFactor = 1.0

    constructor(context: Context) : super(context) {}

    constructor(context: Context, interpolator: Interpolator) : super(context, interpolator) {}

    @SuppressLint("NewApi")
    constructor(context: Context, interpolator: Interpolator, flywheel: Boolean) : super(context, interpolator,
                                                                                         flywheel) {
    }

    /**
	 * Set the factor by which the duration will change
	 */
    fun setScrollDurationFactor(scrollFactor: Double) {
        mScrollFactor = scrollFactor
    }

    override fun startScroll(startX: Int, startY: Int, dx: Int, dy: Int, duration: Int) {
        super.startScroll(startX, startY, dx, dy, (duration * mScrollFactor).toInt())
    }
}
