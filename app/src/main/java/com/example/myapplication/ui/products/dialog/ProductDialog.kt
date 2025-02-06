package com.example.myapplication.ui.products.dialog

import androidx.compose.material3.*
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myapplication.data.model.Product

@Composable
fun ProductDialog(
    product: Product? = null,
    onDismiss: () -> Unit,
    onConfirm: (String, Double, Int, String) -> Unit,
    viewModel: ProductDialogViewModel = hiltViewModel()
) {
    LaunchedEffect(product) {
        if (product == null) {
            viewModel.resetState()
        } else {
            viewModel.initializeWithProduct(product)
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            viewModel.resetState()
        }
    }

    val state by viewModel.state.collectAsState()

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (product == null) "Agregar Producto" else "Editar Producto") },
        text = {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = state.nombre,
                    onValueChange = { viewModel.updateNombre(it) },
                    label = { Text("Nombre") }
                )
                OutlinedTextField(
                    value = state.precio,
                    onValueChange = { viewModel.updatePrecio(it) },
                    label = { Text("Precio") }
                )
                OutlinedTextField(
                    value = state.stock,
                    onValueChange = { viewModel.updateStock(it) },
                    label = { Text("Stock") }
                )
                OutlinedTextField(
                    value = state.categoria,
                    onValueChange = { viewModel.updateCategoria(it) },
                    label = { Text("Categoría") }
                )

                state.error?.let { error ->
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (viewModel.validateInputs()) {
                        val (nombre, precio, stock) = viewModel.getProductValues()
                        onConfirm(nombre, precio, stock, state.categoria)
                    }
                }
            ) {
                Text(if (product == null) "Agregar" else "Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
} 