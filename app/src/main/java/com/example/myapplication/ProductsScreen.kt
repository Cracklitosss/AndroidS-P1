package com.example.myapplication

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import com.example.myapplication.data.api.RetrofitClient
import com.example.myapplication.data.model.Product
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductsScreen(
    onAddClick: () -> Unit,
    onEditClick: (Product) -> Unit,
    onDeleteClick: (Product) -> Unit,
    refreshTrigger: Int = 0
) {
    var products by remember { mutableStateOf<List<Product>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    
    LaunchedEffect(refreshTrigger) {
        try {
            val response = RetrofitClient.apiService.getProducts()
            if (response.isSuccessful) {
                products = response.body() ?: emptyList()
            }
        } finally {
            isLoading = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Productos") },
                actions = {
                    IconButton(onClick = onAddClick) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Agregar producto"
                        )
                    }
                }
            )
        }
    ) { padding ->
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                items(products) { product ->
                    ProductItem(
                        product = product,
                        onEditClick = { onEditClick(product) },
                        onDeleteClick = { 
                            onDeleteClick(product)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ProductItem(
    product: Product,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(
                text = product.nombre,
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = "Precio: $${product.precio}",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "Stock: ${product.stock}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Categoría: ${product.categoria}",
                style = MaterialTheme.typography.bodyMedium
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = onEditClick) {
                    Text("Editar")
                }
                TextButton(onClick = onDeleteClick) {
                    Text("Eliminar")
                }
            }
        }
    }
} 