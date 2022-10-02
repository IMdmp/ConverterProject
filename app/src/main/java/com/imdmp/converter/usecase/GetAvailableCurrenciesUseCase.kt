package com.imdmp.converter.usecase

import com.imdmp.converter.schema.SupportedCurrencySchema

interface GetAvailableCurrenciesUseCase {
    suspend operator fun invoke(fromCache: Boolean): List<SupportedCurrencySchema>
}