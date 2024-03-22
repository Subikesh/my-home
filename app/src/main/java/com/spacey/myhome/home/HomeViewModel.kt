package com.spacey.myhome.home

import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.viewModelScope
import com.spacey.data.AppComponent
import com.spacey.data.base.InputType
import com.spacey.data.service.ExpenseRepository
import com.spacey.data.service.Service
import com.spacey.data.service.ServiceEntity
import com.spacey.myhome.base.BaseViewModel
import com.spacey.myhome.ui.component.Field
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

class HomeViewModel : BaseViewModel<HomeUiState, HomeEvent>() {

    private val _uiState = MutableStateFlow(HomeUiState(LocalDate.now(), emptyList(), emptyList(), emptyList(), false))
    override val uiState: StateFlow<HomeUiState> = _uiState

    override fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.SetDate -> {
                viewModelScope.launch {
                    refreshExpenses(event.date)
                    _uiState.value = _uiState.value.copy(selectedDate = event.date, isExpenseLoading = false)
                }
            }
            is HomeEvent.UpdateExpense -> {
                viewModelScope.launch {
                    _uiState.value = _uiState.value.copy(isExpenseLoading = true)
                    repository.updateExpense(event.field.label, uiState.value.selectedDate, event.value)
                    _uiState.value = _uiState.value.copy(isExpenseLoading = false)
                }
            }
        }
    }

    private val repository: ExpenseRepository = AppComponent.expenseRepository

    private suspend fun refreshExpenses(date: LocalDate) {
        val expenses = repository.getServices(date)
        val fields: List<Field<*>> = expenses.map { it.toField() }
        val nonSubscribedServices = repository.getAllServices(date).filterNot { it.service in expenses.map { expense -> expense.service } }
        val nonSubscribedFields = nonSubscribedServices.map { it.copy(defaultAmount = 0.0).toField() }
        val services = repository.getServices()
        _uiState.value = _uiState.value.copy(expenseList = fields, notSubscribedExpenses = nonSubscribedFields, servicesList = services)
    }

    private fun ServiceEntity.toField(): Field<*> {
        return when (this.inputType) {
            // TODO: review amount/text inputType
            InputType.AMOUNT -> Field.Text(this.service, KeyboardType.Text, this.defaultAmount.toString())
            InputType.COUNTER -> {
                Field.Counter(this.service, this.defaultCount)
            }
            InputType.CHECKBOX -> {
                val isChecked = this.defaultAmount > 0
                Field.CheckBox(this.service, isChecked)
            }
        }
    }
}

data class HomeUiState(
    val selectedDate: LocalDate,
    val expenseList: List<Field<*>>,
    val notSubscribedExpenses: List<Field<*>>,
    val servicesList: List<Service>,
    val isExpenseLoading: Boolean
)

sealed class HomeEvent {
    data class SetDate(val date: LocalDate) : HomeEvent()
    data class UpdateExpense(val field: Field<*>, val value: Int) : HomeEvent()
}