package com.example.myapplication.utils

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File

class ComposeFileProvider : FileProvider() {
    companion object {
        fun getImageUri(context: Context): Uri {
            val directory = File(context.cacheDir, "images").apply {
                mkdirs()
            }
            val file = File.createTempFile(
                "camera_photo_",
                ".jpg",
                directory
            )
            val authority = "${context.packageName}.fileprovider"
            return getUriForFile(
                context,
                authority,
                file
            )
        }
    }
} 