package com.imdmp.converter.schema

import java.math.BigDecimal

data class PullLatestRatesSchema(
    val base: String,
    val date: String,
    val rates: Map<String, BigDecimal>,
    val timestamp: Int
) {
}