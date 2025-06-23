package com.makniker.core

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CoreModule {

    @Singleton
    @Provides
    fun provideRoomDatabase(@ApplicationContext appContext: Context): StickerAppDatabase = Room.databaseBuilder(
        appContext, StickerAppDatabase::class.java, "database-name").build()

    @Singleton
    @Provides
    fun provideStickerDao(database: StickerAppDatabase): StickerDao = database.stickerDao()

    @Singleton
    @Provides
    fun provideFileManager(@ApplicationContext appContext: Context): FileManager = FileManager(appContext)
}