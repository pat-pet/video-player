package com.patpat.videoplayer.di

import com.patpat.videoplayer.data.repository.ContentVideoRepositoryImpl
import com.patpat.videoplayer.domain.repository.ContentVideoRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun providesContentVideoRepository(impl: ContentVideoRepositoryImpl): ContentVideoRepository
}