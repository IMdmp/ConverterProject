package com.imdmp.converter.repository.network

import com.imdmp.converter.schema.PullLatestRatesSchema

interface ConverterRepository {
    suspend fun pullLatestRates(): PullLatestRatesSchema
    suspend fun saveCurrencyIdList(currencyList: List<String>)
}