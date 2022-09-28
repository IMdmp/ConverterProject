package com.imdmp.converter.schema

sealed class ConvertUserWalletResultSchema {
    object Loading: ConvertUserWalletResultSchema()

    data class Error(val message: String): ConvertUserWalletResultSchema()

    object Success: ConvertUserWalletResultSchema()
}