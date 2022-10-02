package com.imdmp.converter.repository.network.response

data class ConvertCurrencyResponse(
    val info: Info,
    val date: String,
    val result: Double,
) {
}

data class Info(
    val timestamp: Long,
    val rate: Double
)