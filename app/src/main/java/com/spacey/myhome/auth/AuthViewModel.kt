package com.spacey.myhome.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
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
            try {
                val result = withContext(Dispatchers.IO) {
                    service.userAuth(userName, password)
                }
                Log.d("Auth", "Result: $result")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    class Factory(private val authApiService: AuthApiService) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return AuthViewModel(authApiService) as T
        }
    }
}