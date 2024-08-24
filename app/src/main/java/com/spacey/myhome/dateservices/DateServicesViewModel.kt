package com.spacey.myhome.dateservices

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.time.LocalDate

class DateServicesViewModel : ViewModel() {

    private val _selectedDate: MutableLiveData<LocalDate> = MutableLiveData(LocalDate.ofYearDay(2022, 5))
    val selectedDate: LiveData<LocalDate> = _selectedDate

    fun selectDate(date: LocalDate) {
        _selectedDate.value = date
    }
}