package com.imdmp.converter.usecase.impl

import com.imdmp.converter.BuildConfig
import com.imdmp.converter.repository.ConverterRepository
import com.imdmp.converter.schema.ConvertCurrencyFlowSchema
import com.imdmp.converter.usecase.ConvertCurrencyUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject


class ConvertCurrencyUseCaseImpl @Inject constructor(
    private val converterRepository: ConverterRepository,
): ConvertCurrencyUseCase {
    override suspend fun invoke(
        sellData: Double,
        sellCurrencyId: String,
        receiveCurrencyId: String
    ): Flow<ConvertCurrencyFlowSchema> {

        return flow<ConvertCurrencyFlowSchema> {
            if (BuildConfig.MOCK_RESPONSE) {
                delay(2000)
                emit(
                    ConvertCurrencyFlowSchema.Success(
                        convertedCurrencyResult = sellData + 10, timeStamp = 100, rate = 1.98
                    )
                )
                return@flow
            }

            val result = converterRepository.convertCurrency(
                sellData = sellData,
                sellCurrencyId = sellCurrencyId,
                receiveCurrencyId = receiveCurrencyId
            )
            emit(
                ConvertCurrencyFlowSchema.Success(
                    result.convertedCurrencyResult,
                    result.timeStamp,
                    result.rate
                )
            )

        }.onStart {
            emit(ConvertCurrencyFlowSchema.Loading)
        }
    }
}