package com.imdmp.converter.usecase.commissionrules

import com.imdmp.converter.base.di.NameAnnotationConstants
import com.imdmp.converter.repository.ConverterRepository
import com.imdmp.converter.schema.WalletSchema
import com.imdmp.converter.usecase.GetCommissionChargeUseCase
import com.imdmp.converter.usecase.commissionrules.CommissionRuleConstants.Companion.COMMISSION_CHARGE_PERCENT
import javax.inject.Inject
import javax.inject.Named

const val NUMBER_COMMISSION_FEE_TRANSACTIONS = 5

@Named(NameAnnotationConstants.FREE_UP_TO_5_TRANSACTIONS)
class GetCommissionChargeUseCaseImpl @Inject constructor(
    private val converterRepository: ConverterRepository,
): GetCommissionChargeUseCase {
    override suspend fun invoke(sellData: WalletSchema): Double {
        if (converterRepository.getNumberOfTransactions() < NUMBER_COMMISSION_FEE_TRANSACTIONS) {
            return 0.0
        }
        return sellData.currencyValue * COMMISSION_CHARGE_PERCENT
    }
}