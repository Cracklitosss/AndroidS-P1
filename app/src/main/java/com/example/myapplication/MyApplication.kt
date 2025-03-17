package com.example.myapplication

import android.app.Application
import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication : Application() {
    
    override fun onCreate() {
        super.onCreate()
        
        // LeakCanary se inicializa automáticamente, no necesita configuración adicional
        // en versiones recientes (2.x)
        Log.d("MyApplication", "LeakCanary se inicializa automáticamente")
        
        // Obtener token FCM al inicio de la aplicación
        getFirebaseToken()
    }
    
    private fun getFirebaseToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val token = task.result
                Log.d("MyApplication", "FCM Token obtenido en Application: $token")
            } else {
                Log.e("MyApplication", "Error al obtener FCM Token en Application", task.exception)
            }
        }
    }
} 