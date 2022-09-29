package com.imdmp.converter.usecase

import com.imdmp.converter.schema.CommissionCheckResultSchema
import kotlinx.coroutines.flow.Flow

interface CommissionCheckUseCase {
    //returns true if passes all requirements
    suspend operator fun invoke(): Flow<CommissionCheckResultSchema>
}