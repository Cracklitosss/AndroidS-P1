package com.example.myapplication

import androidx.compose.material3.*
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.myapplication.data.model.Product

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDialog(
    product: Product? = null,  // null para agregar, Product para editar
    onDismiss: () -> Unit,
    onConfirm: (String, Double, Int, String) -> Unit
) {
    var nombre by remember { mutableStateOf(product?.nombre ?: "") }
    var precio by remember { mutableStateOf(product?.precio?.toString() ?: "") }
    var stock by remember { mutableStateOf(product?.stock?.toString() ?: "") }
    var categoria by remember { mutableStateOf(product?.categoria ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (product == null) "Agregar Producto" else "Editar Producto") },
        text = {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre") }
                )
                OutlinedTextField(
                    value = precio,
                    onValueChange = { precio = it },
                    label = { Text("Precio") }
                )
                OutlinedTextField(
                    value = stock,
                    onValueChange = { stock = it },
                    label = { Text("Stock") }
                )
                OutlinedTextField(
                    value = categoria,
                    onValueChange = { categoria = it },
                    label = { Text("Categoría") }
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm(
                        nombre,
                        precio.toDoubleOrNull() ?: 0.0,
                        stock.toIntOrNull() ?: 0,
                        categoria
                    )
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