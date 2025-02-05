package com.example.myapplication

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.data.api.RetrofitClient
import com.example.myapplication.data.model.Product
import com.example.myapplication.data.model.LoginRequest
import com.example.myapplication.data.model.RegisterRequest
import com.example.myapplication.ui.theme.MyApplicationTheme
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MainActivity : ComponentActivity() {
    private var isLoggedIn by mutableStateOf(false)
    private var isRegistering by mutableStateOf(false)
    private var showAddDialog by mutableStateOf(false)
    private var showEditDialog by mutableStateOf(false)
    private var productToEdit by mutableStateOf<Product?>(null)
    private var refreshTrigger by mutableStateOf(0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                when {
                    isLoggedIn -> {
                        ProductsScreen(
                            onAddClick = { showAddDialog = true },
                            onEditClick = { product ->
                                productToEdit = product
                                showEditDialog = true
                            },
                            onDeleteClick = { product ->
                                handleDeleteProduct(product)
                            },
                            refreshTrigger = refreshTrigger
                        )

                        if (showAddDialog) {
                            ProductDialog(
                                product = null,
                                onDismiss = { showAddDialog = false },
                                onConfirm = { nombre, precio, stock, categoria ->
                                    handleAddProduct(nombre, precio, stock, categoria)
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
                                    handleEditProduct(
                                        productToEdit!!.copy(
                                            nombre = nombre,
                                            precio = precio,
                                            stock = stock,
                                            categoria = categoria
                                        )
                                    )
                                    showEditDialog = false
                                    productToEdit = null
                                }
                            )
                        }
                    }
                    isRegistering -> {
                        RegisterScreen(
                            onRegisterClick = { email, password ->
                                handleRegister(email, password)
                            },
                            onBackClick = {
                                isRegistering = false
                            }
                        )
                    }
                    else -> {
                        LoginScreen(
                            onLoginClick = { email, password ->
                                handleLogin(email, password)
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

    private fun handleLogin(email: String, password: String) {
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.apiService.login(LoginRequest(email, password))
                if (response.isSuccessful && response.body()?.success == true) {
                    isLoggedIn = true
                    Toast.makeText(this@MainActivity, "Login exitoso", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@MainActivity, "Error en el login", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@MainActivity, "Error de conexión: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun handleRegister(email: String, password: String) {
        lifecycleScope.launch {
            try {
                Log.d("RegisterAPI", "Intentando registro con email: $email")
                val response = RetrofitClient.apiService.register(RegisterRequest(email, password))
                
                Log.d("RegisterAPI", "Respuesta recibida: ${response.code()}")
                Log.d("RegisterAPI", "Cuerpo de respuesta: ${response.body()}")
                
                if (response.isSuccessful) {
                    val registerResponse = response.body()
                    if (registerResponse?.success == true) {
                        Log.d("RegisterAPI", "Registro exitoso: ${registerResponse.message}")
                        Toast.makeText(this@MainActivity, registerResponse.message, Toast.LENGTH_SHORT).show()
                        isRegistering = false
                    }
                } else {
                    Log.e("RegisterAPI", "Error en registro: ${response.errorBody()?.string()}")
                    Toast.makeText(this@MainActivity, "Error en el registro", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e("RegisterAPI", "Error de conexión: ${e.message}", e)
                Toast.makeText(this@MainActivity, "Error de conexión: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun handleEditProduct(product: Product) {
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.apiService.updateProduct(product.id, product)
                
                if (response.isSuccessful) {
                    Toast.makeText(this@MainActivity, "Producto actualizado", Toast.LENGTH_SHORT).show()
                    refreshTrigger += 1
                } else {
                    Toast.makeText(this@MainActivity, "Error al actualizar producto", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@MainActivity, "Error de conexión", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun handleDeleteProduct(product: Product) {
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.apiService.deleteProduct(product.id)
                
                if (response.isSuccessful) {
                    Toast.makeText(this@MainActivity, "Producto eliminado", Toast.LENGTH_SHORT).show()
                    refreshTrigger += 1
                } else {
                    Toast.makeText(this@MainActivity, "Error al eliminar producto", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@MainActivity, "Error de conexión", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun handleAddProduct(nombre: String, precio: Double, stock: Int, categoria: String) {
        lifecycleScope.launch {
            try {
                val newProduct = Product(0, nombre, precio, stock, categoria)
                val response = RetrofitClient.apiService.createProduct(newProduct)
                
                if (response.isSuccessful) {
                    Toast.makeText(this@MainActivity, "Producto agregado", Toast.LENGTH_SHORT).show()
                    refreshTrigger += 1
                } else {
                    Toast.makeText(this@MainActivity, "Error al agregar producto", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@MainActivity, "Error de conexión", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
