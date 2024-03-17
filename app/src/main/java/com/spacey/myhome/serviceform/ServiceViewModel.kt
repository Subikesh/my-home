package com.spacey.myhome.serviceform

import androidx.lifecycle.viewModelScope
import com.spacey.data.AppComponent
import com.spacey.data.service.Service
import com.spacey.myhome.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ServiceViewModel : BaseViewModel<Unit, ServiceEvent>() {

//    private val _uiState = MutableStateFlow(Unit.Loading)
    override val uiState: StateFlow<Unit> = MutableStateFlow(Unit)

    private val serviceRepository = AppComponent.expenseRepository

    override fun onEvent(event: ServiceEvent) {
        when (event) {
            is ServiceEvent.AddService -> {
                viewModelScope.launch {
                    serviceRepository.createService(event.service)
                }
            }
        }
    }
}

//sealed class ServiceUiState {
//    data object Loading : ServiceUiState()
//    data class Success(val service: Service) : ServiceUiState()
//    data object Failure
//}

sealed class ServiceEvent {
//    data class SetService(val service: Service) : ServiceEvent()
    data class AddService(val service: Service) : ServiceEvent()
}