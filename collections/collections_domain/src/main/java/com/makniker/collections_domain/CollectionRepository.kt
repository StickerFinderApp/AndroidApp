package com.makniker.collections_domain

interface CollectionRepository {
    suspend fun fetchSavedCollection(): Result<List<StickerDTO>>

    suspend fun addSticker(sticker: StickerDTO)

    suspend fun getSticker(id: Int): Result<StickerDTO>
}