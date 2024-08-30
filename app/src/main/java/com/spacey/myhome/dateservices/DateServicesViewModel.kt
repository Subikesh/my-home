package com.spacey.myhome.dateservices

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.liveData
import java.time.LocalDate

class DateServicesViewModel : ViewModel() {

    val defaultDate: LocalDate = LocalDate.now()

    private val _selectedDate: MutableLiveData<LocalDate> = MutableLiveData(defaultDate)
    val selectedDate: LiveData<LocalDate> = _selectedDate

    val datePager = Pager(PagingConfig(100), initialKey = selectedDate.value, pagingSourceFactory = {
        DatePagingSource()
    }).liveData

    fun selectDate(date: LocalDate) {
        _selectedDate.value = date
    }
}