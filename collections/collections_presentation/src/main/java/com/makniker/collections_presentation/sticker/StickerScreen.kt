package com.makniker.collections_presentation.sticker

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.makniker.core.ui.UiState
import com.makniker.core.ui.components.ErrorScreen
import com.makniker.core.ui.components.LoadingScreen
import com.makniker.core.ui.components.StickerImage

@Composable
fun StickerScreen(
    modifier: Modifier = Modifier,
    viewModel: StickerViewModel,
    stickerId: Int
) {
    LaunchedEffect(stickerId) {
        viewModel.loadSticker(stickerId)
    }

    val state = viewModel.uiState.collectAsState()

    when (val it = state.value) {
        is UiState.Failure -> ErrorScreen(it.e.message ?: "Something gone wrong", modifier) {}
        is UiState.Loading -> LoadingScreen(modifier)
        is UiState.Success -> Content(modifier, it.data)
    }
}

@Composable
private fun Content(
    modifier: Modifier,
    sticker: StickerUiModel
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        StickerImage(modifier, sticker.uri)
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(text = sticker.name)
        Text(text = sticker.author)
    }
}