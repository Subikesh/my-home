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

    private val _uiState = MutableStateFlow(HomeUiState(LocalDate.now(), emptyList(), emptyList(), emptyList()))
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
                val count = if (this.serviceAmount == 0.0) 0 else (this.defaultAmount / this.serviceAmount).toInt()
                Field.Counter(this.service, count)
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
    val servicesList: List<Service>
)

sealed class HomeEvent {
    data class SetDate(val date: LocalDate) : HomeEvent()
}