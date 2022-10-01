package com.imdmp.converter.schema

import com.imdmp.converter.repository.database.entity.ConvertRecordEntity
import com.imdmp.converter.repository.database.entity.WalletEntity
import org.junit.Assert.assertEquals
import org.junit.Test
import kotlin.random.Random

class DataMapperKtTest {
    private val DELTA = 1e-15

    @Test
    fun `WalletEntity converts to WalletSchema correctly`() {
        val expectedCurrency = "EUR"
        val expectedBalance = Random.nextDouble()
        val startingWalletEntity = WalletEntity(expectedCurrency, expectedBalance)

        val expectedSchema = WalletSchema(expectedCurrency, expectedBalance)

        val resultSchema = startingWalletEntity.convertToWalletSchema()

        assertEquals(expectedSchema.currencyAbbrev, resultSchema.currencyAbbrev)
        assertEquals(expectedSchema.currencyValue, resultSchema.currencyValue, DELTA)
    }

    @Test
    fun `WalletSchema converts to WalletEntity correctly`() {
        val expectedCurrency = "ABC"
        val expectedBalance = Random.nextDouble()

        val schema =
            WalletSchema(currencyAbbrev = expectedCurrency, currencyValue = expectedBalance)
        val resultEntity = schema.convertToWalletEntity()

        assertEquals(schema.currencyAbbrev, resultEntity.currency)
        assertEquals(schema.currencyValue, resultEntity.balance, DELTA)
    }

    @Test
    fun `CurrencySchema converts to CurrencyEntity correctly`() {
        val expectedCurrency = "ABC"

        val schema = CurrencySchema(currencyAbbrev = expectedCurrency)
        val resultEntity = schema.convertToCurrencyEntity()

        assertEquals(schema.currencyAbbrev, resultEntity.currencyId)
    }

    @Test
    fun `TransactionSchema converts to ConvertRecordEntity correctly `() {

        val expectedCurrency = "EUR"
        val expectedBalance = Random.nextDouble()

        val expectedCurrency2 = "ABC"
        val expectedBalance2 = Random.nextDouble()
        val schema = TransactionSchema(
            sellWalletData = WalletSchema(
                currencyAbbrev = expectedCurrency,
                currencyValue = expectedBalance
            ), buyWalletData = WalletSchema(
                currencyAbbrev = expectedCurrency2,
                currencyValue = expectedBalance2
            )
        )

        val resultEntity = schema.toConvertRecordEntity()

        assertEquals(schema.buyWalletData.convertToWalletEntity(), resultEntity.buyData)
        assertEquals(schema.sellWalletData.convertToWalletEntity(), resultEntity.sellData)
    }

    @Test
    fun `ConvertRecordEntity converts to TransactionSchema correctly`() {

        val expectedCurrency = "EUR"
        val expectedBalance = Random.nextDouble()

        val expectedCurrency2 = "ABC"
        val expectedBalance2 = Random.nextDouble()

        val expectedSellEntity = WalletEntity(
            currency = expectedCurrency,
            balance = expectedBalance
        )

        val expectedBuyEntity = WalletEntity(
            currency = expectedCurrency2,
            balance = expectedBalance2
        )

        val entity = ConvertRecordEntity(
            id = 0, sellData = expectedSellEntity, buyData = expectedBuyEntity
        )

        val result = entity.toTransactionSchema()

        assertEquals(expectedSellEntity, result.sellWalletData)
        assertEquals(expectedBuyEntity, result.buyWalletData)
    }
}