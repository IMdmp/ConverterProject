package com.imdmp.converter.usecase

import com.imdmp.converter.schema.WalletSchema

interface UpdateWalletUseCase {
    suspend operator fun invoke(walletSchema: WalletSchema)
}