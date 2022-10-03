package com.imdmp.converter.usecase.commissionrules

import com.imdmp.converter.base.Constants
import com.imdmp.converter.base.di.NameAnnotationConstants
import com.imdmp.converter.repository.ConverterRepository
import com.imdmp.converter.schema.WalletSchema
import com.imdmp.converter.schema.WalletSchema.Companion.EUR
import com.imdmp.converter.usecase.ConvertCurrencyUseCase
import com.imdmp.converter.usecase.GetCommissionChargeUseCase
import com.imdmp.converter.usecase.commissionrules.CommissionRuleConstants.Companion.COMMISSION_CHARGE_PERCENT
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*
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

        }
        val df = DecimalFormat(
            Constants.DECIMAL_FORMAT, DecimalFormatSymbols.getInstance(
                Locale.US
            )
        )

        df.roundingMode = RoundingMode.CEILING
        return if (totalConvertedSoFar <= 200) {
            0.0
        } else {
            df.format(sellData.currencyValue * COMMISSION_CHARGE_PERCENT).toDouble()
        }
    }

}