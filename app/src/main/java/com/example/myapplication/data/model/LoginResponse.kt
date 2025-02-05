package com.example.myapplication.data.model

data class LoginResponse(
    val success: Boolean,
    val token: String?,
    val message: String
)

data class LoginRequest(
    val email: String,
    val password: String
) 