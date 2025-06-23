package com.makniker.camera_data

import android.graphics.Bitmap
import android.net.Uri
import com.makniker.camera_domain.CameraRepository
import com.makniker.core.FileManager
import com.makniker.core.StickerDao
import javax.inject.Inject
import kotlin.Result

class CameraRepositoryImpl @Inject constructor(private val fileManager: FileManager, private val stickerDao: StickerDao): CameraRepository {
    override suspend fun saveStickerToDisc(sticker: Bitmap): Result<Uri> {
        return fileManager.saveBitmap(sticker)
    }
}