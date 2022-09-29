package com.imdmp.converter.base.di

import com.imdmp.converter.usecase.CommissionCheckUseCase
import com.imdmp.converter.usecase.ConvertCurrencyUseCase
import com.imdmp.converter.usecase.ConvertUserWalletCurrencyUseCase
import com.imdmp.converter.usecase.FirstTimeSetupUseCase
import com.imdmp.converter.usecase.GetAvailableCurrenciesUseCase
import com.imdmp.converter.usecase.GetCommissionChargeUseCase
import com.imdmp.converter.usecase.GetWalletBalanceUseCase
import com.imdmp.converter.usecase.PullRatesAndSaveCurrenciesUseCase
import com.imdmp.converter.usecase.SaveTransactionUseCase
import com.imdmp.converter.usecase.UpdateWalletUseCase
import com.imdmp.converter.usecase.impl.CommissionCheckUseCaseImpl
import com.imdmp.converter.usecase.impl.ConvertCurrencyUseCaseImpl
import com.imdmp.converter.usecase.impl.ConvertUserWalletCurrencyUseCaseImpl
import com.imdmp.converter.usecase.impl.FirstTimeSetupUseCaseImpl
import com.imdmp.converter.usecase.impl.GetAvailableCurrenciesUseCaseImpl
import com.imdmp.converter.usecase.impl.GetCommissionChargeUseCaseImpl
import com.imdmp.converter.usecase.impl.GetWalletBalanceUseCaseImpl
import com.imdmp.converter.usecase.impl.PullRatesAndSaveCurrenciesUseCaseImpl
import com.imdmp.converter.usecase.impl.SaveTransactionUseCaseImpl
import com.imdmp.converter.usecase.impl.UpdateWalletUseCaseImpl
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

    @Binds
    abstract fun bindUpdateWalletUseCase(
        updateWalletUseCase: UpdateWalletUseCaseImpl
    ): UpdateWalletUseCase

    @Binds
    abstract fun bindCommissionCheckUseCase(
        commissionCheckUseCase: CommissionCheckUseCaseImpl
    ): CommissionCheckUseCase

    @Binds
    abstract fun bindConvertUserWalletCurrencyUseCase(
        convertUserWalletCurrencyUseCase: ConvertUserWalletCurrencyUseCaseImpl
    ): ConvertUserWalletCurrencyUseCase

    @Binds
    abstract fun bindGetCommissionChargeUseCase(
        getCommissionChargeUseCase: GetCommissionChargeUseCaseImpl
    ): GetCommissionChargeUseCase

    @Binds
    abstract fun bindSaveTransactionUseCase(
        saveTransactionUseCase: SaveTransactionUseCaseImpl
    ): SaveTransactionUseCase
}