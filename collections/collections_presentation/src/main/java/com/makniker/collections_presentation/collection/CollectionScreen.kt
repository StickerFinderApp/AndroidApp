package com.makniker.collections_presentation.collection

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
 import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.makniker.core.ui.UiState
import com.makniker.core.ui.components.ErrorScreen
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.Menu
import androidx.navigation.NavHostController
import com.makniker.core.ui.components.LoadingScreen
import com.makniker.core.ui.components.StickerImage

@Composable
fun CollectionScreen(
    modifier: Modifier,
    navController: NavHostController,
    collectionViewModel: CollectionViewModel,
    onItemClicked: (id: Int) -> Unit,
    onAddItemClicked: () -> Unit,
    onGalleryItemClicked: () -> Unit
) {
    LaunchedEffect(Unit) {
        collectionViewModel.loadCollection()
    }

    val state = collectionViewModel.uiState.collectAsState()

    when (val it = state.value) {
        is UiState.Failure -> ErrorScreen(it.e.message ?: "Something gone wrong", modifier) {}
        is UiState.Loading -> LoadingScreen(modifier)
        is UiState.Success -> Content(modifier, it.data, onItemClicked, onAddItemClicked, onGalleryItemClicked)
    }
}

@Composable
private fun Content(
    modifier: Modifier,
    collection: List<StickerCollectionUiModel>,
    onItemClicked: (Int) -> Unit,
    onAddItemClicked: () -> Unit,
    onGalleryItemClicked: () -> Unit
) {
    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (collection.isEmpty()) {
                ContentEmpty(modifier)
            } else {
                ContentList(modifier, collection) { onItemClicked }
            }
        }

        Row(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Кнопка для выбора изображений из галереи
            FloatingActionButton(
                onClick = onGalleryItemClicked,
                modifier = Modifier.size(56.dp)
            ) {
                Icon(Icons.Default.Menu, "Выбрать из галереи")
            }
            
            // Кнопка для съемки фото
            FloatingActionButton(
                onClick = onAddItemClicked,
                modifier = Modifier.size(56.dp)
            ) {
                Icon(Icons.Default.Add, "Снять фото")
            }
        }
    }
}

@Composable
private fun ContentEmpty(modifier: Modifier) {
    Text(
        modifier = modifier,
        text = "Your collection is empty!\n Try to add something",
        textAlign = TextAlign.Center
    )
}

@Composable
private fun ContentList(
    modifier: Modifier,
    collection: List<StickerCollectionUiModel>,
    onItemClicked: (Int) -> Unit,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(8.dp)
    ) {
        items(collection) { item ->
            StickerImage(Modifier, item.uri)
        }
    }
}