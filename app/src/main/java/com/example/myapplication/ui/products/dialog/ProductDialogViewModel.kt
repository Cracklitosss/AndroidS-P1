package com.example.myapplication.ui.products.dialog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.model.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ProductDialogViewModel @Inject constructor() : ViewModel() {
    private val _state = MutableStateFlow(ProductDialogState())
    val state: StateFlow<ProductDialogState> = _state

    fun initializeWithProduct(product: Product?) {
        product?.let {
            _state.value = ProductDialogState(
                nombre = it.nombre,
                precio = it.precio.toString(),
                stock = it.stock.toString(),
                categoria = it.categoria
            )
        }
    }

    fun updateNombre(nombre: String) {
        _state.update { it.copy(nombre = nombre) }
    }

    fun updatePrecio(precio: String) {
        _state.update { it.copy(precio = precio) }
    }

    fun updateStock(stock: String) {
        _state.update { it.copy(stock = stock) }
    }

    fun updateCategoria(categoria: String) {
        _state.update { it.copy(categoria = categoria) }
    }

    fun validateInputs(): Boolean {
        if (_state.value.nombre.isBlank()) {
            _state.update { it.copy(error = "El nombre es requerido") }
            return false
        }

        val precio = _state.value.precio.toDoubleOrNull()
        if (precio == null || precio <= 0) {
            _state.update { it.copy(error = "El precio debe ser un número mayor a 0") }
            return false
        }

        val stock = _state.value.stock.toIntOrNull()
        if (stock == null || stock < 0) {
            _state.update { it.copy(error = "El stock debe ser un número mayor o igual a 0") }
            return false
        }

        if (_state.value.categoria.isBlank()) {
            _state.update { it.copy(error = "La categoría es requerida") }
            return false
        }

        return true
    }

    fun getProductValues(): Triple<String, Double, Int> {
        return Triple(
            _state.value.nombre,
            _state.value.precio.toDoubleOrNull() ?: 0.0,
            _state.value.stock.toIntOrNull() ?: 0
        )
    }

    fun clearError() {
        _state.update { it.copy(error = null) }
    }

    fun resetState() {
        _state.value = ProductDialogState()
    }
} 