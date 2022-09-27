package com.imdmp.converter.usecase.impl

import com.imdmp.converter.schema.CurrencySchema
import com.imdmp.converter.usecase.GetAvailableCurrenciesUseCase
import javax.inject.Inject

class GetAvailableCurrenciesUseCaseImpl @Inject constructor(): GetAvailableCurrenciesUseCase {
    override suspend fun invoke(): List<CurrencySchema> {
        return listOf(
            CurrencySchema("EUR"),
            CurrencySchema("USD"),
            CurrencySchema("PHP"),
            CurrencySchema("YEN"),
            CurrencySchema("MYR"),
            CurrencySchema("EUR"),
            CurrencySchema("USD"),
            CurrencySchema("PHP"),
            CurrencySchema("YEN"),
            CurrencySchema("MYR"),
            CurrencySchema("EUR"),
            CurrencySchema("USD"),
            CurrencySchema("PHP"),
            CurrencySchema("YEN"),
            CurrencySchema("MYR"),
            CurrencySchema("EUR"),
            CurrencySchema("USD"),
            CurrencySchema("PHP"),
            CurrencySchema("YEN"),
            CurrencySchema("MYR"),
            CurrencySchema("EUR"),
            CurrencySchema("USD"),
            CurrencySchema("PHP"),
            CurrencySchema("YEN"),
            CurrencySchema("MYR"),
        )
    }
}