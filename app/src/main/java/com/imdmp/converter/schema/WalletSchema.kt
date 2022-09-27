package com.imdmp.converter.schema

data class WalletSchema(
    val currencyAbbrev: String,
    val currencyValue: Double,
) {

    companion object {
        const val EUR = "EUR"
        const val USD = "USD"
        const val BGN = "BGN"
        const val PHP = "PHP"
        const val YEN = "YEN"

    }
}