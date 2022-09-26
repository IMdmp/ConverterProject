package com.imdmp.converter.base.di

import com.imdmp.converter.usecase.ConvertCurrencyUseCase
import com.imdmp.converter.usecase.FirstTimeSetupUseCase
import com.imdmp.converter.usecase.GetAvailableCurrenciesUseCase
import com.imdmp.converter.usecase.GetWalletBalanceUseCase
import com.imdmp.converter.usecase.PullRatesAndSaveCurrenciesUseCase
import com.imdmp.converter.usecase.impl.ConvertCurrencyUseCaseImpl
import com.imdmp.converter.usecase.impl.FirstTimeSetupUseCaseImpl
import com.imdmp.converter.usecase.impl.GetAvailableCurrenciesUseCaseImpl
import com.imdmp.converter.usecase.impl.GetWalletBalanceUseCaseImpl
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

    @Binds
    abstract fun bindGetAvailableCurrenciesUseCase(
        getAvailableCurrenciesUseCaseImpl: GetAvailableCurrenciesUseCaseImpl
    ): GetAvailableCurrenciesUseCase

    @Binds
    abstract fun bindFirstTimeSetupUseCase(
        firstTimeSetupUseCase: FirstTimeSetupUseCaseImpl
    ): FirstTimeSetupUseCase

    @Binds
    abstract fun bindGetWalletBalanceUseCase(
        getWalletBalanceUseCase: GetWalletBalanceUseCaseImpl
    ): GetWalletBalanceUseCase
}