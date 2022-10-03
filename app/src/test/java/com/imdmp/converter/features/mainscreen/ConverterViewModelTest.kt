package com.imdmp.converter.features.mainscreen

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.imdmp.converter.features.CoroutineTestRule
import com.imdmp.converter.features.mainscreen.numberscreen.NumberScreenCallbacks
import com.imdmp.converter.usecase.ConvertCurrencyUseCase
import com.imdmp.converter.usecase.ConvertUserWalletCurrencyUseCase
import com.imdmp.converter.usecase.GetAvailableCurrenciesUseCase
import com.imdmp.converter.usecase.GetWalletBalanceUseCase
import io.mockk.spyk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class ConverterViewModelTest {

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    lateinit var vm: ConverterViewModel

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    lateinit var mockedConvertCurrencyUseCase: ConvertCurrencyUseCase

    @Mock
    lateinit var mockedConvertUserWalletCurrencyUseCase: ConvertUserWalletCurrencyUseCase

    @Mock
    lateinit var mockedGetAvailableCurrenciesUseCase: GetAvailableCurrenciesUseCase

    @Mock
    lateinit var mockedGetWalletBalanceUseCase: GetWalletBalanceUseCase

    lateinit var numberScreenCallbacks: NumberScreenCallbacks

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        vm = spyk(
            ConverterViewModel(
                coroutineDispatcher = coroutineTestRule.testDispatcher,
                convertCurrencyUseCase = mockedConvertCurrencyUseCase,
                convertUserWalletCurrencyUseCase = mockedConvertUserWalletCurrencyUseCase,
                getAvailableCurrenciesUseCase = mockedGetAvailableCurrenciesUseCase,
                getWalletBalanceUseCase = mockedGetWalletBalanceUseCase
            )
        )
        numberScreenCallbacks = vm
    }

    @After
    fun tearDown() {
    }

    @Test
    fun switchCurrencyLabels() {
    }

    @Test
    fun inputBoxSelected() {
    }

    @Test
    fun `characterEmitted callbacks gets collected`() = runTest {
        vm.characterInput.test {
            numberScreenCallbacks.characterEmitted('1')
            assertEquals('1', awaitItem())
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `test`() {
        runTest {

        }

    }
}