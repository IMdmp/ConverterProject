package com.imdmp.converter.usecase.impl

import com.imdmp.converter.usecase.ConvertCurrencyUseCase
import javax.inject.Inject


class ConvertCurrencyUseCaseImpl @Inject constructor(): ConvertCurrencyUseCase {
    override suspend fun invoke(
        sellData: Double,
        sellCurrencyId: String,
        receiveCurrencyId: String
    ): Double {
        val returnData = sellData + 10
        return returnData
    }


}