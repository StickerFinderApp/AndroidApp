package com.makniker.collections_data

import com.makniker.collections_domain.CollectionRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface CollectionModule {
    @Binds
    fun bindCollectionRepository(impl: CollectionRepositoryImpl): CollectionRepository
}