package com.makniker.collections_data

import com.makniker.collections_domain.CollectionRepository
import com.makniker.collections_domain.StickerDTO
import com.makniker.core.StickerDao
import com.makniker.core.StickerEntity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class CollectionRepositoryImpl @Inject constructor(private val stickerDao: StickerDao) :
    CollectionRepository {
    override suspend fun fetchSavedCollection(): Result<List<StickerDTO>> = runCatching {
        stickerDao.getAll().map {
            StickerDTO(
                id = it.id,
                uri = it.imageUri,
                name = it.name,
                author = it.author,
            )
        }
    }

    override suspend fun addSticker(sticker: StickerDTO) = stickerDao.insert(
        StickerEntity(
            imageUri = sticker.uri,
            name = sticker.name,
            author = sticker.author,
            creationDate = SimpleDateFormat(
                "yyyyMMdd_HHmmss", Locale.getDefault()
            ).format(Date()),
        )
    )

    override suspend fun getSticker(id: Int): Result<StickerDTO> = runCatching {
        val list = stickerDao.getAll().filter { it.id == id }
        list.map {
            StickerDTO(
                id = it.id,
                uri = it.imageUri,
                name = it.name,
                author = it.author,
            )
        }[0]
    }
}