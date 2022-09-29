package com.imdmp.converter.usecase.impl

import com.imdmp.converter.repository.ConverterRepository
import com.imdmp.converter.schema.WalletSchema
import com.imdmp.converter.usecase.UpdateWalletUseCase
import javax.inject.Inject

class UpdateWalletUseCaseImpl @Inject constructor(private val converterRepository: ConverterRepository):
    UpdateWalletUseCase {
    override suspend fun invoke(walletSchema: WalletSchema) {
        converterRepository.updateWalletBalance(walletSchema)
    }
}