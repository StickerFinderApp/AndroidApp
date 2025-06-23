package com.makniker.core

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface StickerDao {

    @Query("SELECT * FROM stickers")
    fun getAll(): List<StickerEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @JvmSuppressWildcards
    suspend fun insert(sticker: StickerEntity)
}