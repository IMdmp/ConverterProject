package com.imdmp.converter.usecase

import com.imdmp.converter.schema.CurrencySchema

interface GetAvailableCurrenciesUseCase {
    suspend operator fun invoke(): List<CurrencySchema>
}