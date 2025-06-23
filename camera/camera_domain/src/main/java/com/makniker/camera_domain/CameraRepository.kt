package com.makniker.camera_domain

import android.graphics.Bitmap
import android.net.Uri

interface CameraRepository {
    suspend fun saveStickerToDisc(sticker: Bitmap): Result<Uri>
}