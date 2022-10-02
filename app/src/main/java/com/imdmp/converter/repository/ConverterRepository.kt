package com.imdmp.converter.repository

import com.imdmp.converter.schema.ConvertCurrencySchema
import com.imdmp.converter.schema.PullLatestRatesSchema
import com.imdmp.converter.schema.SupportedCurrencySchema
import com.imdmp.converter.schema.TransactionSchema
import com.imdmp.converter.schema.WalletSchema

interface ConverterRepository {
    suspend fun pullLatestRates(): PullLatestRatesSchema
    suspend fun saveSupportedCurrencyList(currencyList: List<SupportedCurrencySchema>)
    suspend fun getWalletBalance(): List<WalletSchema>?
    suspend fun updateWalletBalance(walletSchema: WalletSchema)
    suspend fun getWalletBalance(currencyId: String): WalletSchema?
    suspend fun saveTransaction(transactionSchema: TransactionSchema)
    suspend fun getNumberOfTransactions(): Int
    suspend fun getTransactionsWithCurrencyLike(walletSchema: WalletSchema): List<TransactionSchema>
    suspend fun getAllTransactions(walletSchema: WalletSchema): List<TransactionSchema>
    suspend fun getSupportedCurrencies(fetchNew: Boolean): List<SupportedCurrencySchema>
    suspend fun convertCurrency(
        sellData: Double,
        sellCurrencyId: String,
        receiveCurrencyId: String
    ): ConvertCurrencySchema
}