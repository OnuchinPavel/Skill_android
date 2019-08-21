package ru.prsolution.winstrike.presentation.utils.date

import timber.log.Timber
import java.util.*
import kotlin.collections.LinkedHashMap
import kotlin.properties.ObservableProperty
import kotlin.reflect.KProperty

object TimeDataModel {

    var pids: LinkedHashMap<Int, String> = LinkedHashMap()
    var date: String = "" // by observing("", willSet = { validateDate() })
    var time: String = "Укажите диапазон времени"
    var selectDate: String = ""
    var start: String = ""
    var end: String = ""
    var timeFrom: String = ""
    var timeTo: String = ""
    var dateOpenAt: Date? = null
    var dateCloseAt: Date? = null
    var isDateSelect = false
    var isDateWrong = false
    var isScheduleWrong = false

    fun setOpenTime(openTime: String) {
        val start = DateTransform.getFormattedDateWithTime(openTime)
        dateOpenAt = DateTransform.getDateInUTC(
            start
        )

    }

    fun setCloseTime(closeTime: String) {
        val end = DateTransform.getFormattedDateWithTime(closeTime)
        dateCloseAt = DateTransform.getDateInUTC(
            end
        )
    }


    fun setStartAt(startAt: String) {
        start = DateTransform.getFormattedDateWithTime(startAt)
        startDate = DateTransform.getDateInUTC(
            start
        )
    }

    fun setEndAt(endAt: String) {
        end = DateTransform.getFormattedDateWithTime(endAt)
        endDate = DateTransform.getDateInUTC(
            end
        )
    }

    fun setDateFromCalendar(dateCal: String) {
        selectDate = DateTransform.getSimpleDateFromCalendar(dateCal)
        date = DateTransform.getSimpleDateFromCalendar(selectDate)
    }

    fun setSelectDate(datetime: Date) {
        selectDate = DateTransform.getSimpleDateFromCalendar(datetime)
        date = selectDate
    }

    var startDate: Date by observing(Date(), didSet = {
        //        validateDate()
    })

    var endDate: Date by observing(Date(), didSet = {
        //        validateDate()
    })


    fun validateDate(): Boolean {
        val current = Date()
        if (!start.isEmpty() && !end.isEmpty()) {
            isDateValid = startDate < endDate && startDate >= current
                    &&
                    startDate >= dateOpenAt && endDate <= dateCloseAt
        }
        isDateWrong = !(startDate < endDate && startDate >= current)
        isScheduleWrong = !(startDate >= dateOpenAt && endDate <= dateCloseAt)

        return isDateValid
    }

    var isDateValid: Boolean by observing(false, {
        Timber.d(
            "dates: valid $isDateValid, start_at: $start, endAt: $end"
        )
    })

    fun clearPids() {
        pids = LinkedHashMap()
    }

    fun clearDateTime() {
        isDateSelect = false
//        date.set("Выберите дату")
//        time.set("Укажите диапазон времени")
        start = ""
        end = ""
        startDate = Date()
        endDate = Date()
        selectDate = ""
    }

    fun <T> observing(
        initialValue: T,
        willSet: () -> Unit = { },
        didSet: () -> Unit = { }
    ) = object : ObservableProperty<T>(initialValue) {
        override fun beforeChange(property: KProperty<*>, oldValue: T, newValue: T): Boolean =
            true.apply { willSet() }

        override fun afterChange(property: KProperty<*>, oldValue: T, newValue: T) = didSet()
    }
}
