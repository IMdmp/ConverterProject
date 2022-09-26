package com.imdmp.converter.usecase

import com.imdmp.converter.schema.WalletSchema

interface GetWalletBalanceUseCase {
    suspend operator fun invoke(): List<WalletSchema>
}