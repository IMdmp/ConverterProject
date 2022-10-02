package com.imdmp.converter.schema

data class ConvertCurrencySchema(
    val convertedCurrencyResult: Double,
    val timeStamp: Long,
    val rate: Double,
) {
}