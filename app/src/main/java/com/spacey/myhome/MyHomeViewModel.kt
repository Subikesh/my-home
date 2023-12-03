package com.spacey.myhome

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spacey.data.AppComponent
import com.spacey.data.service.ExpenseEntity
import com.spacey.data.service.ExpenseRepository
import kotlinx.coroutines.launch

class MyHomeViewModel : ViewModel() {

    private val repository: ExpenseRepository = AppComponent.expenseRepository

    fun addExpense(expense: ExpenseEntity) {
        viewModelScope.launch {
            repository.insertExpense(expense)
        }
    }

}