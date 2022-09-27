package com.imdmp.converter.features.mainscreen

data class ConverterViewState(
    val converterDataLoading: Boolean = false,
    val sellCurrencyLabel: String,
    val sellCurrencyData: Double,
    val receiveCurrencyLabel: String,
    val receiveCurrencyData: Double,
    val errorPresent: Boolean = false,
    val errorMessage: String? = null,
) {
    companion object {
        fun init(): ConverterViewState {
            return ConverterViewState(
                converterDataLoading = false,
                sellCurrencyLabel = "",
                sellCurrencyData = 0.0,
                receiveCurrencyLabel = "",
                receiveCurrencyData = 0.0,
                errorPresent = false,
                errorMessage = null,
            )
        }
    }
}
