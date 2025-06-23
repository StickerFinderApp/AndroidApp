package com.makniker.stickerapp

import android.content.Intent
import android.net.Uri
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.makniker.camera_presentation.CameraActivity
import com.makniker.collections_presentation.addsticker.AddStickerScreen
import com.makniker.collections_presentation.collection.CollectionScreen
import com.makniker.core.ui.Routes

fun NavGraphBuilder.collectionGraph(
    modifier: Modifier,
    cameraLauncher: ActivityResultLauncher<Intent>,
    galleryLauncher: ActivityResultLauncher<String>,
    navController: NavHostController
) {
    composable(Routes.COLLECTION) {
        val context = LocalContext.current
        CollectionScreen(
            modifier = modifier,
            navController = navController,
            collectionViewModel = hiltViewModel(),
            onAddItemClicked = {
                val intent = Intent(context, CameraActivity::class.java)
                cameraLauncher.launch(intent)
            },
            onGalleryItemClicked = {
                galleryLauncher.launch("image/*")
            },
            onItemClicked = { })
    }
}

fun NavGraphBuilder.addStickerGraph(modifier: Modifier, navController: NavHostController) {
    composable(
        Routes.ADD_STICKER_ARG, arguments = listOf(
            navArgument("uri") { type = NavType.StringType; })
    ) { backStackEntry ->
        val uriString = backStackEntry.arguments?.getString("uri")
        val uri = (uriString ?: "").toUri()
        AddStickerScreen(
            modifier = modifier,
            navController = navController,
            addStickerViewModel = hiltViewModel(),
            stickerUri = uri
        )
    }
}

fun NavGraphBuilder.stickerScreenGraph(modifier: Modifier, navController: NavHostController) {
    composable(
        Routes.STICKER_SCREEN, arguments = listOf(
            navArgument("id") { type = NavType.StringType; })
    ) { backStackEntry ->
        val idString = backStackEntry.arguments?.getString("uri")
        val id = (idString ?: "1").toInt()
//        AddStickerScreen(
//            modifier = modifier,
//            navController = navController,
//            addStickerViewModel = hiltViewModel(),
//            stickerUri = uri
//        )
    }
}
