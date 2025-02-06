package com.example.myapplication.data.model

data class LoginResponse(
    val success: Boolean,
    val message: String,
    val token: String? = null
)

data class LoginRequest(
    val email: String,
    val password: String
)

data class RegisterResponse(
    val success: Boolean,
    val message: String,
    val token: String? = null
)

data class RegisterRequest(
    val email: String,
    val password: String
) 