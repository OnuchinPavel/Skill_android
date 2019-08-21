package ru.prsolution.winstrike.presentation.main

import android.graphics.drawable.Animatable
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.facebook.drawee.controller.BaseControllerListener
import com.facebook.imagepipeline.image.ImageInfo
import kotlinx.android.synthetic.main.fmt_home.*

import org.koin.androidx.viewmodel.ext.viewModel
import ru.prsolution.winstrike.data.repository.resouces.ResourceState
import ru.prsolution.winstrike.domain.models.arena.ArenaHallType

import ru.prsolution.winstrike.domain.models.arena.SeatCarousel
import ru.prsolution.winstrike.presentation.utils.Constants.SCREEN_MARGIN_350
import ru.prsolution.winstrike.presentation.utils.Constants.SCREEN_MARGIN_450
import ru.prsolution.winstrike.presentation.utils.Constants.SCREEN_MARGIN_600
import ru.prsolution.winstrike.presentation.utils.Constants.SCREEN_WIDTH_PX_1080
import ru.prsolution.winstrike.presentation.utils.Constants.SCREEN_WIDTH_PX_1440
import ru.prsolution.winstrike.presentation.utils.Constants.SCREEN_WIDTH_PX_720
import ru.prsolution.winstrike.presentation.main.carousel.CarouselAdapter
import ru.prsolution.winstrike.presentation.main.carousel.CarouselFragment
import ru.prsolution.winstrike.presentation.model.arena.ArenaItem
import ru.prsolution.winstrike.presentation.utils.pref.PrefUtils
import ru.prsolution.winstrike.viewmodel.CityItemViewModel
import timber.log.Timber

import com.facebook.drawee.backends.pipeline.Fresco

import ru.prsolution.winstrike.R


/**
 * Created by Oleg Sitnikov on 2019-02-13
 */

class HomeFragment : Fragment() {

    private val mVm: CityItemViewModel by viewModel()

    private var mArenaPid: String? = ""
    private var mArena: ArenaItem? = null

    var mCarouselAdapter: CarouselAdapter? = null

    private val seatMap: MutableMap<String, SeatCarousel> = mutableMapOf()
    var hallType: ArenaHallType = ArenaHallType.COMMON

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mCarouselAdapter = CarouselAdapter(activity?.supportFragmentManager)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(ru.prsolution.winstrike.R.layout.fmt_home, container, false)
    }

    private var mArenaName: String? = "No ArenaStorage"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressHeadImage.visibility = View.VISIBLE
        arguments?.let {
            val safeArgs = HomeFragmentArgs.fromBundle(it)
            when (safeArgs.arenaPID) {
                "0" -> {
                    this.mArenaPid = PrefUtils.arenaPid
                }
                else -> {
                    mArenaPid = safeArgs.arenaPID
                    PrefUtils.arenaPid = mArenaPid
                }
            }

            when (safeArgs.title) {
                "No ArenaStorage" -> {
                    mArenaName = PrefUtils.arenaName
                }
                else -> {
                    mArenaName = safeArgs.title
                    PrefUtils.arenaName = mArenaName
                }
            }
        }

        if (savedInstanceState == null) {
            mVm.fetchArenaList()
        }

        mVm.arenaList.observe(this@HomeFragment, Observer {
            it.let { arenaList ->
                if (arenaList.state == ResourceState.SUCCESS){
                    mArenaPid?.let{arenaPid->
                        mArena = arenaList.data?.find { it.publicId!!.contains(arenaPid) }
                        //TODO: replace with preferences
                        (activity as MainActivity).mArenaActiveLayoutPid = mArena?.activeLayoutPid
                        updateArenaInfo(mArena)
                        updateCarouselView(mArena)
                    }
                } else {
                    Toast.makeText(activity, arenaList.message, Toast.LENGTH_SHORT).show()
                    Timber.tag("$$$").e("New version checked FAIL!")
                }
            }
        })
        setupViewPager()
    }

    override fun onResume() {
        super.onResume()
        val handler = Handler()
        handler.post {
            mCarouselAdapter?.notifyDataSetChanged()
        }
    }

    private fun updateArenaInfo(arena: ArenaItem?) {
        val listener = object : BaseControllerListener<ImageInfo>() {
            override fun onFinalImageSet(id: String?, @Nullable imageInfo: ImageInfo?, @Nullable animatable: Animatable?) {
                progressHeadImage.visibility = View.GONE
            }

            override fun onFailure(id: String?, throwable: Throwable?) {
                progressHeadImage.visibility = View.GONE
                Toast.makeText(activity, getString(R.string.error_loading_image), Toast.LENGTH_SHORT).show()
            }

        }
        PrefUtils.arenaMetro = arena?.trsMetro
        PrefUtils.arenaAddress = arena?.exactAddress

        metro_tv.text = arena?.trsMetro
        address_tv.text = arena?.exactAddress
        arena_description.text = arena?.description
     //   head_image.imageURI = Uri.parse(arena?.imageUrl)

        val controller = Fresco.newDraweeControllerBuilder()
            .setUri(Uri.parse(arena?.imageUrl))
            .setControllerListener(listener)
            .build()
        head_image.controller = controller


       // head_image.pro
    }

// Carousel view initiated

    private fun updateCarouselView(arenaItem: ArenaItem?) {
        mCarouselAdapter?.isClear()?.let {
            require(it) {
                "++++ Adapter must be empty! ++++"
            }
        }
        // Define mArena type ( double(common & vip), common, vip)

//        if (arenaItem?.halls?.isNotEmpty()!!) hallType = ArenaHallType.HALL
//        else {
//            if (
//                (!TextUtils.isEmpty(arenaItem?.commonDescription) && (!TextUtils.isEmpty(arenaItem?.vipDescription)))
//
//            ) {
//                hallType = ArenaHallType.DOUBLE
//            } else if (!TextUtils.isEmpty(arenaItem?.commonDescription)) {
//                hallType = ArenaHallType.COMMON
//            } else if (!TextUtils.isEmpty(arenaItem?.vipDescription)) {
//                hallType = ArenaHallType.VIP
//            }
//        }


        if (arenaItem?.halls ==null){
            if (
                (!TextUtils.isEmpty(arenaItem?.commonDescription) && (!TextUtils.isEmpty(arenaItem?.vipDescription)))

            ) {
                hallType = ArenaHallType.DOUBLE
            } else if (!TextUtils.isEmpty(arenaItem?.commonDescription)) {
                hallType = ArenaHallType.COMMON
            } else if (!TextUtils.isEmpty(arenaItem?.vipDescription)) {
                hallType = ArenaHallType.VIP
            }
        } else {
            if (arenaItem?.halls.isNotEmpty()!!) hallType = ArenaHallType.HALL
        }


        if (hallType != ArenaHallType.HALL) {
            when (hallType) {
                ArenaHallType.DOUBLE -> { // create two mArena: COMMON and VIP

                    seatMap["COMMON"] =
                        SeatCarousel(
                            type = "COMMON",
                            imageUrl = arenaItem?.commonImageUrl,
                            description = arenaItem?.commonDescription,
                            hallName = null,
                            hallPublicId = null
                        )

                    seatMap["VIP"] =
                        SeatCarousel(
                            type = "VIP",
                            imageUrl = arenaItem?.vipImageUrl,
                            description = arenaItem?.vipDescription,
                            hallName = null,
                            hallPublicId = null
                        )
                }
                ArenaHallType.COMMON -> {
                    seatMap["COMMON"] =
                        SeatCarousel(
                            type = "COMMON",
                            imageUrl = arenaItem?.commonImageUrl,
                            description = arenaItem?.commonDescription,
                            hallName = null,
                            hallPublicId = null
                        )
                }
                ArenaHallType.VIP -> { // Create VIP mArena

                    seatMap["VIP"] =
                        SeatCarousel(
                            type = "VIP",
                            imageUrl = arenaItem?.vipImageUrl,
                            description = arenaItem?.vipDescription,
                            hallName = null,
                            hallPublicId = null
                        )
                }


            }
        } else {
            arenaItem?.halls?.forEach {
                seatMap[it.name] =
                    SeatCarousel(
                        type = it.name,
                        imageUrl = it.imageUrl,
                        description =  it.description,
                        hallName = it.name,
                        hallPublicId = it.publicId
                    )
            }
        }


        with(mCarouselAdapter) {
            seatMap.forEach {
                CarouselFragment.newInstance(
                    activity?.supportFragmentManager,
                    it.value
                )?.let { this?.addFragment(fragment = it) }
            }
        }

        view_pager_seat.adapter = mCarouselAdapter
        mCarouselAdapter?.notifyDataSetChanged()
    }

    private fun setupViewPager() {
        val widthPx = PrefUtils.displayWidhtPx
        with(view_pager_seat) {
            adapter = mCarouselAdapter
            currentItem = 0
            offscreenPageLimit = 2
            setPageTransformer(false, mCarouselAdapter)
            pageMargin = when {
                widthPx <= SCREEN_WIDTH_PX_720 -> SCREEN_MARGIN_350
                widthPx <= SCREEN_WIDTH_PX_1080 -> SCREEN_MARGIN_450
                widthPx <= SCREEN_WIDTH_PX_1440 -> SCREEN_MARGIN_600
                else -> SCREEN_MARGIN_450
            }
        }
    }





}
