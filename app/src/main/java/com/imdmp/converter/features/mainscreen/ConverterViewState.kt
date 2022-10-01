package com.imdmp.converter.features.mainscreen

data class ConverterViewState(
    val converterDataLoading: Boolean = false,
    val sellCurrencyLabel: String,
    val sellCurrencyData: String,
    val receiveCurrencyLabel: String,
    val receiveCurrencyData: String,
    val errorPresent: Boolean = false,
    val errorMessage: String? = null,
) {
    companion object {
        fun init(): ConverterViewState {
            return ConverterViewState(
                converterDataLoading = false,
                sellCurrencyLabel = "EUR",
                sellCurrencyData = "",
                receiveCurrencyLabel = "USD",
                receiveCurrencyData = "",
                errorPresent = false,
                errorMessage = null,
            )
        }
    }
}
