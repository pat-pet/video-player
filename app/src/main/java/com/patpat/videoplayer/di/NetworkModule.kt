package com.patpat.videoplayer.di

import com.patpat.videoplayer.data.network.NetworkDataSource
import com.patpat.videoplayer.data.network.NetworkDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NetworkModule {

    @Binds
    @Singleton
    abstract fun providesNetworkDataSource(impl: NetworkDataSourceImpl): NetworkDataSource
}