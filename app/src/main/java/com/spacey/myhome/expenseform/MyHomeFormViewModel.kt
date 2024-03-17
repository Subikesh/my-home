package com.spacey.myhome.expenseform

import androidx.lifecycle.viewModelScope
import com.spacey.data.AppComponent
import com.spacey.data.service.ExpenseRepository
import com.spacey.data.service.ServiceEntity
import com.spacey.myhome.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

class MyHomeFormViewModel : BaseViewModel<MyHomeFormUiState, MyHomeFormEvent>() {

    private val _uiState: MutableStateFlow<MyHomeFormUiState> = MutableStateFlow(MyHomeFormUiState.Loading)
    override val uiState: StateFlow<MyHomeFormUiState> = _uiState

    private val repository: ExpenseRepository = AppComponent.expenseRepository

    override fun onEvent(event: MyHomeFormEvent) {
        when (event) {
            is MyHomeFormEvent.SetService -> {
                viewModelScope.launch {
                    _uiState.value = repository.getServices(event.date, event.service)?.let {
                        MyHomeFormUiState.Success(it)
                    } ?: MyHomeFormUiState.Failure
                }
            }
            is MyHomeFormEvent.CreateExpense -> {
                viewModelScope.launch {
                    repository.insertExpense(event.serviceEntity)
                }
            }
        }
    }
}

sealed class MyHomeFormUiState {
    data object Loading : MyHomeFormUiState()
    data class Success(val serviceEntity: ServiceEntity) : MyHomeFormUiState()
    data object Failure : MyHomeFormUiState()
}

sealed class MyHomeFormEvent {
    data class SetService(val service: String, val date: LocalDate) : MyHomeFormEvent()
    data class CreateExpense(val serviceEntity: ServiceEntity) : MyHomeFormEvent()
}
