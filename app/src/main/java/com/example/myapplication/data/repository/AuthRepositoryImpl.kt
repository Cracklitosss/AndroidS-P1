package com.example.myapplication.data.repository

import com.example.myapplication.data.api.ApiService
import com.example.myapplication.data.model.LoginRequest
import com.example.myapplication.data.model.LoginResponse
import com.example.myapplication.data.model.RegisterRequest
import com.example.myapplication.data.model.RegisterResponse
import com.example.myapplication.domain.model.AuthResponse
import com.example.myapplication.domain.repository.AuthRepository
import javax.inject.Inject
import android.net.Uri

class AuthRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : AuthRepository {

    override suspend fun login(email: String, password: String): Result<AuthResponse> {
        return try {
            val response = apiService.login(LoginRequest(email, password))
            if (response.isSuccessful) {
                Result.success(AuthResponse(
                    success = true,
                    message = "Login exitoso"
                ))
            } else {
                Result.failure(Exception("Error en el login"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun register(email: String, password: String, photoUri: Uri?): Result<AuthResponse> {
        return try {
            val response = apiService.register(RegisterRequest(email, password))
            if (response.isSuccessful) {
                Result.success(AuthResponse(
                    success = true,
                    message = "Registro exitoso"
                ))
            } else {
                Result.failure(Exception("Error en el registro"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
} 