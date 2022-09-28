package com.imdmp.converter.usecase.impl

import com.imdmp.converter.repository.ConverterRepository
import com.imdmp.converter.schema.ConvertUserWalletResultSchema
import com.imdmp.converter.schema.WalletSchema
import com.imdmp.converter.usecase.ConvertUserWalletCurrencyUseCase
import javax.inject.Inject

class ConvertUserWalletCurrencyUseCaseImpl @Inject constructor(
    private val converterRepository: ConverterRepository
): ConvertUserWalletCurrencyUseCase {
    override suspend fun invoke(
        sellWalletSchema: WalletSchema,
        buyWalletSchema: WalletSchema
    ): ConvertUserWalletResultSchema {
        val sellData = converterRepository.getWalletBalance(sellWalletSchema.currencyAbbrev)
        val buyData = converterRepository.getWalletBalance(buyWalletSchema.currencyAbbrev)

        val newSellValue = sellData.currencyValue - sellWalletSchema.currencyValue
        val newBuyValue = buyData.currencyValue + buyWalletSchema.currencyValue

        converterRepository.updateWalletBalance(sellWalletSchema.copy(currencyValue = newSellValue))
        converterRepository.updateWalletBalance(buyWalletSchema.copy(currencyValue = newBuyValue))

        return ConvertUserWalletResultSchema.Success
    }
}