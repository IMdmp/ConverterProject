package com.imdmp.converter.schema

import com.imdmp.converter.repository.database.entity.ConvertRecordEntity
import com.imdmp.converter.repository.database.entity.CurrencyEntity
import com.imdmp.converter.repository.database.entity.WalletEntity
import org.json.JSONException
import org.json.JSONObject
import java.math.BigDecimal

fun WalletEntity.convertToWalletSchema(): WalletSchema {
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

fun CurrencySchema.convertToCurrencyEntity(): CurrencyEntity {
    return CurrencyEntity(this.currencyAbbrev)
}

fun TransactionSchema.toConvertRecordEntity(): ConvertRecordEntity {
    return ConvertRecordEntity(
        sellData = this.sellWalletData.convertToWalletEntity(),
        buyData = this.buyWalletData.convertToWalletEntity()
    )
}

fun ConvertRecordEntity.toTransactionSchema(): TransactionSchema {
    return TransactionSchema(
        sellWalletData = this.sellData.convertToWalletSchema(),
        buyWalletData = this.buyData.convertToWalletSchema()
    )
}

@Suppress("UNCHECKED_CAST")
fun JSONObject.convertToPullLatestRatesSchema(): PullLatestRatesSchema { //used for unit test
    val rateObject: JSONObject = this.get("rates") as JSONObject
    val rateMap: Map<String, BigDecimal> = rateObject.toMap() as Map<String, BigDecimal>

    return PullLatestRatesSchema(
        base = this.getString("base"),
        date = this.getString("date"),
        rates = rateMap,
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