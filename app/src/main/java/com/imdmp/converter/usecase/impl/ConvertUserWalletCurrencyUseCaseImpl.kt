package com.imdmp.converter.usecase.impl

import com.imdmp.converter.repository.ConverterRepository
import com.imdmp.converter.schema.ConvertUserWalletCurrencyError
import com.imdmp.converter.schema.ConvertUserWalletResultSchema
import com.imdmp.converter.schema.TransactionSchema
import com.imdmp.converter.schema.WalletSchema
import com.imdmp.converter.usecase.ConvertUserWalletCurrencyUseCase
import com.imdmp.converter.usecase.GetCommissionChargeUseCase
import com.imdmp.converter.usecase.SaveTransactionUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class ConvertUserWalletCurrencyUseCaseImpl @Inject constructor(
    private val converterRepository: ConverterRepository,
    private val getCommissionChargeUseCase: GetCommissionChargeUseCase,
    private val saveTransactionUseCase: SaveTransactionUseCase
): ConvertUserWalletCurrencyUseCase {
    override suspend fun invoke(
        sellWalletSchema: WalletSchema,
        buyWalletSchema: WalletSchema
    ): Flow<ConvertUserWalletResultSchema> {
        return flow<ConvertUserWalletResultSchema> {
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

            if (userSellBalance.currencyValue < sellWalletSchema.currencyValue) {
                throw ConvertUserWalletCurrencyError.NotEnoughBalance
            }

            val commissionCharge = getCommissionChargeUseCase(sellWalletSchema.currencyValue)
            val newSellValue = userSellBalance.currencyValue -
                sellWalletSchema.currencyValue -
                commissionCharge

            if (newSellValue < 0) {
                throw ConvertUserWalletCurrencyError.NotEnoughBalanceAfterCommission
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
        }.catch {

            emit(ConvertUserWalletResultSchema.Error(""))
        }.onStart {
            emit(ConvertUserWalletResultSchema.Loading)
        }
    }
}