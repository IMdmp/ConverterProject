package com.imdmp.converter.usecase

import com.imdmp.converter.schema.PullLatestRatesSchema
import kotlinx.coroutines.flow.Flow

interface PullRatesAndSaveCurrenciesUseCase {
    suspend operator fun invoke(): Flow<PullLatestRatesSchema>
}