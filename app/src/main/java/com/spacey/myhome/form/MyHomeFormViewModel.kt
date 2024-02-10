package com.spacey.myhome.form

import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.lifecycle.viewModelScope
import com.spacey.data.AppComponent
import com.spacey.data.service.ExpenseEntity
import com.spacey.data.service.ExpenseRepository
import com.spacey.myhome.base.BaseViewModel
import kotlinx.coroutines.launch

class MyHomeFormViewModel : BaseViewModel<Unit, MyHomeFormEvent>() {
    override val uiState: State<Unit> = derivedStateOf { }

    private val repository: ExpenseRepository = AppComponent.expenseRepository

    override fun onEvent(event: MyHomeFormEvent) {
        when (event) {
            is MyHomeFormEvent.CreateExpense -> {
                viewModelScope.launch {
                    repository.insertExpense(event.expense)
                }
            }
        }
    }
}

sealed class MyHomeFormEvent {
    data class CreateExpense(val expense: ExpenseEntity) : MyHomeFormEvent()
}
