package com.imdmp.converter.schema

data class TransactionSchema(
    val sellWalletData: WalletSchema,
    val buyWalletData: WalletSchema,
) {
}