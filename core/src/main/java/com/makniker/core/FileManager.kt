package com.makniker.core

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class FileManager @Inject constructor(
    private val context: Context
) {
    fun saveBitmap(bitmap: Bitmap): Result<Uri> {
        return try {
            val file = createImageFile()
            FileOutputStream(file).use { out ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
            }
            Result.success(getUriForFile(file))
        } catch (e: IOException) {
            Result.failure(e)
        } catch (e: SecurityException) {
            Result.failure(e)
        }
    }

    private fun createImageFile(): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)?.apply {
            if (!exists()) {
                mkdirs()
            }
        }

        return File.createTempFile(
            "STICKER_${timeStamp}_", ".jpg", storageDir
        )
    }

    private fun getUriForFile(file: File): Uri {
        if (!file.exists()) {
            throw FileNotFoundException("File not found: ${file.absolutePath}")
        }
        Log.d("FileManager", "File path: ${file.absolutePath}")
        return Uri.fromFile(file)
    }
}