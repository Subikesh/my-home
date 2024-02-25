package com.spacey.myhome.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.StateFlow

abstract class BaseViewModel<UiState, Event> : ViewModel() {

    abstract val uiState: StateFlow<UiState>

    abstract fun onEvent(event: Event)
}