package com.imdmp.converter.schema

import java.math.BigDecimal

data class PullLatestRatesSchema( //normally, would have response data class to represent response from server. but since using json object, omitted for now.
    val base: String,
    val date: String,
    val rates: Map<String, BigDecimal>,
    val timestamp: Int
) {
}