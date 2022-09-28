package com.imdmp.converter.usecase

interface ConvertCurrencyUseCase {
    suspend operator fun invoke(
        sellData: Double,
        sellCurrencyId: String,
        receiveCurrencyId: String
    ): Double
}