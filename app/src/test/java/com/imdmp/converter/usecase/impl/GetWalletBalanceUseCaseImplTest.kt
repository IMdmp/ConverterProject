package com.imdmp.converter.usecase.impl

import com.imdmp.converter.repository.ConverterRepository
import com.imdmp.converter.schema.WalletSchema
import com.imdmp.converter.usecase.GetWalletBalanceUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class GetWalletBalanceUseCaseImplTest {

    lateinit var useCase: GetWalletBalanceUseCase

    @Mock
    lateinit var mockedConverterRepository: ConverterRepository

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        useCase = GetWalletBalanceUseCaseImpl(mockedConverterRepository)
    }

    @Test
    fun `useCalls invoke called ConverterRepository and returns WalletSchema List`() {
        val expected: List<WalletSchema> = mock()

        runTest {
            whenever(mockedConverterRepository.getWalletBalance()).thenReturn(expected)
            val result = useCase()

            verify(mockedConverterRepository).getWalletBalance()
            assertEquals(expected, result)
        }
    }
}