package com.imdmp.converter.usecase.commissionrules

import com.imdmp.converter.base.di.NameAnnotationConstants
import com.imdmp.converter.repository.ConverterRepository
import com.imdmp.converter.schema.WalletSchema
import com.imdmp.converter.schema.WalletSchema.Companion.EUR
import com.imdmp.converter.usecase.ConvertCurrencyUseCase
import com.imdmp.converter.usecase.GetCommissionChargeUseCase
import com.imdmp.converter.usecase.commissionrules.CommissionRuleConstants.Companion.COMMISSION_CHARGE_PERCENT
import javax.inject.Inject
import javax.inject.Named

@Named(NameAnnotationConstants.FREE_UP_TO_200_EUR)
class FreeUpTo200Eur @Inject constructor(
    private val repository: ConverterRepository,
    private val convertUserCurrencyUseCase: ConvertCurrencyUseCase
): GetCommissionChargeUseCase {

    override suspend fun invoke(sellData: WalletSchema): Double {
        val dataList = repository.getAllTransactions(sellData)
        var totalConvertedSoFar = 0.0 // assume in eur
        dataList.forEach {
            val converted = convertUserCurrencyUseCase(
                it.sellWalletData.currencyValue,
                it.sellWalletData.currencyAbbrev,
                EUR
            )

            totalConvertedSoFar += converted
        }

        return if (totalConvertedSoFar <= 200) {
            0.0
        } else {
            sellData.currencyValue * COMMISSION_CHARGE_PERCENT
        }
    }

}