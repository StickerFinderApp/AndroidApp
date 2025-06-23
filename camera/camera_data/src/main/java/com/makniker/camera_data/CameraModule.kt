package com.makniker.camera_data

import com.makniker.camera_domain.CameraRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface CameraModule {
    @Binds
    fun bindCameraRepository(impl: CameraRepositoryImpl): CameraRepository
}