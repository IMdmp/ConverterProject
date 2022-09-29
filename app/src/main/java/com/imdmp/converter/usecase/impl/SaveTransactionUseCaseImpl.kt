package com.imdmp.converter.usecase.impl

import com.imdmp.converter.repository.ConverterRepository
import com.imdmp.converter.schema.TransactionSchema
import com.imdmp.converter.usecase.SaveTransactionUseCase
import javax.inject.Inject

class SaveTransactionUseCaseImpl @Inject constructor(private val converterRepository: ConverterRepository):
    SaveTransactionUseCase {
    override suspend fun invoke(transactionSchema: TransactionSchema) {
        converterRepository.saveTransaction(transactionSchema)
    }
}