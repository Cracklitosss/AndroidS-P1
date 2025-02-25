package com.example.myapplication.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.example.myapplication.R

class MyFirebaseMessagingService : FirebaseMessagingService() {
    
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG, "Nuevo token FCM: $token")
        // Aquí puedes enviar el token a tu servidor si es necesario
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.d(TAG, "Mensaje recibido desde: ${remoteMessage.from}")

        // Mostrar datos del mensaje para debugging
        remoteMessage.data.isNotEmpty().let {
            Log.d(TAG, "Datos del mensaje: ${remoteMessage.data}")
        }

        // Mostrar la notificación tanto si viene como notification o como data
        val title = remoteMessage.notification?.title 
            ?: remoteMessage.data["title"]
            ?: "Notificación"
            
        val message = remoteMessage.notification?.body 
            ?: remoteMessage.data["message"] 
            ?: remoteMessage.data["body"]
            ?: "Mensaje"

        showNotification(title, message)
    }

    private fun showNotification(title: String, message: String) {
        val channelId = "default_channel"
        val notificationId = System.currentTimeMillis().toInt()

        // Crear el canal de notificación para Android 8.0 y superiores
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Canal Principal",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Canal para notificaciones principales"
                enableLights(true)
                enableVibration(true)
            }
            
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        // Construir la notificación
        val builder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_info) // Cambia esto por tu propio ícono
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setVibrate(longArrayOf(0, 500, 200, 500))

        // Mostrar la notificación
        try {
            NotificationManagerCompat.from(this).notify(notificationId, builder.build())
            Log.d(TAG, "Notificación mostrada con éxito: $title - $message")
        } catch (e: SecurityException) {
            Log.e(TAG, "Error al mostrar la notificación: ${e.message}")
        }
    }

    companion object {
        private const val TAG = "FCMService"
    }
} 