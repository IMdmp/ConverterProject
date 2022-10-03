package com.imdmp.converter.usecase.impl

import com.imdmp.converter.repository.ConverterRepository
import com.imdmp.converter.schema.PullLatestRatesSchema
import com.imdmp.converter.usecase.PullRatesAndSaveCurrenciesUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

const val refreshIntervalMs = 5000L

class PullRatesAndSaveCurrenciesUseCaseImpl @Inject constructor(
    private val converterRepository: ConverterRepository
): PullRatesAndSaveCurrenciesUseCase {
    override suspend operator fun invoke(): Flow<PullLatestRatesSchema> {
        return flow {
            while (true) {
                val rates = converterRepository.pullLatestRates()
                emit(rates) // Emits the result of the request to the flow
                delay(refreshIntervalMs) // Suspends the coroutine for some time
            }
        }
    }
}