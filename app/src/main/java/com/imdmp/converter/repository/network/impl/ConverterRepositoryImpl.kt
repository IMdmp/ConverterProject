package com.imdmp.converter.repository.network.impl

import com.imdmp.converter.repository.database.dao.CurrencyDAO
import com.imdmp.converter.repository.database.entity.CurrencyEntity
import com.imdmp.converter.repository.network.ConverterRepository
import com.imdmp.converter.repository.network.ConverterService
import com.imdmp.converter.schema.PullLatestRatesSchema
import com.imdmp.converter.schema.convertToPullLatestRatesSchema
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import javax.inject.Inject

class ConverterRepositoryImpl @Inject constructor(
    private val converterService: ConverterService,
    private val currencyDAO: CurrencyDAO,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
): ConverterRepository {

    override suspend fun pullLatestRates(): PullLatestRatesSchema {
        val jsonObject = JSONObject(converterService.pullLatestRates())
        return jsonObject.convertToPullLatestRatesSchema()
    }

    override suspend fun saveCurrencyIdList(currencyList: List<String>) {
        withContext(dispatcher) {
            currencyDAO.insertAllCurrencies(currencyList.map {
                CurrencyEntity(it)
            })
        }
    }


}