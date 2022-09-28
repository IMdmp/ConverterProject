package com.imdmp.converter.schema

sealed class ConvertUserWalletResultSchema {
    object Loading: ConvertUserWalletResultSchema()

    data class Error(val message: String): ConvertUserWalletResultSchema()

    data class Success(
        val commissionCharged: Double,
        val sellData: WalletSchema,
        val buyData: WalletSchema,
    ): ConvertUserWalletResultSchema()
}