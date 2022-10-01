package com.imdmp.converter.usecase.impl

import com.imdmp.converter.repository.ConverterRepository
import com.imdmp.converter.usecase.ConvertUserWalletCurrencyUseCase
import com.imdmp.converter.usecase.GetCommissionChargeUseCase
import com.imdmp.converter.usecase.SaveTransactionUseCase
import org.junit.Before
import org.mockito.Mock

class ConvertUserWalletCurrencyUseCaseImplTest {

    lateinit var useCase: ConvertUserWalletCurrencyUseCase

    @Mock
    lateinit var mockedConverterRepository: ConverterRepository

    @Mock
    lateinit var mockedGetCommissionChargeUseCaseImpl: GetCommissionChargeUseCase

    @Mock
    lateinit var mockedSaveTransactionUseCaseImpl: SaveTransactionUseCase

    @Before
    fun setUp() {
        useCase = ConvertUserWalletCurrencyUseCaseImpl(
            converterRepository = mockedConverterRepository,
            getCommissionChargeUseCase = mockedGetCommissionChargeUseCaseImpl,
            saveTransactionUseCase = mockedSaveTransactionUseCaseImpl
        )
    }


}