package com.example.myapplication.domain.usecase.product

import com.example.myapplication.data.model.Product
import com.example.myapplication.domain.repository.ProductRepository
import javax.inject.Inject

class CreateProductUseCase @Inject constructor(
    private val productRepository: ProductRepository
) {
    suspend operator fun invoke(nombre: String, precio: Double, stock: Int, categoria: String): Result<Product> {
        // Validaciones
        if (nombre.isBlank()) {
            return Result.failure(IllegalArgumentException("El nombre no puede estar vacío"))
        }
        if (precio <= 0) {
            return Result.failure(IllegalArgumentException("El precio debe ser mayor a 0"))
        }
        if (stock < 0) {
            return Result.failure(IllegalArgumentException("El stock no puede ser negativo"))
        }
        if (categoria.isBlank()) {
            return Result.failure(IllegalArgumentException("La categoría no puede estar vacía"))
        }

        val product = Product(0, nombre, precio, stock, categoria)
        return productRepository.createProduct(product)
    }
} 