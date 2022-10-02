package com.imdmp.converter.usecase

import com.imdmp.converter.schema.ConvertCurrencyFlowSchema
import kotlinx.coroutines.flow.Flow

interface ConvertCurrencyUseCase {
    suspend operator fun invoke(
        sellData: Double,
        sellCurrencyId: String,
        receiveCurrencyId: String
    ): Flow<ConvertCurrencyFlowSchema>
}