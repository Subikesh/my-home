package com.spacey.myhome.form

import androidx.lifecycle.viewModelScope
import com.spacey.data.AppComponent
import com.spacey.data.service.ExpenseRepository
import com.spacey.data.service.ServiceEntity
import com.spacey.myhome.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MyHomeFormViewModel : BaseViewModel<Unit, MyHomeFormEvent>() {
    override val uiState: StateFlow<Unit> = MutableStateFlow(Unit)

    private val repository: ExpenseRepository = AppComponent.expenseRepository

    override fun onEvent(event: MyHomeFormEvent) {
        when (event) {
            is MyHomeFormEvent.CreateExpense -> {
                viewModelScope.launch {
                    repository.insertExpense(event.serviceEntity)
                }
            }
        }
    }
}

sealed class MyHomeFormEvent {
    data class CreateExpense(val serviceEntity: ServiceEntity) : MyHomeFormEvent()
}
