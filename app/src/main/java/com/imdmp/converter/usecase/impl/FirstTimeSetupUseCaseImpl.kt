package com.imdmp.converter.usecase.impl

import com.imdmp.converter.repository.database.dao.WalletDAO
import com.imdmp.converter.repository.database.entity.WalletEntity
import com.imdmp.converter.usecase.FirstTimeSetupUseCase
import javax.inject.Inject

class FirstTimeSetupUseCaseImpl @Inject constructor(
    private val walletDAO: WalletDAO
): FirstTimeSetupUseCase {

    override suspend fun invoke(walletEntity: WalletEntity) {
        walletDAO.insertWallet(walletEntity)
    }

}