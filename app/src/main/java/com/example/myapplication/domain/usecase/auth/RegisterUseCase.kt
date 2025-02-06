package com.example.myapplication.domain.usecase.auth

import com.example.myapplication.domain.repository.AuthRepository
import com.example.myapplication.domain.model.AuthResponse
import javax.inject.Inject
import android.net.Uri

class RegisterUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String, photoUri: Uri?): Result<AuthResponse> {
        return authRepository.register(email, password, photoUri)
    }
} 