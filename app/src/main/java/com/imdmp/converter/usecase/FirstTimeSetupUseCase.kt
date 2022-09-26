package com.imdmp.converter.usecase

import com.imdmp.converter.repository.database.entity.WalletEntity

interface FirstTimeSetupUseCase {
    suspend operator fun invoke(walletEntity: WalletEntity)
}