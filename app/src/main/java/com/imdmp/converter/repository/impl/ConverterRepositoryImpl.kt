package com.imdmp.converter.repository.impl

import com.imdmp.converter.repository.ConverterRepository
import com.imdmp.converter.repository.database.dao.ConvertRecordDAO
import com.imdmp.converter.repository.database.dao.CurrencyDAO
import com.imdmp.converter.repository.database.dao.WalletDAO
import com.imdmp.converter.repository.network.ConverterService
import com.imdmp.converter.schema.CurrencySchema
import com.imdmp.converter.schema.PullLatestRatesSchema
import com.imdmp.converter.schema.TransactionSchema
import com.imdmp.converter.schema.WalletSchema
import com.imdmp.converter.schema.convertToCurrencyEntity
import com.imdmp.converter.schema.convertToPullLatestRatesSchema
import com.imdmp.converter.schema.convertToWalletEntity
import com.imdmp.converter.schema.convertToWalletSchema
import com.imdmp.converter.schema.toConvertRecordEntity
import com.imdmp.converter.schema.toTransactionSchema
import org.json.JSONObject
import javax.inject.Inject

class ConverterRepositoryImpl @Inject constructor(
    private val converterService: ConverterService,
    private val currencyDAO: CurrencyDAO,
    private val walletDAO: WalletDAO,
    private val convertRecordDAO: ConvertRecordDAO,
): ConverterRepository {

    override suspend fun pullLatestRates(): PullLatestRatesSchema {
        val jsonObject = JSONObject(converterService.pullLatestRates())
        return jsonObject.convertToPullLatestRatesSchema()
    }

    override suspend fun saveCurrencyIdList(currencyList: List<CurrencySchema>) {
        currencyDAO.insertAllCurrencies(currencyList.map {
            it.convertToCurrencyEntity()
        })
    }

    override suspend fun getWalletBalance(): List<WalletSchema>? {
        return walletDAO.getAllWalletData()?.map {
            it.convertToWalletSchema()
        }
    }

    override suspend fun getWalletBalance(currencyId: String): WalletSchema? {
        return walletDAO.getWallet(currencyId)?.convertToWalletSchema()
    }

    override suspend fun updateWalletBalance(walletSchema: WalletSchema) {
        walletDAO.insertWallet(walletSchema.convertToWalletEntity())
    }

    override suspend fun saveTransaction(transactionSchema: TransactionSchema) {
        convertRecordDAO.insertRecord(transactionSchema.toConvertRecordEntity())
    }

    override suspend fun getNumberOfTransactions(): Int {
        return convertRecordDAO.getRecordCount() ?: 0
    }

    override suspend fun getTransactionsWithCurrencyLike(walletSchema: WalletSchema): List<TransactionSchema> {
        return convertRecordDAO.getTransactionsWithCurrencyLike(walletSchema.convertToWalletEntity())
            .map {
                it.toTransactionSchema()
            }
    }

    override suspend fun getAllTransactions(walletSchema: WalletSchema): List<TransactionSchema> {
        return convertRecordDAO.getAllRecords()?.map {
            it.toTransactionSchema()
        } ?: listOf()
    }
}