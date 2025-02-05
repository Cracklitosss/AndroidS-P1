package com.example.myapplication.data.api

import com.example.myapplication.data.model.LoginRequest
import com.example.myapplication.data.model.LoginResponse
import com.example.myapplication.data.model.RegisterRequest
import com.example.myapplication.data.model.RegisterResponse
import com.example.myapplication.data.model.Product
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @POST("api/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>
    
    @POST("api/registro")
    suspend fun register(@Body registerRequest: RegisterRequest): Response<RegisterResponse>
    
    @GET("api/productos")
    suspend fun getProducts(): Response<List<Product>>
    
    @POST("api/productos")
    suspend fun createProduct(@Body product: Product): Response<Product>
    
    @PUT("api/productos/{id}")
    suspend fun updateProduct(@Path("id") id: Int, @Body product: Product): Response<Product>
    
    @DELETE("api/productos/{id}")
    suspend fun deleteProduct(@Path("id") id: Int): Response<Unit>
}