package com.imdmp.converter.schema

import com.imdmp.converter.repository.database.entity.ConvertRecordEntity
import com.imdmp.converter.repository.database.entity.WalletEntity
import org.json.JSONException
import org.json.JSONObject
import java.math.BigDecimal

fun WalletEntity?.convertToWalletSchema(): WalletSchema? {
    if (this == null) {
        return null
    }

    return WalletSchema(
        this.currency,
        this.balance
    )
}

fun WalletSchema.convertToWalletEntity(): WalletEntity {
    return WalletEntity(
        this.currencyAbbrev,
        this.currencyValue
    )
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

fun TransactionSchema.ToConvertRecordEntity(): ConvertRecordEntity {
    return ConvertRecordEntity(
        sellData = this.sellWalletData.convertToWalletEntity(),
        buyData = this.buyWalletData.convertToWalletEntity()
    )
}