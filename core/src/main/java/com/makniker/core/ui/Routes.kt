package com.makniker.core.ui

import android.net.Uri

object Routes {
    const val COLLECTION = "collection"
    const val CAMERA = "camera"
    const val STICKER_SCREEN = "sticker_screen?id={id}"
    const val ADD_STICKER_ARG = "add_sticker?uri={uri}"
    fun addStickerRoute(uri: Uri): String = "add_sticker?uri=${Uri.encode(uri.toString())}"
    fun addStickerScreenRoute(id: Int): String = "add_sticker?id=${id}"
}