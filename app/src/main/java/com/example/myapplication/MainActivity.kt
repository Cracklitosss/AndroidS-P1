package com.example.myapplication

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.data.model.Product
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.ui.auth.AuthState
import com.example.myapplication.ui.auth.AuthViewModel
import com.example.myapplication.ui.products.ProductsViewModel
import com.example.myapplication.ui.products.dialog.ProductDialog
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val authViewModel: AuthViewModel by viewModels()
    private val productsViewModel: ProductsViewModel by viewModels()
    
    private var isLoggedIn by mutableStateOf(false)
    private var isRegistering by mutableStateOf(false)
    private var showAddDialog by mutableStateOf(false)
    private var showEditDialog by mutableStateOf(false)
    private var productToEdit by mutableStateOf<Product?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Solicitar permisos de notificaciones en Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestNotificationPermission()
        }
        
        // Obtener token de FCM
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val token = task.result
                Log.d("FCM_TOKEN", "Token: $token")
                // Opcional: Mostrar el token en un Toast para pruebas
                Toast.makeText(this, "FCM Token: $token", Toast.LENGTH_LONG).show()
            } else {
                Log.e("FCM_TOKEN", "Error al obtener token", task.exception)
            }
        }
        
        // Observar estados de autenticación
        lifecycleScope.launch {
            authViewModel.authState.collect { state ->
                when (state) {
                    is AuthState.Success -> {
                        isLoggedIn = true
                        Toast.makeText(this@MainActivity, state.message, Toast.LENGTH_SHORT).show()
                    }
                    is AuthState.Error -> {
                        Toast.makeText(this@MainActivity, state.message, Toast.LENGTH_SHORT).show()
                    }
                    else -> {}
                }
            }
        }

        setContent {
            MyApplicationTheme {
                when {
                    isLoggedIn -> {
                        ProductsScreen(
                            viewModel = productsViewModel,
                            onAddClick = { showAddDialog = true },
                            onEditClick = { product: Product ->
                                productToEdit = product
                                showEditDialog = true
                            },
                            onDeleteClick = { product: Product ->
                                handleDeleteProduct(product)
                            }
                        )

                        if (showAddDialog) {
                            ProductDialog(
                                product = null,
                                onDismiss = { 
                                    showAddDialog = false
                                },
                                onConfirm = { nombre, precio, stock, categoria ->
                                    productsViewModel.addProduct(nombre, precio, stock, categoria)
                                    showAddDialog = false
                                }
                            )
                        }

                        if (showEditDialog && productToEdit != null) {
                            ProductDialog(
                                product = productToEdit,
                                onDismiss = { 
                                    showEditDialog = false
                                    productToEdit = null
                                },
                                onConfirm = { nombre, precio, stock, categoria ->
                                    productToEdit?.let { product ->
                                        productsViewModel.updateProduct(
                                            product.copy(
                                                nombre = nombre,
                                                precio = precio,
                                                stock = stock,
                                                categoria = categoria
                                            )
                                        )
                                    }
                                    showEditDialog = false
                                    productToEdit = null
                                }
                            )
                        }
                    }
                    isRegistering -> {
                        RegisterScreen(
                            onRegisterClick = { email, password, photoUri ->
                                authViewModel.register(email, password, photoUri)
                            },
                            onBackClick = {
                                isRegistering = false
                            }
                        )
                    }
                    else -> {
                        LoginScreen(
                            onLoginClick = { email, password ->
                                authViewModel.login(email, password)
                            },
                            onRegisterClick = {
                                isRegistering = true
                            }
                        )
                    }
                }
            }
        }
    }

    private fun handleDeleteProduct(product: Product) {
        productsViewModel.deleteProduct(product.id)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun requestNotificationPermission() {
        val permission = Manifest.permission.POST_NOTIFICATIONS
        if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(permission), 100)
        }
    }
}
