package com.example.myapplication.ui.auth

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.domain.usecase.auth.LoginUseCase
import com.example.myapplication.domain.usecase.auth.RegisterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase
) : ViewModel() {
    private val _authState = MutableStateFlow<AuthState>(AuthState.Initial)
    val authState: StateFlow<AuthState> = _authState

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            loginUseCase(email, password).fold(
                onSuccess = { response ->
                    _authState.value = if (response.success) {
                        AuthState.Success(response.message)
                    } else {
                        AuthState.Error("Error en el login")
                    }
                },
                onFailure = {
                    _authState.value = AuthState.Error(it.message ?: "Error desconocido")
                }
            )
        }
    }

    fun register(email: String, password: String, photoUri: Uri?) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            registerUseCase(email, password, photoUri).fold(
                onSuccess = { response ->
                    _authState.value = if (response.success) {
                        AuthState.Success(response.message)
                    } else {
                        AuthState.Error("Error en el registro")
                    }
                },
                onFailure = {
                    _authState.value = AuthState.Error(it.message ?: "Error desconocido")
                }
            )
        }
    }
}

sealed class AuthState {
    object Initial : AuthState()
    object Loading : AuthState()
    data class Success(val message: String) : AuthState()
    data class Error(val message: String) : AuthState()
} 