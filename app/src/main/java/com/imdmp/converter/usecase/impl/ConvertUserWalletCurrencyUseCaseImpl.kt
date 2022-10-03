package com.imdmp.converter.usecase.impl

import com.imdmp.converter.BuildConfig
import com.imdmp.converter.base.di.NameAnnotationConstants
import com.imdmp.converter.repository.ConverterRepository
import com.imdmp.converter.schema.ConvertUserWalletCurrencyError
import com.imdmp.converter.schema.ConvertUserWalletResultSchema
import com.imdmp.converter.schema.TransactionSchema
import com.imdmp.converter.schema.WalletSchema
import com.imdmp.converter.usecase.ConvertUserWalletCurrencyUseCase
import com.imdmp.converter.usecase.GetCommissionChargeUseCase
import com.imdmp.converter.usecase.SaveTransactionUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject
import javax.inject.Named

class ConvertUserWalletCurrencyUseCaseImpl @Inject constructor(
    private val converterRepository: ConverterRepository,
    @Named(NameAnnotationConstants.FREE_UP_TO_5_TRANSACTIONS)
    private val getCommissionChargeUseCase: GetCommissionChargeUseCase,
    private val saveTransactionUseCase: SaveTransactionUseCase
): ConvertUserWalletCurrencyUseCase {
    override suspend fun invoke(
        sellWalletSchema: WalletSchema,
        buyWalletSchema: WalletSchema
    ): Flow<ConvertUserWalletResultSchema> {
        return flow {
            val userSellBalance =
                converterRepository.getWalletBalance(sellWalletSchema.currencyAbbrev)
                    ?: WalletSchema(
                        sellWalletSchema.currencyAbbrev,
                        0.0
                    )
            val userBuyBalance =
                converterRepository.getWalletBalance(buyWalletSchema.currencyAbbrev)
                    ?: WalletSchema(
                        buyWalletSchema.currencyAbbrev,
                        0.0
                    )

            if (BuildConfig.MOCK_RESPONSE) {
                delay(2000)
            }

            if (userSellBalance.currencyValue < sellWalletSchema.currencyValue) {
                emit(ConvertUserWalletResultSchema.Error(ConvertUserWalletCurrencyError.NotEnoughBalance))
                return@flow
            }

            val commissionCharge = getCommissionChargeUseCase(sellWalletSchema)
            val newSellValue = userSellBalance.currencyValue -
                sellWalletSchema.currencyValue -
                commissionCharge

            if (newSellValue < 0) {
                emit(ConvertUserWalletResultSchema.Error(ConvertUserWalletCurrencyError.NotEnoughBalanceAfterCommission))
                return@flow
            }

            val newBuyValue = userBuyBalance.currencyValue + buyWalletSchema.currencyValue

            converterRepository.updateWalletBalance(sellWalletSchema.copy(currencyValue = newSellValue))
            converterRepository.updateWalletBalance(buyWalletSchema.copy(currencyValue = newBuyValue))

            saveTransactionUseCase(
                TransactionSchema(
                    sellWalletSchema,
                    buyWalletSchema
                )
            )

            emit(
                ConvertUserWalletResultSchema.Success(
                    commissionCharge,
                    sellWalletSchema,
                    buyWalletSchema
                )
            )
            return@flow
        }.catch {
            emit(ConvertUserWalletResultSchema.Error(ConvertUserWalletCurrencyError.GeneralError))
        }.onStart {
            emit(ConvertUserWalletResultSchema.Loading)
        }
    }
}