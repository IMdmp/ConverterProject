package com.imdmp.converter.usecase.impl

import com.imdmp.converter.usecase.GetCommissionChargeUseCase
import javax.inject.Inject

const val COMMISSION_CHARGE_PERCENT = 0.007

class GetCommissionChargeUseCaseImpl @Inject constructor(): GetCommissionChargeUseCase {
    override suspend fun invoke(value: Double): Double {
        return value * COMMISSION_CHARGE_PERCENT
    }
}