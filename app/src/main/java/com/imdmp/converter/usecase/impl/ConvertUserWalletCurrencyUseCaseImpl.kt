package com.imdmp.converter.usecase.impl

import com.imdmp.converter.repository.ConverterRepository
import com.imdmp.converter.schema.ConvertUserWalletResultSchema
import com.imdmp.converter.schema.WalletSchema
import com.imdmp.converter.usecase.ConvertUserWalletCurrencyUseCase
import com.imdmp.converter.usecase.GetCommissionChargeUseCase
import javax.inject.Inject

class ConvertUserWalletCurrencyUseCaseImpl @Inject constructor(
    private val converterRepository: ConverterRepository,
    private val getCommissionChargeUseCase: GetCommissionChargeUseCase,
): ConvertUserWalletCurrencyUseCase {
    override suspend fun invoke(
        sellWalletSchema: WalletSchema,
        buyWalletSchema: WalletSchema
    ): ConvertUserWalletResultSchema {
        val userSellBalance = converterRepository.getWalletBalance(sellWalletSchema.currencyAbbrev)
        val userBuyBalance = converterRepository.getWalletBalance(buyWalletSchema.currencyAbbrev)

        val commissionCharge = getCommissionChargeUseCase(sellWalletSchema.currencyValue)

        val newSellValue = userSellBalance.currencyValue -
            sellWalletSchema.currencyValue -
            commissionCharge
        val newBuyValue = userBuyBalance.currencyValue + buyWalletSchema.currencyValue

        converterRepository.updateWalletBalance(sellWalletSchema.copy(currencyValue = newSellValue))
        converterRepository.updateWalletBalance(buyWalletSchema.copy(currencyValue = newBuyValue))

        return ConvertUserWalletResultSchema.Success(
            commissionCharge,
            sellWalletSchema,
            buyWalletSchema
        )
    }
}