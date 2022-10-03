package com.imdmp.converter.repository.network.response

data class GetSupportedCurrenciesResponse(
    val symbols: Map<String, String>
) {
}