package com.spacey.myhome.dateservices

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.liveData
import com.spacey.myhome.dateservices.datelist.DatePagingSource
import com.spacey.myhome.domain.Deliverer
import com.spacey.myhome.domain.Service
import com.spacey.myhome.domain.ServiceJob
import com.spacey.myhome.domain.ServiceRegistry
import com.spacey.myhome.domain.Subscription
import com.spacey.myhome.domain.User
import java.time.LocalDate

class DateServicesViewModel : ViewModel() {

    val defaultDate: LocalDate = LocalDate.now()

    private val _selectedDate: MutableLiveData<LocalDate> = MutableLiveData(defaultDate)
    val selectedDate: LiveData<LocalDate> = _selectedDate.distinctUntilChanged()

    private val _subscriptionList: MutableLiveData<List<Subscription>> = MutableLiveData()
    val subscriptionList: LiveData<List<Subscription>> = _subscriptionList

    fun getSelectedDate(): LocalDate = selectedDate.value ?: defaultDate

    private lateinit var dataSource: DatePagingSource

    val datePager = Pager(PagingConfig(100), initialKey = selectedDate.value, pagingSourceFactory = {
        DatePagingSource().also { dataSource = it }
    }).liveData.cachedIn(viewModelScope)

    private val deliverer = Deliverer(User("Sample", "SamplePass"))
    private val user = User("Space", "SpaceBar")

    fun selectDate(date: LocalDate) {
        _selectedDate.value = date
        _subscriptionList.value = getSubscriptionList(date)
    }

    private fun getSubscriptionList(date: LocalDate): List<Subscription> =
        (1 until date.dayOfMonth).map {
            Subscription(
                ServiceRegistry(ServiceJob(deliverer, Service("${date.dayOfWeek} $it")), user),
                LocalDate.ofYearDay(2024, 1),
                listOf(),
                it.toDouble()
            )
        }
}