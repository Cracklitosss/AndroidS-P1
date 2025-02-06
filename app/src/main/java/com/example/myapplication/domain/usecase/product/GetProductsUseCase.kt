package com.example.myapplication.domain.usecase.product

import com.example.myapplication.domain.repository.ProductRepository
import javax.inject.Inject

class GetProductsUseCase @Inject constructor(
    private val productRepository: ProductRepository
) {
    suspend operator fun invoke() = productRepository.getProducts()
} 