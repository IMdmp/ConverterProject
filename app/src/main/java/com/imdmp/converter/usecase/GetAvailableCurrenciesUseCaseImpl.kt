package com.imdmp.converter.usecase

import com.imdmp.converter.schema.CurrencySchema
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