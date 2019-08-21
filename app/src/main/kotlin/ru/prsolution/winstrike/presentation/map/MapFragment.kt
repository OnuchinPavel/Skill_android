package ru.prsolution.winstrike.presentation.map

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Point
import android.graphics.Typeface
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.VectorDrawable
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.frm_map.*
import org.jetbrains.anko.support.v4.toast
import org.koin.androidx.viewmodel.ext.viewModel
import ru.prsolution.winstrike.R
import ru.prsolution.winstrike.data.repository.resouces.ResourceState
import ru.prsolution.winstrike.presentation.utils.MapViewUtils
import ru.prsolution.winstrike.presentation.utils.Utils
import ru.prsolution.winstrike.domain.models.arena.ArenaMap
import ru.prsolution.winstrike.domain.models.arena.ArenaSchemaName
import ru.prsolution.winstrike.domain.models.arena.SeatMap
import ru.prsolution.winstrike.domain.models.arena.SeatType
import ru.prsolution.winstrike.presentation.model.payment.PaymentResponseItem
import ru.prsolution.winstrike.presentation.utils.Constants
import ru.prsolution.winstrike.presentation.utils.date.TimeDataModel
import ru.prsolution.winstrike.domain.models.payment.PaymentModel
import ru.prsolution.winstrike.domain.models.payment.mapToPresentation
import ru.prsolution.winstrike.domain.models.payment.setPlacesPid
import ru.prsolution.winstrike.presentation.NavigationListener
import ru.prsolution.winstrike.presentation.model.arena.SchemaItem
import ru.prsolution.winstrike.presentation.model.arena.mapToDomain
import ru.prsolution.winstrike.presentation.setup.ArenaStorage
import ru.prsolution.winstrike.presentation.utils.pref.PrefUtils
import ru.prsolution.winstrike.viewmodel.MapViewModel
import ru.prsolution.winstrike.viewmodel.SetUpViewModel
import timber.log.Timber
import java.util.LinkedHashMap

/**
 * Created by oleg 24.01.2019
 */
class MapFragment : Fragment() {
    private val mVm: MapViewModel by viewModel()
    private val mSetUpVm: SetUpViewModel by viewModel()
    var mSelectedSeatList = mutableListOf<ImageView>()
    var mSelectedSeatNumberList = mutableMapOf<String, String>()
    var mSeatList = mutableListOf<SeatMap>()
    var mSeatNumberList = mutableListOf<TextView>()
    private val selectedSeats = mutableListOf<SelectSeat>()

    private lateinit var mBookingDialog: AlertDialog

    var mapLayout: RelativeLayout? = null
    private var snackbar: Snackbar? = null
    private var snackLayout: Snackbar.SnackbarLayout? = null

    private val RLW = RelativeLayout.LayoutParams.WRAP_CONTENT
    private var numberParams: RelativeLayout.LayoutParams? = null
    private var seatParams: RelativeLayout.LayoutParams? = null
  //  private var rootLayoutParams: RelativeLayout.LayoutParams? = null
    private val mPickedSeatsIds = LinkedHashMap<Int, String>()

    val height: Float = PrefUtils.displayHeightPx
    val width: Float = PrefUtils.displayWidhtPx
    var mXScaleFactor: Float? = null
    var mYScaleFactor: Float? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.frm_map, container, false)
    }

    private var mContext: Context? = null


    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.mContext = context
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mapLayout = view.findViewById(R.id.rootMap)
        initSnackBar()


        //TODO: use anather solutuion instead parcebale
        arguments?.let {
            val safeArgs = MapFragmentArgs.fromBundle(it)
            val mActiveLayoutPid = safeArgs.acitveLayoutPID

            val time = mutableMapOf<String, String>()
            time["start_at"] = TimeDataModel.start
            time["end_at"] = TimeDataModel.end
         //   mSetUpVm.fetchSchema(mActiveLayoutPid, time)
            initMap(ArenaStorage.shema)

        }

        if (isAdded) {
            Timber.d("fragment is added")
        } else {
            Timber.d("fragment is not added")
        }

//        mSetUpVm.arenaSchema.observe(this@MapFragment, Observer {
//            it?.let {
//                initMap(it)
//            }
//        })
//
//        mSetUpVm.error.observe( this@MapFragment, Observer { e ->
//            Toast.makeText(activity, e, Toast.LENGTH_SHORT).show()
//            }
//        )


        // payment response from map fragment:
        mVm.paymentResponse.observe(this@MapFragment, Observer {
            if (it.state == ResourceState.SUCCESS){
                it.data?.let {
                    mBookingDialog.dismiss()
                    onPaymentShow(it.mapToPresentation())
                    TimeDataModel.pids.clear()
                    mSelectedSeatNumberList.clear()
                }
            } else {
                it.message?.let {
                    onGetPaymentFailure(it)
                    TimeDataModel.pids.clear()
                }
            }

        })

    }

    private fun initMap(schema: SchemaItem?) {
        requireNotNull(schema) { "++++ RoomLayoutFactory must be initView. ++++" }
        requireNotNull(mapLayout) { "++++ Map Fragment root layout must not be null. ++++" }

    //    rootLayoutParams = RelativeLayout.LayoutParams(RLW, RLW)

        drawSeat(ArenaMap(schema.mapToDomain()))
    }


    // TODO: Move this code in UseCases
    private fun drawSeat(arenaMap: ArenaMap?) {

        val schema = arenaMap?.arenaSchema

        calculateMapSize(schema)

        val seatSize = Point()

        val seatBitmap = getBitmap(mContext, R.drawable.ic_seat_gray)

        seatSize.set(seatBitmap.width, seatBitmap.height)

        val mScreenSize = MapViewUtils.calculateScreenSize(
            seatSize, arenaMap?.seats, mXScaleFactor!! + 0.2f,
            mYScaleFactor!! - 1.5f
        )


        // Calculate  width and height for different Android screen sizes

        calculateMapLayout(mScreenSize, schema)

        // Draw seats and numbers

        for (seat in arenaMap?.seats!!) {

            val numberTextView = TextView(mContext)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                numberTextView.setTextAppearance(R.style.StemRegular10Gray)
            } else {
                numberTextView.setTextAppearance(mContext, R.style.StemRegular10Gray)
            }

            setNumber(seat, numberTextView)

            // Draw one seat
            // TODO: make it custom view
            ImageView(mContext).apply {

                setImage(this, seat)

                setSeat(seatBitmap, seat, this)

                this.layoutParams = seatParams

                // On seat click mListener
                this.setOnClickListener(
                    SeatViewOnClickListener(
                        numberTextView, seat, this, seatBitmap,
                        mPickedSeatsIds
                    )
                )

                mapLayout?.addView(this)
            }
        }

        // Draw labels for halls

        for (label in arenaMap.labels) {
            val text = label.text
            var offsetY = -5

            if (schema == ArenaSchemaName.WINSTRIKE) {
                offsetY = 5
            }

            val dx: Int? = ((label.x?.minus(0))?.times(mXScaleFactor!!))?.toInt()
            val dy: Int? = ((label.y?.plus(offsetY))?.times(mYScaleFactor!!))?.toInt()

            numberParams = RelativeLayout.LayoutParams(RLW, RLW)
            numberParams?.leftMargin = dx!!
            numberParams?.topMargin = dy!!

            if (height <= Constants.SCREEN_HEIGHT_PX_1280) {
                numberParams?.topMargin = dy - 5
            }

            val textView = TextView(mContext)
            textView.text = text
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                textView.setTextAppearance(R.style.StemMedium15Accent)
            } else {
                textView.setTextAppearance(mContext, R.style.StemMedium15Accent)
            }

            textView.layoutParams = numberParams
            mapLayout?.addView(textView)
        }
    }

    private fun calculateMapLayout(
        mScreenSize: Point,
        schema: ArenaSchemaName?
    ) {
        progressBar.visibility = View.GONE
        val mapLP = mapLayout?.layoutParams as FrameLayout.LayoutParams
        mapLP.setMargins(dpToPx(16F), dpToPx(0F), dpToPx(16F), dpToPx(16F))
      //  mapLP.setMargins(-65, -80, 100, 80)

        mapLP.width = FrameLayout.LayoutParams.WRAP_CONTENT
        mapLP.height = FrameLayout.LayoutParams.WRAP_CONTENT
        when {

            height <= Constants.SCREEN_HEIGHT_PX_1280 -> {
//                mapLP.width = mScreenSize.x
//                mapLP.height = mScreenSize.y + 250
                mYScaleFactor = mYScaleFactor!! - 1.5f
            }

            height <= Constants.SCREEN_HEIGHT_PX_1920 -> {
//                mapLP.setMargins(-35, -80, 100, 80)
//                mapLP.width = mScreenSize.x
//                mapLP.height = mScreenSize.y + 380
                mYScaleFactor = mYScaleFactor!! - 2.0f
            }

            height <= Constants.SCREEN_HEIGHT_PX_2560 -> { // Samsung GX-7
//
//                mapLP.width = mScreenSize.x
//                mapLP.height = mScreenSize.y + 150
                mYScaleFactor = mYScaleFactor!! - 3f

                if (schema == ArenaSchemaName.WINSTRIKE) {
//                    mapLP.height = mScreenSize.y + 850
//                    mapLP.width = mScreenSize.x + 500
                    mYScaleFactor = mYScaleFactor!! - 0f
                    mXScaleFactor = mXScaleFactor!! - 0.2f
                   // mapLP.setMargins(0, -250, 0, 80)
                }
            }

            else -> {
//                mapLP.width = FrameLayout.LayoutParams.WRAP_CONTENT
//                mapLP.height = FrameLayout.LayoutParams.WRAP_CONTENT
                mYScaleFactor = mYScaleFactor!!  - 1.5f
            }
        }

        mapLayout?.layoutParams = mapLP
    }

    private fun calculateMapSize(schema: ArenaSchemaName?) {
        mXScaleFactor = width / 358
        mYScaleFactor = height / 421

        when (schema) {
            ArenaSchemaName.WINSTRIKE -> {
                mXScaleFactor = (width / 358) * 1
                mYScaleFactor = (height / 421) * 1.5f
            }
            else -> {
                mXScaleFactor = width / 358
                mYScaleFactor = height / 421
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Clear listeners to prevent memory leak
        snackLayout?.setOnClickListener(null)

        for (i in 0 until mapLayout?.childCount!!) {
            val v = mapLayout?.getChildAt(i)
            if (v is ImageView) {
                v.setOnClickListener(null)
            }
        }
    }

    /**
     * Save selected seat's pids in db. For offline mode compatibility.
     *
     * @param id - seat id
     * @param unselect - is seat selected
     * @param publicPid - pid of selected seat
     */

    private fun onSelectSeat(id: String, unselect: Boolean, publicPid: String) {

        if (!unselect) {
            mPickedSeatsIds[Integer.parseInt(id)] = publicPid
        } else {
            mPickedSeatsIds.remove(Integer.parseInt(id))
            mSelectedSeatNumberList.remove(id)
        }

        TimeDataModel.pids = mPickedSeatsIds

        onPickedSeatChanged()
    }

    private fun onPickedSeatChanged() {
        if (!mPickedSeatsIds.isEmpty()) {
            snackbar?.show()
        } else {
            snackbar?.dismiss()
        }
    }

    private fun animateView(seatView: ImageView) {
        val animation1 = AlphaAnimation(0.5f, 1.0f)
        animation1.duration = 100
        animation1.startOffset = 300
        animation1.fillAfter = true
        seatView.startAnimation(animation1)
    }

    private fun setNumber(seat: SeatMap, numberTextView: TextView) {
        numberParams = RelativeLayout.LayoutParams(RLW, RLW)
        val seatNumber = Utils.parseNumber(seat.name)
        numberTextView.text = seatNumber

        val angle = radianToDegrees(seat)
        val angleInt = Math.round(angle)
        val angleAbs = Math.abs(angleInt)

        val offsetX: Int

        if (angleAbs != 90) { // horizontal seats
            offsetX = 0
        } else { // vertical seats
            if (seatNumber.length < 2) {
                offsetX = 5
            } else {
                offsetX = 4
            }
        }

        numberParams?.leftMargin = ((seat.numberDeltaX?.plus(offsetX))?.times(
            mXScaleFactor!!
        ))?.toInt()
        numberParams?.topMargin = ((seat.numberDeltaY?.plus(0))?.times(
            mYScaleFactor!!
        ))?.toInt()

        numberTextView.layoutParams = numberParams
        mapLayout?.addView(numberTextView)
    }

    private fun setSeat(seatBitmap: Bitmap, seat: SeatMap, ivSeat: ImageView) {
        seatParams = RelativeLayout.LayoutParams(RLW, RLW)
        seatParams?.leftMargin = ((seat.dx.minus(0)) * mXScaleFactor!!).toInt()
        seatParams?.topMargin = ((seat.dy.plus(0)) * mYScaleFactor!!).toInt()

        val angle = radianToDegrees(seat)
// 		Timber.tag("@@@").d("name: ${seat.name} -  angle: $angle")

        val pivotX = seatBitmap.width / 2f
        val pivotY = seatBitmap.height / 2f
        ivSeat.pivotX = pivotX
        ivSeat.pivotY = pivotY
        ivSeat.rotation = angle
    }

    private fun radianToDegrees(seat: SeatMap): Float {
        return Math.toDegrees(seat.angle).toFloat()
    }

    private fun setImage(seatImg: ImageView, seat: SeatMap) {
        when (seat.type) {
            SeatType.FREE -> {
                seatImg.setBackgroundResource(R.drawable.ic_seat_gray)
            }
            SeatType.HIDDEN -> {
                seatImg.setBackgroundResource(R.drawable.ic_seat_darkgray)
            }
            SeatType.SELF_BOOKING -> {
                seatImg.setBackgroundResource(R.drawable.ic_seat_blue)
            }
            SeatType.BOOKING -> {
                seatImg.setBackgroundResource(R.drawable.ic_seat_red)
            }
            SeatType.VIP -> {
                seatImg.setBackgroundResource(R.drawable.ic_seat_yellow)
            }
        }
    }

    private inner class BookingBtnListener : View.OnClickListener {

        override fun onClick(view: View) {
            val timeFrom: String = TimeDataModel.start
            val timeTo: String = TimeDataModel.end
            if (Utils.validateDate(timeFrom, timeTo)) {
                onPaymentRequest()
            } else {
                activity?.resources?.getString(R.string.toast_wrong_range)?.let { toast(it) }
            }
        }
    }

    /**
     * Something goes wrong, and we can't bye seat from Winstrike PC club. show user toast with description this fucking situation.
     */
    fun onGetPaymentFailure(appErrorMessage: String) {
        val timeFrom = TimeDataModel.start
        val timeTo = TimeDataModel.end
        Timber.d("timeFrom: %s", timeFrom)
        Timber.d("timeTo: %s", timeTo)

        Timber.tag("common").w("Failure on pay: %s", appErrorMessage)
        when {
            appErrorMessage.contains(getString(R.string.msg_server_500)) -> toast(
                getString(R.string.msg_server_internal_err)
            )
            appErrorMessage.contains(getString(R.string.msg_server_400)) -> toast(getString(R.string.msg_no_data))
            appErrorMessage.contains(getString(R.string.msg_server_401)) -> toast(getString(R.string.msg_no_auth))
            appErrorMessage.contains(getString(R.string.msg_serve_403)) -> toast(getString(R.string.msg_auth_err))
            appErrorMessage.contains(getString(R.string.msg_server_404)) -> toast(
                getString(R.string.msg_no_seat_with_id)
            )
            appErrorMessage.contains(getString(R.string.msg_server_405)) -> toast(getString(R.string.msg_auth_error))
            appErrorMessage.contains(getString(R.string.msg_server_424)) -> toast(getString(R.string.msg_date_error))
            appErrorMessage.contains(getString(R.string.msg_server_416)) -> toast(getString(R.string.msg_booking_error))
            else -> toast(getString(R.string.msg_bookin_err))
        }
    }

    private fun getBitmap(vectorDrawable: VectorDrawable): Bitmap {
        val bitmap = Bitmap.createBitmap(
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        vectorDrawable.setBounds(0, 0, canvas.width, canvas.height)
        vectorDrawable.draw(canvas)
        return bitmap
    }

    private fun getBitmap(context: Context?, drawableId: Int?): Bitmap {
        val drawable = mContext?.getDrawable(drawableId!!)
        val bitmap: Bitmap

        when (drawable) {
            is BitmapDrawable -> return BitmapFactory.decodeResource(context?.resources, drawableId!!)
            is VectorDrawable -> bitmap = getBitmap((drawable as VectorDrawable?)!!)
            else -> throw IllegalArgumentException("unsupported drawable type") as Throwable
        }
        return bitmap
    }

    private inner class SeatViewOnClickListener(
        private val seatNumber: TextView,
        private val seat: SeatMap,
        private val ivSeat: ImageView,
        private val seatBitmap: Bitmap,
        private val mPickedSeatsIds: LinkedHashMap<*, *>
    ) : View.OnClickListener {

        override fun onClick(v: View) {
            if (seat.type === SeatType.FREE || seat.type === SeatType.VIP) {
                if (!mPickedSeatsIds.containsKey(Integer.parseInt(seat.id))) {
                    val numberSeat = Utils.parseNumber(seat.name)
                    mSelectedSeatNumberList[seat.id] = numberSeat
                    mSelectedSeatList.add(ivSeat)
                    mSeatList.add(seat)
                    selectedSeats.add(SelectSeat(ivSeat, seat))
                    mSeatNumberList.add(seatNumber)
                    ivSeat.setBackgroundResource(R.drawable.ic_seat_picked)
                    seat.pid?.let { onSelectSeat(seat.id, false, it) }
                    Timber.d("Seat id: %s,type: %s, name: %s, pid: %s", seat.id, seat.type, seat.name, seat.pid)
                    seatNumber.setTextColor(Color.WHITE)
                    seatNumber.setTypeface(null, Typeface.BOLD)
                } else {
                    mapLayout?.removeView(ivSeat)
                    setImage(ivSeat, seat)
                    setSeat(seatBitmap, seat, ivSeat)
                    mapLayout?.addView(ivSeat)
                    seat.pid?.let { onSelectSeat(seat.id, true, it) }
                    seatNumber.setTextColor(ContextCompat.getColor(activity!!, R.color.grey))
                }
            } else {
                Timber.d("Seat id: %s,type: %s, name: %s, pid: %s", seat.id, seat.type, seat.name, seat.pid)
                animateView(ivSeat)
                seatNumber.setTypeface(null, Typeface.NORMAL)
            }
        }
    }

    private fun initSnackBar() {
        snackbar = Snackbar.make(mapLayout!!, "", Snackbar.LENGTH_INDEFINITE)
        snackbar?.view?.setBackgroundColor(Color.TRANSPARENT)
        snackbar?.view?.setBackgroundResource(R.drawable.btn_bukking)
        val layoutInflater = this.layoutInflater
        val snackView = layoutInflater.inflate(R.layout.my_snackbar, null)
        snackLayout = snackbar?.view as Snackbar.SnackbarLayout
        snackLayout?.addView(snackView)

        snackLayout?.setOnClickListener(BookingBtnListener())
        snackbar?.dismiss()
    }

    override fun onStop() {
        super.onStop()
        snackbar?.dismiss()
    }

    override fun onStart() {
        super.onStart()
        snackbar?.dismiss()
    }

    // Open Yandex WebView on payment response from MapFragment
    private fun onPaymentShow(payResponse: PaymentResponseItem) {
        // TODO: Show progress bar when load web view.
//        val testUrl = "https://yandex.ru"
        val url = payResponse.redirectUrl
        val action = MapFragmentDirections.nextAction()
        action.url = url
        (activity as NavigationListener).navigate(action)
    }


    fun onPaymentRequest() {
        showBookingDialog()
        snackbar?.dismiss()
    }


    private fun showBookingDialog() {

        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dlg_booking, mapLayout, false)
        val builder = AlertDialog.Builder(requireContext())
        builder.setView(dialogView)
        mBookingDialog = builder.create()
        mBookingDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        mBookingDialog.show()


        val arenaTitle = SpannableString("Вы бронируете «${PrefUtils.arenaName}»")
        arenaTitle.setSpan(
            ForegroundColorSpan(resources.getColor(R.color.magenta)),
            13,
            arenaTitle.length,
            SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        val address = "(${PrefUtils.arenaAddress}, ${PrefUtils.arenaMetro})"


        val closeBtn = mBookingDialog.findViewById<View>(R.id.close_dlg)

        // On close button click - clear all selected seats
        closeBtn?.setOnClickListener {
            TimeDataModel.pids.clear()
            mSeatNumberList.forEach {
                it.setTextColor(ContextCompat.getColor(activity!!, R.color.grey))
            }
            selectedSeats.forEach {
                setImage(it.ivSeat, it.seat)
            }



            mSelectedSeatNumberList.clear()
            selectedSeats.clear()
            mapLayout?.invalidate()
            mBookingDialog.dismiss()
        }

        val hallName = if (PrefUtils.hallName?.contains("VIP")!!) {
            "VIP"
        } else {
            "Общий"
        }

        mBookingDialog.findViewById<TextView>(R.id.arena_tv)?.text = arenaTitle

        mBookingDialog.findViewById<TextView>(R.id.hall_tv)?.text = hallName

        mBookingDialog.findViewById<TextView>(R.id.address_tv)?.text = address

        val seatNumbers = mSelectedSeatNumberList.values.joinToString(", ")

        mBookingDialog.findViewById<TextView>(R.id.mesto_tv)?.text = seatNumbers

        mBookingDialog.findViewById<TextView>(R.id.date_tv)?.text = TimeDataModel.date

        mBookingDialog.findViewById<TextView>(R.id.time_tv)?.text =
            "c ${TimeDataModel.timeFrom} до ${TimeDataModel.timeTo}"

        val btnBooking = mBookingDialog.findViewById<View>(R.id.btn_v)

        val androidId = Settings.Secure.getString(context!!.contentResolver, Settings.Secure.ANDROID_ID)
        btnBooking?.setOnClickListener {
            // set up data for payment request (time star, time end and selected seat's pid)
            val payModel = PaymentModel(
                TimeDataModel.start,
                TimeDataModel.end,
                null,
                 androidId
            )

            payModel.setPlacesPid(TimeDataModel.pids)

            mVm.getPayment(payModel.mapToPresentation())
        }

    }

    private fun dpToPx(dp: Float): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, this.resources.displayMetrics).toInt()
    }



}
