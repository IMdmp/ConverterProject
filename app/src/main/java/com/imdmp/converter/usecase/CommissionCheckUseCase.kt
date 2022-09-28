package com.imdmp.converter.usecase

import com.imdmp.converter.schema.CommissionCheckResultSchema

interface CommissionCheckUseCase {
    //returns true if passes all requirements
    suspend operator fun invoke(): CommissionCheckResultSchema
}