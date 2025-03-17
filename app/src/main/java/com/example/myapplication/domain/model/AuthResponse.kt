package com.example.myapplication.domain.model

data class AuthResponse(
    val success: Boolean,
    val message: String,
    val token: String? = null
) 