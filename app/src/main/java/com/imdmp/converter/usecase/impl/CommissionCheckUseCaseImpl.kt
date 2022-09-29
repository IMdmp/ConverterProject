package com.imdmp.converter.usecase.impl

import com.imdmp.converter.schema.CommissionCheckResultSchema
import com.imdmp.converter.usecase.CommissionCheckUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class CommissionCheckUseCaseImpl @Inject constructor(): CommissionCheckUseCase {
    override suspend fun invoke(): Flow<CommissionCheckResultSchema> {
        return flow<CommissionCheckResultSchema> {

            delay(1000)
            emit(CommissionCheckResultSchema.Success)
        }.catch {
            emit(CommissionCheckResultSchema.Error("Error here."))
        }.onStart {
            emit(CommissionCheckResultSchema.Loading)
        }

    }
}