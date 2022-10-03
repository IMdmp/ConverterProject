package com.imdmp.converter.schema

sealed class ConvertUserWalletCurrencyError {
    object NotEnoughBalance: ConvertUserWalletCurrencyError()
    object NotEnoughBalanceAfterCommission: ConvertUserWalletCurrencyError()
    object GeneralError: ConvertUserWalletCurrencyError()

}