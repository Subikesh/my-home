package com.spacey.myhome.utils

import java.util.Calendar

object CalendarUtils {
    fun getYearFromMillis(dateInMillis: Long): Int {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = dateInMillis
        return calendar.get(Calendar.YEAR)
    }

    fun getMonthFromMillis(dateInMillis: Long): Int {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = dateInMillis
        return calendar.get(Calendar.MONTH)
    }

    fun getDayOfMonthFromMillis(dateInMillis: Long): Int {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = dateInMillis
        return calendar.get(Calendar.DAY_OF_MONTH)
    }
}