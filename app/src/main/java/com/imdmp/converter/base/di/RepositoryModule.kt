package com.imdmp.converter.base.di

import com.imdmp.converter.repository.network.ConverterRepository
import com.imdmp.converter.repository.network.impl.ConverterRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
abstract class RepositoryModule {

    @Binds
    abstract fun provideConverterRepository(
        converterRepository: ConverterRepositoryImpl
    )
        : ConverterRepository

}