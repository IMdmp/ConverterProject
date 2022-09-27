package com.imdmp.converter.repository.network.impl

import com.imdmp.converter.repository.ConverterRepository
import com.imdmp.converter.repository.database.dao.CurrencyDAO
import com.imdmp.converter.repository.database.dao.WalletDAO
import com.imdmp.converter.repository.impl.ConverterRepositoryImpl
import com.imdmp.converter.repository.network.ConverterService
import com.imdmp.converter.schema.convertToPullLatestRatesSchema
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.json.JSONObject
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.exceptions.base.MockitoException
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class ConverterRepositoryImplTest {

    lateinit var repository: ConverterRepository

    @Mock
    lateinit var mockedConverterService: ConverterService

    @Mock
    lateinit var mockedCurrencyDAO: CurrencyDAO

    @Mock
    lateinit var mockedWalletDAO: WalletDAO

    val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        repository = ConverterRepositoryImpl(
            converterService = mockedConverterService,
            currencyDAO = mockedCurrencyDAO,
            walletDAO = mockedWalletDAO
        )
    }

    @Test
    fun `pull_latest_rates should return PullLatestRatesSchema when success`() =
        runTest {
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
    fun `pull_latest_rates should throw exception when response not successful `() = runTest {
        val mockitoException = MockitoException("")
        whenever(mockedConverterService.pullLatestRates()).thenThrow(mockitoException)
        repository.pullLatestRates()
    }


    // TODO : db tests
}