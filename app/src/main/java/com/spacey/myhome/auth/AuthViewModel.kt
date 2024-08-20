package com.spacey.myhome.auth

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.spacey.myhome.data.network.AuthApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AuthViewModel(private val service: AuthApiService) : ViewModel() {

    private val _isAuthenticated = MutableLiveData<AuthState>()
    val isAuthenticated: LiveData<AuthState> = _isAuthenticated

    fun login(userName: String, password: String) {
        viewModelScope.launch {
            _isAuthenticated.value = AuthState.LOADING
            try {
                val result = withContext(Dispatchers.IO) {
                    service.userAuth(userName, password)
                }
                Log.d("Auth", "Result: $result")
                _isAuthenticated.value = AuthState.SUCCESS
            } catch (e: Exception) {
                e.printStackTrace()
                _isAuthenticated.value = AuthState.FAILURE
            }
        }
    }

    class Factory(private val authApiService: AuthApiService) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return AuthViewModel(authApiService) as T
        }
    }

    enum class AuthState {
        LOADING, SUCCESS, FAILURE
    }
}