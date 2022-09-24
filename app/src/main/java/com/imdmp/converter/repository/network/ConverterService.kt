package com.imdmp.converter.repository.network

import retrofit2.http.GET
import retrofit2.http.Query

interface ConverterService {

    companion object {
        const val BASE = "base"
    }

    @GET("exchangerates_data/latest")
    suspend fun pullLatestRates(@Query(BASE) base: String? = null): String
}