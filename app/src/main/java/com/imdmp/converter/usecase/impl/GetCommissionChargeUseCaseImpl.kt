package com.imdmp.converter.usecase.impl

import com.imdmp.converter.repository.ConverterRepository
import com.imdmp.converter.usecase.GetCommissionChargeUseCase
import javax.inject.Inject

const val COMMISSION_CHARGE_PERCENT = 0.007
const val NUMBER_COMMISSION_FEE_TRANSACTIONS = 5

class GetCommissionChargeUseCaseImpl @Inject constructor(
    private val converterRepository: ConverterRepository,
): GetCommissionChargeUseCase {
    override suspend fun invoke(value: Double): Double {
        if (converterRepository.getNumberOfTransactions() < NUMBER_COMMISSION_FEE_TRANSACTIONS) {
            return 0.0
        }
        return value * COMMISSION_CHARGE_PERCENT
    }
}