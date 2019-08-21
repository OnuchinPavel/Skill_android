package ru.prsolution.winstrike.presentation.setup

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.Animatable
import java.text.DateFormatSymbols
import java.text.SimpleDateFormat
import java.util.Calendar
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.controller.BaseControllerListener
import com.facebook.drawee.view.SimpleDraweeView
import com.facebook.imagepipeline.image.ImageInfo
import com.google.android.material.button.MaterialButton
import com.ositnikov.datepicker.TimePickerPopWin
import kotlinx.android.synthetic.main.frm_setup.*
import org.jetbrains.anko.support.v4.longToast
import org.jetbrains.anko.support.v4.toast
import org.koin.androidx.viewmodel.ext.viewModel
import ru.prsolution.winstrike.R
import ru.prsolution.winstrike.domain.models.arena.SeatCarousel
import ru.prsolution.winstrike.presentation.model.arena.ScheduleItem
import ru.prsolution.winstrike.presentation.model.arena.SchemaItem
import ru.prsolution.winstrike.presentation.utils.date.TimeDataModel
import ru.prsolution.winstrike.presentation.utils.pref.PrefUtils
import ru.prsolution.winstrike.viewmodel.SetUpViewModel
import timber.log.Timber
import java.util.*

class SetupFragment : Fragment(),
    DatePickerDialog.OnDateSetListener {

    private val mVm: SetUpViewModel by viewModel()

    private var mArenaActivePid: String? = null
    private var mSeatName: TextView? = null
    private var mCpuDescription: TextView? = null
    private var mSeatImage: SimpleDraweeView? = null
    private var mArenaSchema: SchemaItem? = null
    private var mArenaSchedule: List<ScheduleItem>? = null
    private var mDayOfWeek: String? = null
    private var seat: SeatCarousel? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(ru.prsolution.winstrike.R.layout.frm_setup, container, false)
    }

    private var pickerDialog: TimePickerPopWin? = null


    private var vDateTap: View? = null
    private var tvDate: TextView? = null
    private var vTimeTap: View? = null
    private var tvTime: TextView? = null
    private var nextButton: MaterialButton? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progressHeadImage.visibility = View.VISIBLE

        vDateTap = view.findViewById(R.id.v_date_tap)
        tvDate = view.findViewById(R.id.tv_date)
        tvTime = view.findViewById(R.id.tv_time)
        vTimeTap = view.findViewById(R.id.v_time_tap)

        nextButton = view.findViewById(R.id.next_button)

        mSeatName = view.findViewById(R.id.seat_name_tv)
        mCpuDescription = view.findViewById(R.id.cpu)
        mSeatImage = view.findViewById(R.id.head_image)

        if (!TimeDataModel.date.isEmpty()) {
            tvDate?.text = TimeDataModel.date
        } else {
            tvDate?.text = getString(R.string.seatdetail_date)
        }

        if (!TimeDataModel.timeFrom.isEmpty()) {
            tvTime?.text = "${TimeDataModel.timeFrom} : ${TimeDataModel.timeTo}"
        } else {
            tvTime?.text = getString(R.string.seatdetail_time)
        }

        progressBar.visibility = View.INVISIBLE

        arguments?.let {
            val safeArgs = SetupFragmentArgs.fromBundle(it)
            this.mArenaActivePid = safeArgs.activeLayoutPid
            seat = safeArgs.seat
            updateSeatInfo(safeArgs.seat)
        }

        // Get arena schedulers
        mVm.getSchedule()
        mVm.schedulers.observe(this@SetupFragment, Observer {
            // data
            it?.let {
                mArenaSchedule = it.filter { it.roomPid == PrefUtils.arenaPid.toString() }
            }

        })

        mVm.error.observe( this@SetupFragment, Observer { e ->
            Toast.makeText(activity, e, Toast.LENGTH_SHORT).show() }
        )


        // mArenaPid
        mVm.arenaSchema.observe(this@SetupFragment, Observer { schema ->
            // data
            schema?.let {
                this.mArenaSchema = schema
                mArenaSchema?.let {
                    require(isAdded) { "+++ SetupFragment not attached to an activity. +++" }
                    ArenaStorage.shema = schema
                    val action = SetupFragmentDirections.nextAction()
                    action.acitveLayoutPID = mArenaActivePid
                    Navigation.findNavController(requireActivity(), R.id.main_host_fragment).navigate(action)
                }
            }

        })

        initListeners()

        pickerDialog = TimePickerPopWin.Builder(
            requireContext(), (TimePickerPopWin.OnTimePickedListener { hour, min, timeDesc, timeFromData, timeToData ->
                //                textView.text = "$timeFromData - $timeToData"
                Timber.tag("$$$").d("$timeFromData - $timeToData")
                onTimeSet(
                    hourFrom = timeFromData.hour.toInt(),
                    minuteFrom = timeFromData.min.toInt(),
                    hourTo = timeToData.hour.toInt(),
                    minTo = timeToData.min.toInt()
                )
            })
        )
            .textConfirm("Продолжить") //text of confirm button
            .textCancel("CANCEL") //text of cancel button
            .btnTextSize(20) // button text size
            .viewTextSize(25) // pick view text size
            .colorCancel(Color.parseColor("#999999")) //color of cancel button
            .colorConfirm(Color.parseColor("#A9A9A9"))//color of confirm button
            .build()

    }

    private fun getArenaByTime(activePid: String?) {
        require(!TextUtils.isEmpty(activePid)) { "+++ ArenaStorage active pid must be set. +++" }

        if (TextUtils.isEmpty(TimeDataModel.start) || TextUtils.isEmpty(TimeDataModel.end)) {
            toast("Не указана дата")
            return
        }

        val time = mutableMapOf<String, String>()
        time["start_at"] = TimeDataModel.start
        time["end_at"] = TimeDataModel.end
// 		time["start_at"] = "2019-01-26T20:00:00" //		time["end_at"] = "2019-01-26T21:00:00"
        mVm.fetchSchema(activePid, time)
    }

    private fun getHallArenaByTime(activePid: String?) {
        require(!TextUtils.isEmpty(activePid)) { "+++ ArenaStorage active pid must be set. +++" }

        if (TextUtils.isEmpty(TimeDataModel.start) || TextUtils.isEmpty(TimeDataModel.end)) {
            toast("Не указана дата")
            return
        }

        val time = mutableMapOf<String, String>()
        time["start_at"] = TimeDataModel.start
        time["end_at"] = TimeDataModel.end
// 		time["start_at"] = "2019-01-26T20:00:00" //		time["end_at"] = "2019-01-26T21:00:00"
        mVm.fetchHallSchema(activePid, time)
    }


    private var daySchedule: ScheduleItem? = null

    override fun onDateSet(datePicker: DatePicker?, year: Int, month: Int, day: Int) {

        val dayFormat = SimpleDateFormat("EEEE", Locale.ENGLISH)

        val calendar = Calendar.getInstance()
        // get day of week
        calendar.set(Calendar.DAY_OF_MONTH, day)
        mDayOfWeek = dayFormat.format(calendar.time)
        // save start and end time work on this day from schedulers
        // TODO: fix indexOf bound exeption
        try {
            daySchedule = mArenaSchedule?.filter { it.weekDay == mDayOfWeek }?.get(0)
        } catch (e: Exception) {
            Timber.e(e)
            return
        }

        PrefUtils.openTime = daySchedule?.startTime?.replaceAfter("00", "")
        PrefUtils.closeTime = daySchedule?.endTime?.replaceAfter("00", "")

        // set selected date
//        val monthLoc = Month.of(month + 1).getDisplayName(TextStyle.FULL, Locale("RU"))
//        val month_date = SimpleDateFormat("MMMM", Locale("RU"))
//        val month_date = SimpleDateFormat("MMMM", Locale("RU"))
        val monthLoc = (DateFormatSymbols(Locale("RU")).months[month])

        val selectedDate = "$day $monthLoc $year"

        TimeDataModel.setDateFromCalendar(selectedDate)
        PrefUtils.selectedDate = TimeDataModel.selectDate
        TimeDataModel.timeFrom = ""
        TimeDataModel.timeTo = ""

        TimeDataModel.setOpenTime(PrefUtils.openTime!!)
        TimeDataModel.setCloseTime(PrefUtils.closeTime!!)

        // update date info
        tvDate?.text = PrefUtils.selectedDate
        tvTime?.text = TimeDataModel.time
    }

    @SuppressLint("NewApi")
    fun onTimeSet(hourFrom: Int, minuteFrom: Int, hourTo: Int, minTo: Int) {

        val calendar = Calendar.getInstance()

        // from
        calendar.set(Calendar.HOUR_OF_DAY, hourFrom)
        calendar.set(Calendar.MINUTE, minuteFrom)
        val sdf = SimpleDateFormat("HH:mm", Locale("RU"))
        val timeFrom = sdf.format(calendar.time)

        // to
        calendar.add(Calendar.HOUR_OF_DAY, hourTo - hourFrom)
        val timeTo = sdf.format(calendar.time)

        val time = "$timeFrom - $timeTo"

        TimeDataModel.timeFrom = timeFrom
        TimeDataModel.timeTo = timeTo

        TimeDataModel.setStartAt(timeFrom)
        TimeDataModel.setEndAt(timeTo)

        tvTime?.text = time
    }

    private fun initListeners() {
        // date
        vDateTap?.setOnClickListener {
            showDatePickerDialog(it)
        }
        tvDate?.setOnClickListener {
            showDatePickerDialog(it)
        }

        // time
        vTimeTap?.setOnClickListener {
            showTimePickerDialog(it)
        }
        tvTime?.setOnClickListener {
            showTimePickerDialog(it)
        }


        // next button
        nextButton?.setOnClickListener {
            when {
                TimeDataModel.date.isEmpty() -> {
                    longToast("Выберите дату")
                }
                TimeDataModel.timeFrom.isEmpty() -> {
                    longToast("Выберите время")
                }
                TimeDataModel.validateDate() -> {
                    progressBar.visibility = View.VISIBLE
                    if (seat?.hallName == null) getArenaByTime(mArenaActivePid)
                    else getHallArenaByTime(seat?.hallPublicId)
                    activity?.supportFragmentManager?.executePendingTransactions()
                }
                TimeDataModel.isDateWrong -> {
                    longToast("Неправильно выбрана дата")
                }
                TimeDataModel.isScheduleWrong -> {
                    longToast("Время работы клуба на ${PrefUtils.selectedDate} г.:  с ${PrefUtils.openTime} до ${PrefUtils.closeTime}.")
                }
                else
                ->
                    longToast("Неправильно выбрана дата")
            }
        }
    }

    private fun updateSeatInfo(seat: SeatCarousel?) {
        val listener = object : BaseControllerListener<ImageInfo>() {
            override fun onFinalImageSet(id: String?, @Nullable imageInfo: ImageInfo?, @Nullable animatable: Animatable?) {
                progressHeadImage.visibility = View.GONE
            }

            override fun onFailure(id: String?, throwable: Throwable?) {
                progressHeadImage.visibility = View.GONE
                Toast.makeText(activity, getString(R.string.error_loading_image), Toast.LENGTH_SHORT).show()
            }

        }

        PrefUtils.hallName = seat?.title
        mSeatName?.text = seat?.title

      //  mSeatImage?.setImageURI(Uri.parse(seat?.imageUrl))

        val controller = Fresco.newDraweeControllerBuilder()
            .setUri(Uri.parse(seat?.imageUrl))
            .setControllerListener(listener)
            .build()
        mSeatImage?.controller = controller
        //TODO from html
      //  mCpuDescription?.text = seat?.description.let { it?.replace(oldValue = "\\", newValue = "") }

        val description = seat?.description.let { it?.replace(oldValue = "\\", newValue = "") }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mCpuDescription?.text = Html.fromHtml(description, Html.FROM_HTML_MODE_LEGACY)
        } else {
            mCpuDescription?.text = Html.fromHtml(description)
        }
    }

    private fun showDatePickerDialog(v: View) {
        val newFragment = DatePickerFragment(this)
        activity?.supportFragmentManager?.let { newFragment.show(it, "datePicker") }
    }

    private fun showTimePickerDialog(v: View) {

        if (TextUtils.isEmpty(TimeDataModel.date)) {
            toast("Сначала выберите дату")
            return
        }
        pickerDialog?.showPopWin(requireActivity())
    }
}

class DatePickerFragment(
    private val listener: DatePickerDialog.OnDateSetListener
) : DialogFragment() {

    init {
        requireNotNull(listener) { "+++++ Must implement DatePickerDialog.OnDateSetListener. +++++" }
    }

    @SuppressLint("NewApi")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        return DatePickerDialog(
            context, R.style.Theme_Winstrike_DateDialog, listener, year,
            month, day
        )
    }
}

