package com.imdmp.converter.repository.network

import com.imdmp.converter.repository.network.response.ConvertCurrencyResponse
import com.imdmp.converter.repository.network.response.GetSupportedCurrenciesResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ConverterService {

    companion object {
        const val BASE = "base"
        const val FROM = "from"
        const val TO = "to"
        const val AMOUNT = "amount"
    }

    @GET("exchangerates_data/latest")
    suspend fun pullLatestRates(@Query(BASE) base: String? = null): String

    @GET("exchangerates_data/symbols")
    suspend fun getSupportedCurrencies(): GetSupportedCurrenciesResponse

    @GET("exchangerates_data/convert")
    suspend fun convertCurrency(
        @Query(FROM) from: String,
        @Query(TO) to: String,
        @Query(AMOUNT) amount: String
    ): ConvertCurrencyResponse
}