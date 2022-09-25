package com.imdmp.converter.di

import com.imdmp.converter.usecase.ConvertCurrencyUseCase
import com.imdmp.converter.usecase.PullRatesAndSaveCurrenciesUseCase
import com.imdmp.converter.usecase.impl.ConvertCurrencyUseCaseImpl
import com.imdmp.converter.usecase.impl.PullRatesAndSaveCurrenciesUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class) // todo:change component later
abstract class UseCaseModule {

    @Binds
    abstract fun providePullRatesAndSaveCurrenciesUseCase(
        pullRatesAndSaveCurrenciesUseCaseImpl: PullRatesAndSaveCurrenciesUseCaseImpl
    ): PullRatesAndSaveCurrenciesUseCase

    @Binds
    abstract fun bindConvertCurrencyUseCase(
        convertCurrencyUseCase: ConvertCurrencyUseCaseImpl
    ): ConvertCurrencyUseCase
}