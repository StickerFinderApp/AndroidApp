package com.makniker.stickerapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.makniker.camera_presentation.CameraActivity
import com.makniker.collections_presentation.addsticker.AddStickerScreen
import com.makniker.collections_presentation.collection.CollectionScreen
import com.makniker.core.ui.Routes
import com.makniker.core.ui.theme.StickerAppTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private var isBackFromCamera = false
    private var isBackFromGallery = false
    private var uriString: String? = null
    private var galleryUri: Uri? = null
    
    private val cameraLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            uriString = result.data?.getStringExtra("captured_image_uri")
            isBackFromCamera = true
        }
    }
    
    private val galleryLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            galleryUri = uri
            isBackFromGallery = true
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
    }

    override fun onResume() {
        super.onResume()
        setContent {
            StickerAppTheme {
                val navController = rememberNavController()

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = Routes.COLLECTION,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        collectionGraph(
                            modifier = Modifier.padding(innerPadding),
                            cameraLauncher = cameraLauncher,
                            galleryLauncher = galleryLauncher,
                            navController = navController,
                        )
                        addStickerGraph(
                            modifier = Modifier.padding(innerPadding),
                            navController = navController,
                        )
                    }
                    
                    // Обработка результата от камеры
                    if (isBackFromCamera) {
                        uriString?.let {
                            navController.navigate(Routes.addStickerRoute(it.toUri()))
                            isBackFromCamera = false
                            uriString = null
                        }
                    }
                    
                    // Обработка результата от галереи
                    if (isBackFromGallery) {
                        galleryUri?.let {
                            navController.navigate(Routes.addStickerRoute(it))
                            isBackFromGallery = false
                            galleryUri = null
                        }
                    }
                }
            }
        }
    }
}
