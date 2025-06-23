package com.makniker.core

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "stickers")
data class StickerEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val creationDate: String,
    val imageUri: String,
    val name: String,
    val author: String,
)