package com.example.myapplication.domain.repository

import com.example.myapplication.data.model.Product

interface ProductRepository {
    suspend fun getProducts(): Result<List<Product>>
    suspend fun createProduct(product: Product): Result<Product>
    suspend fun updateProduct(product: Product): Result<Product>
    suspend fun deleteProduct(id: Int): Result<Unit>
} 