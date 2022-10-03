package com.imdmp.converter.schema

sealed class ConvertCurrencyFlowSchema {
    object Loading: ConvertCurrencyFlowSchema()
    object Error: ConvertCurrencyFlowSchema()
    data class Success(
        val convertedCurrencyResult: Double,
        val timeStamp: Long,
        val rate: Double,
    ): ConvertCurrencyFlowSchema()
}