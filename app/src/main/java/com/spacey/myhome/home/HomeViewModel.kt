package com.spacey.myhome.home

import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.viewModelScope
import com.spacey.data.AppComponent
import com.spacey.data.base.InputType
import com.spacey.data.service.ExpenseRepository
import com.spacey.myhome.base.BaseViewModel
import com.spacey.myhome.ui.component.Field
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

class HomeViewModel : BaseViewModel<HomeUiState, HomeEvent>() {

    private val _uiState = MutableStateFlow(HomeUiState(LocalDate.now(), emptyList()))
    override val uiState: StateFlow<HomeUiState>
        get() = _uiState

    override fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.SetDate -> {
                viewModelScope.launch {
                    refreshExpenses(event.date)
                }
                _uiState.value = _uiState.value.copy(selectedDate = event.date)
            }
        }
    }

    private val repository: ExpenseRepository = AppComponent.expenseRepository

    private suspend fun refreshExpenses(date: LocalDate) {
        repository.getExpenses(date).collect {
            val field: List<Field<*>> = it.map { expense ->
                val service = expense.service
                // TODO: Calculate this amount count
                val count = if (service.amount.toInt() == 0) 0 else (expense.amount / service.amount).toInt()
                when (service.type) {
                    // TODO: review amount/text inputType
                    InputType.AMOUNT -> Field.Text(service.name, KeyboardType.Text)
                    InputType.COUNTER -> Field.Counter(service.name, count)
                    InputType.CHECKBOX -> Field.CheckBox(service.name, false)
                }
            }
            _uiState.value = _uiState.value.copy(expenseList = field)
        }
    }
}

data class HomeUiState(
    val selectedDate: LocalDate,
    val expenseList: List<Field<*>>
)

sealed class HomeEvent {
    data class SetDate(val date: LocalDate) : HomeEvent()
}