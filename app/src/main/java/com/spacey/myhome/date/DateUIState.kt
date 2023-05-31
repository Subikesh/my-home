package com.spacey.myhome.date

import com.spacey.myhome.utils.CalendarUtils

data class DateUIState(val year: Int, val month: Int, val dayOfMonth: Int) {
    companion object {
        fun getCurrentDateState(): DateUIState {
            val currentMillis = System.currentTimeMillis()
            val year = CalendarUtils.getYearFromMillis(currentMillis)
            val month = CalendarUtils.getMonthFromMillis(currentMillis)
            val dayOfMonth = CalendarUtils.getDayOfMonthFromMillis(currentMillis)
            return DateUIState(year, month, dayOfMonth)
        }
    }
}
