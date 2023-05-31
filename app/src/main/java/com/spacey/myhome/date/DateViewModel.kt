package com.spacey.myhome.date

import androidx.lifecycle.ViewModel
import com.spacey.myhome.utils.CalendarUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class DateViewModel : ViewModel() {

    private val _dateState: MutableStateFlow<DateUIState> = MutableStateFlow(DateUIState.getCurrentDateState())
    val dateState: StateFlow<DateUIState> = _dateState

    fun setDate(year: Int, month: Int, dayOfMonth: Int) {
        _dateState.value = DateUIState(year = year, month = month, dayOfMonth = dayOfMonth)
    }

    fun setDate(millis: Long) {
        val year = CalendarUtils.getYearFromMillis(millis)
        val month = CalendarUtils.getMonthFromMillis(millis)
        val dayOfMonth = CalendarUtils.getDayOfMonthFromMillis(millis)
        setDate(year, month, dayOfMonth)
    }
}