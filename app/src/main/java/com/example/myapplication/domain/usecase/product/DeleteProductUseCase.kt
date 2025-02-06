package com.example.myapplication.domain.usecase.product

import com.example.myapplication.domain.repository.ProductRepository
import javax.inject.Inject

class DeleteProductUseCase @Inject constructor(
    private val productRepository: ProductRepository
) {
    suspend operator fun invoke(id: Int) = productRepository.deleteProduct(id)
} 