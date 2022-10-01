package com.imdmp.converter.usecase

import com.imdmp.converter.schema.ConvertUserWalletResultSchema
import com.imdmp.converter.schema.WalletSchema
import kotlinx.coroutines.flow.Flow

interface ConvertUserWalletCurrencyUseCase {
    suspend operator fun invoke(
        sellWalletSchema: WalletSchema,
        buyWalletSchema: WalletSchema
    ): Flow<ConvertUserWalletResultSchema>
}