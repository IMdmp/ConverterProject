package com.imdmp.converter.schema

sealed class ConvertUserWalletCurrencyError: RuntimeException() {
    object NotEnoughBalance: ConvertUserWalletCurrencyError()
    object NotEnoughBalanceAfterCommission: ConvertUserWalletCurrencyError()

}