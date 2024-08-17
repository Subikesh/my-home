package com.spacey.myhome.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spacey.myhome.data.network.AuthApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AuthViewModel(private val service: AuthApiService) : ViewModel() {

    val isAuthenticated: Boolean
        get() = true

    fun login(userName: String, password: String) {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                service.userAuth(userName, password)
            }
            Log.d("Auth", "Result: $result")
        }
    }
}