package com.example.myapplication.domain.usecase.auth

import com.example.myapplication.data.model.AuthResponse
import com.example.myapplication.data.model.LoginResponse
import com.example.myapplication.domain.repository.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): Result<LoginResponse> {
        // Primero intentamos con el usuario local
        if (email == "test@test.com" && password == "test123") {
            return Result.success(
                LoginResponse(
                    success = true,
                    message = "Login exitoso con usuario local",
                    token = "local_test_token"
                )
            )
        }

        // Si no es el usuario local, intentamos con la API
        return try {
            val result = authRepository.login(email, password)
            result.map { authResponse ->
                LoginResponse(
                    success = authResponse.success,
                    message = authResponse.message,
                    token = authResponse.token
                )
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
} 