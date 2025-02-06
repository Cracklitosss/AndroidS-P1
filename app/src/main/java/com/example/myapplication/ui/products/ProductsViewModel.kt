package com.example.myapplication.ui.products

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.model.Product
import com.example.myapplication.domain.usecase.product.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor(
    private val getProductsUseCase: GetProductsUseCase,
    private val createProductUseCase: CreateProductUseCase,
    private val updateProductUseCase: UpdateProductUseCase,
    private val deleteProductUseCase: DeleteProductUseCase
) : ViewModel() {
    private val _productsState = MutableStateFlow<ProductsState>(ProductsState.Loading)
    val productsState: StateFlow<ProductsState> = _productsState

    init {
        loadProducts()
    }

    fun loadProducts() {
        viewModelScope.launch {
            _productsState.value = ProductsState.Loading
            getProductsUseCase().fold(
                onSuccess = { products ->
                    _productsState.value = ProductsState.Success(products)
                },
                onFailure = {
                    _productsState.value = ProductsState.Error(it.message ?: "Error desconocido")
                }
            )
        }
    }

    fun addProduct(nombre: String, precio: Double, stock: Int, categoria: String) {
        viewModelScope.launch {
            createProductUseCase(nombre, precio, stock, categoria).fold(
                onSuccess = {
                    loadProducts()
                },
                onFailure = {
                    _productsState.value = ProductsState.Error(it.message ?: "Error al crear producto")
                }
            )
        }
    }

    fun updateProduct(product: Product) {
        viewModelScope.launch {
            updateProductUseCase(product).fold(
                onSuccess = {
                    loadProducts()
                },
                onFailure = {
                    _productsState.value = ProductsState.Error(it.message ?: "Error al actualizar producto")
                }
            )
        }
    }

    fun deleteProduct(id: Int) {
        viewModelScope.launch {
            deleteProductUseCase(id).fold(
                onSuccess = {
                    loadProducts()
                },
                onFailure = {
                    _productsState.value = ProductsState.Error(it.message ?: "Error al eliminar producto")
                }
            )
        }
    }
}

sealed class ProductsState {
    object Loading : ProductsState()
    data class Success(val products: List<Product>) : ProductsState()
    data class Error(val message: String) : ProductsState()
} 