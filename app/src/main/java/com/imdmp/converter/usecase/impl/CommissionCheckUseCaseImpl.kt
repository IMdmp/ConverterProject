package com.imdmp.converter.usecase.impl

import com.imdmp.converter.schema.CommissionCheckResultSchema
import com.imdmp.converter.usecase.CommissionCheckUseCase
import javax.inject.Inject

class CommissionCheckUseCaseImpl @Inject constructor(): CommissionCheckUseCase {
    override suspend fun invoke(): CommissionCheckResultSchema {


        return CommissionCheckResultSchema.Success
    }
}