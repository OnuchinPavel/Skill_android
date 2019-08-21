package ru.prsolution.winstrike.presentation.utils.date

import android.text.TextUtils
import ru.prsolution.winstrike.presentation.utils.date.TimeDataModel.date
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DateTransform {

    companion object {

        fun getSimpleDateFromCalendar(date: Date): String {
            val simpleDateFormat = SimpleDateFormat("dd MMMM yyyy", Locale("RU", "RU"))
            return simpleDateFormat.format(date)
        }

        fun getSimpleDateFromCalendar(dateStr: String): String {
            val simpleDateFormat = SimpleDateFormat("dd MMMM yyyy", Locale("RU", "RU"))
            val date = simpleDateFormat.parse(dateStr)
            return simpleDateFormat.format(date)
        }

        fun getFormattedDateWithTime(time: String): String {
            require(!TextUtils.isEmpty(date)) {
                "++++ Date must be set before time! ++++"
            }
            var dateInStr = date
            var fmtDate = Date()
            val date: String

            dateInStr += "T$time:00"
            val formatter = SimpleDateFormat("dd MMM yyyy'T'HH:mm:ss", Locale("RU", "RU"))
            try {
                fmtDate = formatter.parse(dateInStr)
            } catch (e: java.text.ParseException) {
                e.printStackTrace()
            }

            date = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale("RU", "RU")).format(fmtDate)
            return date
        }

        fun getDateInUTC(time: String): Date {
            var fmtDate = Date()
            val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale("RU", "RU"))
            try {
                fmtDate = formatter.parse(time)
            } catch (e: java.text.ParseException) {
                e.printStackTrace()
            }

            return fmtDate
        }
    }
}