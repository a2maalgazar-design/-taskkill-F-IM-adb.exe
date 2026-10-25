package com.example.util

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object WorkWeekUtil {

    private val ARABIC_DAYS = listOf(
        "الأحد", "الاثنين", "الثلاثاء", "الأربعاء", "الخميس", "الجمعة", "السبت"
    )

    private val ARABIC_MONTHS = listOf(
        "يناير", "فبراير", "مارس", "أبريل", "مايو", "يونيو",
        "يوليو", "أغسطس", "سبتمبر", "أكتوبر", "نوفمبر", "ديسمبر"
    )

    fun getTodayDayNameAr(): String {
        val cal = Calendar.getInstance()
        val dayIndex = cal.get(Calendar.DAY_OF_WEEK) - 1 // 0 = Sunday, 1 = Monday ... 6 = Saturday
        return ARABIC_DAYS.getOrElse(dayIndex) { "اليوم" }
    }

    fun getTodayDateAr(): String {
        val sdf = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
        return sdf.format(Date())
    }

    fun getTodayDateIso(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(Date())
    }

    fun getWorkWeekString(date: Date = Date()): String {
        val cal = Calendar.getInstance()
        cal.time = date
        val weekNumber = cal.get(Calendar.WEEK_OF_YEAR)
        val monthName = ARABIC_MONTHS.getOrElse(cal.get(Calendar.MONTH)) { "" }
        val year = cal.get(Calendar.YEAR)
        return "الأسبوع $weekNumber - $monthName $year"
    }

    fun isSundayToday(): Boolean {
        return Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY
    }

    fun isPastOnePm(): Boolean {
        val cal = Calendar.getInstance()
        val hour = cal.get(Calendar.HOUR_OF_DAY)
        val minute = cal.get(Calendar.MINUTE)
        return (hour > 13) || (hour == 13 && minute > 0)
    }

    fun isVegOrderDelayedOver24h(orderDateStr: String): Boolean {
        return try {
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val orderDate = sdf.parse(orderDateStr) ?: return false
            val cal = Calendar.getInstance()
            cal.time = orderDate
            cal.set(Calendar.HOUR_OF_DAY, 13)
            cal.set(Calendar.MINUTE, 0)
            cal.set(Calendar.SECOND, 0)
            val fixedArrivalMs = cal.timeInMillis
            val diffHours = (System.currentTimeMillis() - fixedArrivalMs) / (1000 * 3600)
            diffHours >= 24
        } catch (e: Exception) {
            false
        }
    }
}
