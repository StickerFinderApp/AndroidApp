package com.makniker.core

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [StickerEntity::class], version = 1)
abstract class StickerAppDatabase: RoomDatabase() {
    abstract fun stickerDao(): StickerDao
}