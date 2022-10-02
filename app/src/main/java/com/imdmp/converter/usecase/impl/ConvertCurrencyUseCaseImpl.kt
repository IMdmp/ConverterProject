package com.imdmp.converter.usecase.impl

import com.imdmp.converter.repository.ConverterRepository
import com.imdmp.converter.usecase.ConvertCurrencyUseCase
import javax.inject.Inject


class ConvertCurrencyUseCaseImpl @Inject constructor(
    private val converterRepository: ConverterRepository
): ConvertCurrencyUseCase {
    override suspend fun invoke(
        sellData: Double,
        sellCurrencyId: String,
        receiveCurrencyId: String
    ): Double {
        val result = converterRepository.convertCurrency(
            sellData = sellData,
            sellCurrencyId = sellCurrencyId,
            receiveCurrencyId = receiveCurrencyId
        )

        return result.convertedCurrencyResult
    }


}