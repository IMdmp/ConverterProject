package com.imdmp.converter.usecase

import java.math.BigDecimal

interface ConvertCurrencyUseCase {
    suspend operator fun invoke(
        sellData: Double,
        sellCurrencyId: String,
        receiveCurrencyId: String
    ): BigDecimal
}