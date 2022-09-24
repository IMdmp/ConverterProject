package com.imdmp.converter.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@InstallIn(SingletonComponent::class)
@Module
class ApplicationModule {

    @Provides
    fun provideDefaultDispatcher(): CoroutineDispatcher {
        return Dispatchers.IO
    }
}