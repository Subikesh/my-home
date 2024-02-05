package com.spacey.myhome.base

import androidx.compose.runtime.State
import androidx.lifecycle.ViewModel

abstract class BaseViewModel<UiState, Event> : ViewModel() {

    abstract val uiState: State<UiState>

    abstract fun onEvent(event: Event)
}