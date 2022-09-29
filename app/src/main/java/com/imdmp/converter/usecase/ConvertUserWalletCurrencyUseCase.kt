package com.imdmp.converter.usecase

import com.imdmp.converter.schema.ConvertUserWalletResultSchema
import com.imdmp.converter.schema.WalletSchema

interface ConvertUserWalletCurrencyUseCase {
    suspend operator fun invoke(
        sellWalletSchema: WalletSchema,
        buyWalletSchema: WalletSchema
    ): ConvertUserWalletResultSchema
}