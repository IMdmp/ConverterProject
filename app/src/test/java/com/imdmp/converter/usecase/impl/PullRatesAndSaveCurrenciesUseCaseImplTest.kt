package com.imdmp.converter.usecase.impl

import com.imdmp.converter.repository.ConverterRepository
import com.imdmp.converter.schema.PullLatestRatesSchema
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class PullRatesAndSaveCurrenciesUseCaseImplTest {

    lateinit var useCase: PullRatesAndSaveCurrenciesUseCaseImpl

    @Mock
    lateinit var mockedConverterRepository: ConverterRepository

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        useCase = PullRatesAndSaveCurrenciesUseCaseImpl(mockedConverterRepository)
    }

    @Test
    fun `invoke calls pullLatestRates and currencyIdList`() = runTest {
        whenever(mockedConverterRepository.pullLatestRates()).thenReturn(
            PullLatestRatesSchema(
                base = "",
                date = "",
                rates = mapOf(),
                timestamp = 0
            )
        )

        whenever(mockedConverterRepository.saveCurrencyIdList(listOf())).thenReturn(Unit)

        useCase()

        verify(mockedConverterRepository).pullLatestRates()
        verify(mockedConverterRepository).saveCurrencyIdList(listOf())
    }

    // todo: add error tests
}