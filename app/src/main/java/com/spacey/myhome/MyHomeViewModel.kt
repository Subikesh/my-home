package com.spacey.myhome

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spacey.data.AppComponent
import com.spacey.data.service.DateRecurrence
import com.spacey.data.service.ExpenseEntity
import com.spacey.data.service.ExpenseRepository
import com.spacey.data.service.Service
import kotlinx.coroutines.launch

class MyHomeViewModel : ViewModel() {

    private val repository: ExpenseRepository = AppComponent.expenseRepository

    fun addExpense(service: Service, dateRecurrence: DateRecurrence) {
        viewModelScope.launch {
            repository.insertExpense(ExpenseEntity(service, service.amount, dateRecurrence))
        }
    }

}