package com.example.myapplication.ui.products.dialog

data class ProductDialogState(
    val nombre: String = "",
    val precio: String = "",
    val stock: String = "",
    val categoria: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
) 