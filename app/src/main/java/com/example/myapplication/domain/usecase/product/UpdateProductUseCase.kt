package com.example.myapplication.domain.usecase.product

import com.example.myapplication.data.model.Product
import com.example.myapplication.domain.repository.ProductRepository
import javax.inject.Inject

class UpdateProductUseCase @Inject constructor(
    private val productRepository: ProductRepository
) {
    suspend operator fun invoke(product: Product): Result<Product> {
        // Validaciones
        if (product.nombre.isBlank()) {
            return Result.failure(IllegalArgumentException("El nombre no puede estar vacío"))
        }
        if (product.precio <= 0) {
            return Result.failure(IllegalArgumentException("El precio debe ser mayor a 0"))
        }
        if (product.stock < 0) {
            return Result.failure(IllegalArgumentException("El stock no puede ser negativo"))
        }
        if (product.categoria.isBlank()) {
            return Result.failure(IllegalArgumentException("La categoría no puede estar vacía"))
        }

        return productRepository.updateProduct(product)
    }
} 