package com.spacey.myhome.date

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spacey.myhome.data.DIServiceLocator
import com.spacey.myhome.data.base.Data
import com.spacey.myhome.data.expense.DateExpensesEntity
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Calendar

class DateViewModel : ViewModel() {

    private val _dateState: MutableStateFlow<DateUIState> = MutableStateFlow(DateUIState.LOADING(Calendar.getInstance()))
    val dateState: StateFlow<DateUIState> = _dateState

    private val _metadataDownloaded = MutableStateFlow(false)
    val metadataDownloaded: StateFlow<Boolean> = _metadataDownloaded

    private val expenseRepository = DIServiceLocator.expenseRepository

    private val jobList = mutableListOf<Job>()

    fun setDate(year: Int, month: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance().apply {
            set(year, month, dayOfMonth)
        }
        setDate(calendar)
    }

    fun setDate(millis: Long) {
        setDate(Calendar.getInstance().apply { timeInMillis = millis })
    }

    private fun setDate(calendar: Calendar) {
        while (jobList.isNotEmpty()) {
            jobList.removeLast().cancel()
        }
        val job = viewModelScope.launch {
            try {
                _dateState.value = DateUIState.LOADING(calendar)
                expenseRepository.getDateFields(dateState.value.date).collect { dateFields ->
                    _dateState.value = dateFields.toUIState(dateState.value.date)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        jobList.add(job)
    }

    fun getMetadata() {
        _metadataDownloaded.value = false
        viewModelScope.launch {
            Log.d("MEtadata", DIServiceLocator.metadataRepository.getMetadata().toString())
            _metadataDownloaded.value = true
        }
    }

    private fun Data<DateExpensesEntity>.toUIState(date: Calendar): DateUIState {
        return when (this) {
            is Data.Success -> DateUIState.SUCCESS(date, this.data.expenses)
            is Data.Error -> DateUIState.ERROR(date, this.exception)
        }
    }
}