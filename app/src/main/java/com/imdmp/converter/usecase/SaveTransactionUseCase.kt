package com.imdmp.converter.usecase

import com.imdmp.converter.schema.TransactionSchema

interface SaveTransactionUseCase {
    suspend operator fun invoke(transactionSchema: TransactionSchema)
}