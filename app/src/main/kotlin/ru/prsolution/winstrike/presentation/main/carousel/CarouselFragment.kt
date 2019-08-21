package ru.prsolution.winstrike.presentation.main.carousel

/*
 * Created by oleg on 01.02.2018.
 */

import android.content.Context
import android.graphics.drawable.Animatable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.controller.BaseControllerListener
import com.facebook.drawee.view.SimpleDraweeView
import com.facebook.imagepipeline.image.ImageInfo
import kotlinx.android.synthetic.main.item_carousel.*
import ru.prsolution.winstrike.R
import ru.prsolution.winstrike.domain.models.arena.SeatCarousel

class CarouselFragment : Fragment() {
    lateinit var mListener: OnSeatClickListener
    private var mSeat: SeatCarousel? = null

    interface OnSeatClickListener {

        fun onCarouselClick(seat: SeatCarousel?)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnSeatClickListener) {
            mListener = context
        } else {
            throw ClassCastException(
                "$context must implements CarouselFragment.OnSeatClickListener "
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            mSeat = arguments?.getParcelable("seat") as SeatCarousel

            when  (mSeat!!.type){
                "COMMON" -> mSeat!!.title = "Вы выбрали: Общий зал"
                "VIP" -> mSeat!!.title = "Вы выбрали: VIP"
                else ->  mSeat!!.title = "Вы выбрали ${ mSeat!!.hallName!!}"
            }


        } catch (e: Exception) {
            mSeat = null
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.item_carousel, container, false)

        val seatTitle: TextView = view!!.findViewById(R.id.seat_name_tv)

        val seatImage: SimpleDraweeView = view.findViewById(R.id.seat_image_iv)

        seatImage.setOnClickListener {
            mListener.onCarouselClick(mSeat)
        }

        updateSeat(seatImage, seatTitle)

        return view
    }

    private fun updateSeat(seatImage: SimpleDraweeView, seatTitle: TextView) {
        val listener = object : BaseControllerListener<ImageInfo>() {
            override fun onFinalImageSet(id: String?, imageInfo: ImageInfo?,  animatable: Animatable?) {
                progress.visibility = View.GONE
            }

            override fun onFailure(id: String?, throwable: Throwable?) {
                progress.visibility = View.GONE
                Toast.makeText(activity, getString(R.string.error_loading_image), Toast.LENGTH_SHORT).show()
            }

        }

        val controller = Fresco.newDraweeControllerBuilder()
            .setUri(Uri.parse(mSeat?.imageUrl))
            .setControllerListener(listener)
            .build()
        seatImage.controller = controller

//        if (mSeat?.type == "COMMON") {
//            seatTitle.text = getString(R.string.common_hall)
//        } else {
//            seatTitle.text = getString(R.string.vip_hall)
//        }

        when (mSeat?.type) {
            "COMMON" -> seatTitle.text = getString(R.string.common_hall)
            "VIP" -> seatTitle.text = getString(R.string.vip_hall)
            else -> seatTitle.text = mSeat?.type
        }
    }

    companion object {

        fun newInstance(fm: FragmentManager?, seat: SeatCarousel?): Fragment? {
            val bundle = Bundle()
            bundle.putParcelable("seat", seat)


            return fm?.fragmentFactory?.instantiate(
                ClassLoader.getSystemClassLoader(),
                CarouselFragment::class.java.name,
                bundle
            )
        }
    }
}
