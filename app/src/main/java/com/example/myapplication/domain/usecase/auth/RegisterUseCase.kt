package com.example.myapplication.domain.usecase.auth

import android.net.Uri
import com.example.myapplication.data.model.RegisterResponse
import com.example.myapplication.domain.repository.AuthRepository
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String, photoUri: Uri?): Result<RegisterResponse> {
        return try {
            val result = authRepository.register(email, password, photoUri)
            result.map { authResponse ->
                RegisterResponse(
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