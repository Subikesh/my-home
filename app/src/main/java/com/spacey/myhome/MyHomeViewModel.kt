package com.spacey.myhome

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spacey.data.AppComponent
import com.spacey.data.base.InputType
import com.spacey.data.service.ExpenseEntity
import com.spacey.data.service.ExpenseRepository
import com.spacey.myhome.ui.component.Field
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

class MyHomeViewModel : ViewModel() {

    private val repository: ExpenseRepository = AppComponent.expenseRepository

    private val _expenseList: MutableStateFlow<List<Field>> = MutableStateFlow(emptyList())
    val expenseList: StateFlow<List<Field>> = _expenseList

    val currentDate: MutableState<LocalDate> = mutableStateOf(LocalDate.now())

    fun setDate(date: LocalDate) {
        viewModelScope.launch {
            currentDate.value = date
            repository.getExpenses(date).collect {
                val field = it.map { expense ->
                    val service = expense.service
                    // TODO: Calculate this amount count
                    val count = if (service.amount.toInt() == 0) 0 else (expense.amount / service.amount).toInt()
                    when (service.type) {
                        // TODO: review amount/text inputType
                        InputType.AMOUNT -> Field.Text(service.name, KeyboardType.Text)
                        InputType.COUNTER -> Field.Counter(service.name, 2)
                        InputType.CHECKBOX -> Field.CheckBox(service.name, false)
                    }
                }
                _expenseList.value = field
            }
        }
    }

    fun addExpense(expense: ExpenseEntity) {
        viewModelScope.launch {
            repository.insertExpense(expense)
        }
    }
}