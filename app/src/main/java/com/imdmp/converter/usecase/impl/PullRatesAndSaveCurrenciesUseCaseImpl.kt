package com.imdmp.converter.usecase.impl

import com.imdmp.converter.repository.ConverterRepository
import com.imdmp.converter.usecase.PullRatesAndSaveCurrenciesUseCase
import javax.inject.Inject

class PullRatesAndSaveCurrenciesUseCaseImpl @Inject constructor(
    private val converterRepository: ConverterRepository
): PullRatesAndSaveCurrenciesUseCase {
    override suspend operator fun invoke() {
        val latestRates = converterRepository.pullLatestRates()

    }
}