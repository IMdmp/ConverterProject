package com.imdmp.converter.repository

import com.imdmp.converter.schema.CurrencySchema
import com.imdmp.converter.schema.PullLatestRatesSchema
import com.imdmp.converter.schema.TransactionSchema
import com.imdmp.converter.schema.WalletSchema

interface ConverterRepository {
    suspend fun pullLatestRates(): PullLatestRatesSchema
    suspend fun saveCurrencyIdList(currencyList: List<CurrencySchema>)
    suspend fun getWalletBalance(): List<WalletSchema>
    suspend fun updateWalletBalance(walletSchema: WalletSchema)
    suspend fun getWalletBalance(currencyId: String): WalletSchema?
    suspend fun saveTransaction(transactionSchema: TransactionSchema)
}