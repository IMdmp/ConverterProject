package com.imdmp.converter.repository.network.impl

import com.imdmp.converter.repository.ConverterRepository
import com.imdmp.converter.repository.database.dao.ConvertRecordDAO
import com.imdmp.converter.repository.database.dao.CurrencyDAO
import com.imdmp.converter.repository.database.dao.WalletDAO
import com.imdmp.converter.repository.database.entity.WalletEntity
import com.imdmp.converter.repository.impl.ConverterRepositoryImpl
import com.imdmp.converter.repository.network.ConverterService
import com.imdmp.converter.schema.SupportedCurrencySchema
import com.imdmp.converter.schema.TransactionSchema
import com.imdmp.converter.schema.WalletSchema
import com.imdmp.converter.schema.convertToCurrencyEntity
import com.imdmp.converter.schema.convertToPullLatestRatesSchema
import com.imdmp.converter.schema.convertToWalletEntity
import com.imdmp.converter.schema.convertToWalletSchema
import com.imdmp.converter.schema.toConvertRecordEntity
import kotlinx.coroutines.runBlocking
import org.json.JSONObject
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.exceptions.base.MockitoException
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class ConverterRepositoryImplTest {

    lateinit var repository: ConverterRepository

    @Mock
    lateinit var mockedConverterService: ConverterService

    @Mock
    lateinit var mockedCurrencyDAO: CurrencyDAO

    @Mock
    lateinit var mockedWalletDAO: WalletDAO

    @Mock
    lateinit var mockedConvertRecordDao: ConvertRecordDAO

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        repository = ConverterRepositoryImpl(
            converterService = mockedConverterService,
            currencyDAO = mockedCurrencyDAO,
            walletDAO = mockedWalletDAO,
            convertRecordDAO = mockedConvertRecordDao,
        )
    }

    @Test
    fun `pull_latest_rates should return PullLatestRatesSchema when success`(): Unit =
        runBlocking {
            val stringRes = """
                {
            "success": true,
            "timestamp": 1663990563,
            "base": "EUR",
            "date": "2022-09-24",
            "rates": {
                "AED": 3.559044,
                "AFN": 84.78819,
                "ALL": 116.667093
                }
        }
            """.trimIndent()
            whenever(mockedConverterService.pullLatestRates()).thenReturn(stringRes)

            val expected = JSONObject(stringRes).convertToPullLatestRatesSchema()
            val actual = repository.pullLatestRates()

            assertEquals(expected, actual)
        }

    @Test(expected = MockitoException::class)
    fun `pull_latest_rates should throw exception when response not successful `(): Unit =
        runBlocking {
            val mockitoException = MockitoException("")
            whenever(mockedConverterService.pullLatestRates()).thenThrow(mockitoException)
            repository.pullLatestRates()
        }


    @Test
    fun `saveSupportedCurrencyList should call CurrencyDao when success`() {
        runBlocking {
            val expectedList = listOf<SupportedCurrencySchema>()
            repository.saveSupportedCurrencyList(expectedList)

            verify(mockedCurrencyDAO).insertAllCurrencies(expectedList.map {
                it.convertToCurrencyEntity()
            })
        }
    }

    @Test
    fun `getWalletBalance returns WalletSchemaList when success`() {
        val entityList = listOf<WalletEntity>()
        val expectedList = entityList.map { it.convertToWalletSchema() }
        runBlocking {
            whenever(mockedWalletDAO.getAllWalletData()).thenReturn(entityList)
            val result = repository.getWalletBalance()

            assertEquals(expectedList, result)
        }
    }

    @Test
    fun `getWalletBalance returns null when repository returns null`() {
        val entityList = null
        val expectedList = null
        runBlocking {
            whenever(mockedWalletDAO.getAllWalletData()).thenReturn(entityList)
            val result = repository.getWalletBalance()

            assertEquals(expectedList, result)
        }
    }

    @Test
    fun `updateWalletBalance calls WalletDao with walletEntity`() {
        val expectedWalletSchema = WalletSchema(
            currencyAbbrev = "",
            currencyValue = 0.0
        )
        val expectedWalletEntity: WalletEntity = expectedWalletSchema.convertToWalletEntity()
        runBlocking {
            repository.updateWalletBalance(expectedWalletSchema)

            verify(mockedWalletDAO).insertWallet(expectedWalletEntity)
        }
    }

    @Test
    fun `getWalletBalance returns WalletSchema when success`() {
        val expectedCurrencyId = "abc"
        runBlocking {
            repository.getWalletBalance(expectedCurrencyId)
            verify(mockedWalletDAO).getWallet(expectedCurrencyId)
        }
    }

    @Test
    fun `getWalletBalance returns null when DAO returns null`() {
        val dummyCurrencyId = "abc"
        val expected = null

        runBlocking {
            whenever(mockedWalletDAO.getWallet(dummyCurrencyId)).thenReturn(expected)
            val result = repository.getWalletBalance(dummyCurrencyId)
            verify(mockedWalletDAO).getWallet(dummyCurrencyId)
            assertEquals(expected, result)
        }
    }

    @Test
    fun `saveTransaction calls DAO`() {
        val expectedTransactionSchema = TransactionSchema(
            sellWalletData = WalletSchema(
                currencyAbbrev = "",
                currencyValue = 0.0
            ), buyWalletData = WalletSchema(
                currencyAbbrev = "",
                currencyValue = 0.0
            )
        )
        val expected = expectedTransactionSchema.toConvertRecordEntity()
        runBlocking {
            repository.saveTransaction(expectedTransactionSchema)
            verify(mockedConvertRecordDao).insertRecord(expected)
        }
    }

    @Test
    fun `getNumberOfTransactions returns int when success`() {
        val expected = 10
        runBlocking {
            whenever(mockedConvertRecordDao.getRecordCount()).thenReturn(expected)
            val result = repository.getNumberOfTransactions()
            assertEquals(result, expected)
        }
    }
}