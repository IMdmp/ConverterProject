package com.imdmp.converter.base.di

import com.imdmp.converter.repository.ConverterRepository
import com.imdmp.converter.repository.impl.ConverterRepositoryImpl
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