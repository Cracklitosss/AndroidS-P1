package com.example.myapplication.ui.auth

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.local.SecurePreferences
import com.example.myapplication.data.model.LoginResponse
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
    private val registerUseCase: RegisterUseCase,
    private val securePreferences: SecurePreferences
) : ViewModel() {
    private val _authState = MutableStateFlow<AuthState>(AuthState.Initial)
    val authState: StateFlow<AuthState> = _authState

    init {
        // Verificar si hay credenciales guardadas y remember me está activado
        if (securePreferences.getRememberMe()) {
            val email = securePreferences.getSavedEmail()
            val password = securePreferences.getSavedPassword()
            if (!email.isNullOrEmpty() && !password.isNullOrEmpty()) {
                login(email, password, true)
            }
        }
    }

    fun login(email: String, password: String, isAutoLogin: Boolean = false) {
        viewModelScope.launch {
            if (!isAutoLogin) {
                _authState.value = AuthState.Loading
            }
            
            loginUseCase(email, password).fold(
                onSuccess = { loginResponse ->
                    if (loginResponse.success) {
                        // Guardar token y datos de usuario
                        loginResponse.token?.let { securePreferences.saveAuthToken(it) }
                        securePreferences.saveUserEmail(email)
                        
                        _authState.value = AuthState.Success(loginResponse.message)
                    } else {
                        _authState.value = AuthState.Error(loginResponse.message)
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
                onSuccess = { registerResponse ->
                    if (registerResponse.success) {
                        // Guardar token y datos de usuario
                        registerResponse.token?.let { securePreferences.saveAuthToken(it) }
                        securePreferences.saveUserEmail(email)
                        
                        _authState.value = AuthState.Success(registerResponse.message)
                    } else {
                        _authState.value = AuthState.Error(registerResponse.message)
                    }
                },
                onFailure = {
                    _authState.value = AuthState.Error(it.message ?: "Error desconocido")
                }
            )
        }
    }
    
    fun saveRememberMe(remember: Boolean, email: String, password: String) {
        securePreferences.saveRememberMe(remember)
        if (remember) {
            securePreferences.saveCredentials(email, password)
        } else {
            // Si desmarca remember me, limpiamos las credenciales guardadas
            securePreferences.saveCredentials("", "")
        }
    }
    
    fun logout() {
        securePreferences.clearAuthToken()
        _authState.value = AuthState.Initial
    }
    
    fun isLoggedIn(): Boolean {
        return securePreferences.getAuthToken() != null
    }
}

sealed class AuthState {
    object Initial : AuthState()
    object Loading : AuthState()
    data class Success(val message: String) : AuthState()
    data class Error(val message: String) : AuthState()
} 