package com.spacey.myhome

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spacey.data.AppComponent
import com.spacey.data.service.ExpenseEntity
import com.spacey.data.service.ExpenseRepository
import kotlinx.coroutines.launch
import java.time.LocalDate

@Deprecated("Use new home view model")
class MyHomeViewModel : ViewModel() {

    private val repository: ExpenseRepository = AppComponent.expenseRepository

    val currentDate: MutableState<LocalDate> = mutableStateOf(LocalDate.now())

    fun addExpense(expense: ExpenseEntity) {
        viewModelScope.launch {
            repository.insertExpense(expense)
        }
    }
}