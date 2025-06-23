package com.makniker.core.ui.components

import android.net.Uri
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import coil.request.ImageRequest

@Composable
fun StickerImage(modifier: Modifier, uri: Uri) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current).data(uri).crossfade(true).build(),
        contentDescription = "Loaded image",
        modifier = modifier.fillMaxWidth(0.8f),
        contentScale = ContentScale.FillWidth,
    )
}