package com.example.myapplication.ui.components

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest

@Composable
fun CameraPreview(
    photoUri: Uri?,
    onPhotoClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (photoUri != null) {
        Image(
            painter = rememberAsyncImagePainter(
                ImageRequest.Builder(LocalContext.current)
                    .data(data = photoUri)
                    .crossfade(true)
                    .size(width = 120, height = 120)
                    .build()
            ),
            contentDescription = "Foto de perfil",
            modifier = modifier
                .size(120.dp)
                .clip(CircleShape)
                .clickable(onClick = onPhotoClick),
            contentScale = ContentScale.Crop
        )
    } else {
        Icon(
            imageVector = Icons.Default.Person,
            contentDescription = "Agregar foto",
            modifier = modifier
                .size(120.dp)
                .clip(CircleShape)
                .clickable(onClick = onPhotoClick)
        )
    }
} 