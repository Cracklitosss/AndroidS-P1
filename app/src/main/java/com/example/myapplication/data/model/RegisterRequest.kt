package com.example.myapplication.data.model

data class RegisterRequest(
    val email: String,
    val password: String
)

data class RegisterResponse(
    val success: Boolean,
    val message: String
) 