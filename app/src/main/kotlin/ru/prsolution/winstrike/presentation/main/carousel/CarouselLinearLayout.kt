package ru.prsolution.winstrike.presentation.main.carousel

/*
 * Created by oleg on 01.02.2018.
 */

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.widget.LinearLayout

class CarouselLinearLayout : LinearLayout {
    //    private Float scale = CarouselSeatAdapter.BIG_SCALE;
    private var scale: Float = 0f

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    fun setScaleBoth(scale: Float) {
        this.scale = scale
        this.invalidate() // If you want to see the scale every time you set
        // scale you need to have this line here,
        // invalidate() function will call onDraw(Canvas)
        // to redraw the view for you
    }

    override fun onDraw(canvas: Canvas) {
        val w = this.width
        val h = this.height
        canvas.scale(scale, scale, (w / 2).toFloat(), (h / 2).toFloat())
        super.onDraw(canvas)
    }
}
