package com.imdmp.converter.repository.impl

import com.imdmp.converter.repository.ConverterRepository
import com.imdmp.converter.repository.database.dao.ConvertRecordDAO
import com.imdmp.converter.repository.database.dao.CurrencyDAO
import com.imdmp.converter.repository.database.dao.WalletDAO
import com.imdmp.converter.repository.database.entity.CurrencyEntity
import com.imdmp.converter.repository.network.ConverterService
import com.imdmp.converter.schema.CurrencySchema
import com.imdmp.converter.schema.PullLatestRatesSchema
import com.imdmp.converter.schema.ToConvertRecordEntity
import com.imdmp.converter.schema.TransactionSchema
import com.imdmp.converter.schema.WalletSchema
import com.imdmp.converter.schema.convertToPullLatestRatesSchema
import com.imdmp.converter.schema.convertToWalletEntity
import com.imdmp.converter.schema.convertToWalletSchema
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
            CurrencyEntity(it.currencyAbbrev)
        })
    }

    override suspend fun getWalletBalance(): List<WalletSchema> {
        return walletDAO.getAllWalletData().mapNotNull {
            it.convertToWalletSchema()
        }
    }

    override suspend fun getWalletBalance(currencyId: String): WalletSchema? {
        return walletDAO.getWallet(currencyId).convertToWalletSchema()
    }

    override suspend fun updateWalletBalance(walletSchema: WalletSchema) {
        walletDAO.insertWallet(walletSchema.convertToWalletEntity())
    }

    override suspend fun saveTransaction(transactionSchema: TransactionSchema) {
        convertRecordDAO.insertRecord(transactionSchema.ToConvertRecordEntity())
    }

}