package com.imdmp.converter.usecase.impl

import com.imdmp.converter.repository.ConverterRepository
import com.imdmp.converter.schema.SupportedCurrencySchema
import com.imdmp.converter.usecase.GetAvailableCurrenciesUseCase
import javax.inject.Inject

class GetAvailableCurrenciesUseCaseImpl @Inject constructor(
    private val converterRepository: ConverterRepository
): GetAvailableCurrenciesUseCase {
    override suspend fun invoke(fromCache: Boolean): List<SupportedCurrencySchema> {
        return converterRepository.getSupportedCurrencies(fromCache.not())
    }
}