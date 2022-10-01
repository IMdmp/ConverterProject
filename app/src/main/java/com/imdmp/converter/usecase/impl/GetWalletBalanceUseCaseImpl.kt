package com.imdmp.converter.usecase.impl

import com.imdmp.converter.repository.ConverterRepository
import com.imdmp.converter.schema.WalletSchema
import com.imdmp.converter.usecase.GetWalletBalanceUseCase
import javax.inject.Inject

class GetWalletBalanceUseCaseImpl @Inject constructor(
    private val converterRepository: ConverterRepository
): GetWalletBalanceUseCase {
    override suspend fun invoke(): List<WalletSchema> {
        return converterRepository.getWalletBalance() ?: listOf()
    }
}