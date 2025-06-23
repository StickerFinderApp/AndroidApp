package com.makniker.collections_presentation.sticker

import android.net.Uri

data class StickerUiModel(
    val id: Int,
    val uri: Uri,
    val name: String,
    val author: String
) 