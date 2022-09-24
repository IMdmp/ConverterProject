package com.imdmp.converter.schema

import org.json.JSONException
import org.json.JSONObject
import java.math.BigDecimal

data class PullLatestRatesSchema( //normally, would have response data class to represent response from server. but since using json object, omitted for now.
    val base: String,
    val date: String,
    val rates: Map<String, BigDecimal>,
    val timestamp: Int
) {
}

fun JSONObject.convertToPullLatestRatesSchema(): PullLatestRatesSchema {
    val rateObject: JSONObject = this.get("rates") as JSONObject
    val rateMap = rateObject.toMap()

    return PullLatestRatesSchema(
        base = this.getString("base"),
        date = this.getString("date"),
        rates = rateMap as Map<String, BigDecimal>,
        timestamp = this.getInt("timestamp")
    )
}

@Throws(JSONException::class)
fun JSONObject.toMap(): Map<String, Any> {
    val map = mutableMapOf<String, Any>()
    val keysItr: Iterator<String> = this.keys()
    while (keysItr.hasNext()) {
        val key = keysItr.next()
        var value: Any = this.get(key)
        when (value) {
            is JSONObject -> value = value.toMap()
        }
        map[key] = value
    }
    return map
}