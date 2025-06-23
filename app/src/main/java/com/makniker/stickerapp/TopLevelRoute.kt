package com.makniker.stickerapp

import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.serialization.Serializable

object NavRoutes {
    const val CAMERA_FLOW = "camera_flow"
    const val DETAILS_SCREEN = "details_screen/{stickerId}"

    fun detailsScreen(stickerId: Int) = "details_screen/$stickerId"
}
