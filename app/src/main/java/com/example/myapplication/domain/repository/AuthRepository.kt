package com.example.myapplication.domain.repository

import android.net.Uri
import com.example.myapplication.domain.model.AuthResponse

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<AuthResponse>
    suspend fun register(email: String, password: String, photoUri: Uri?): Result<AuthResponse>
} 