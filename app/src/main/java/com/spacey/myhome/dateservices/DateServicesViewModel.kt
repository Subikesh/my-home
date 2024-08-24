package com.spacey.myhome.dateservices

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.time.LocalDate

class DateServicesViewModel : ViewModel() {

    val defaultDate: LocalDate = LocalDate.now()

    private val _selectedDate: MutableLiveData<LocalDate> = MutableLiveData(defaultDate)
    val selectedDate: LiveData<LocalDate> = _selectedDate

    fun selectDate(date: LocalDate) {
        _selectedDate.value = date
    }
}