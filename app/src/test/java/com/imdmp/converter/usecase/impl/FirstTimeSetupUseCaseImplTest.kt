package com.imdmp.converter.usecase.impl

import com.imdmp.converter.repository.database.dao.WalletDAO
import com.imdmp.converter.repository.database.entity.WalletEntity
import com.imdmp.converter.schema.WalletSchema
import com.imdmp.converter.usecase.FirstTimeSetupUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.verify

@ExperimentalCoroutinesApi
class FirstTimeSetupUseCaseImplTest {

    private lateinit var useCase: FirstTimeSetupUseCase

    @Mock
    lateinit var mockedWalletDAO: WalletDAO

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        useCase = FirstTimeSetupUseCaseImpl(mockedWalletDAO)
    }

    @Test
    fun `useCase invoke calls WalletDao with starting balance in EUR`() {
        val expectedWalletEntity = WalletEntity(WalletSchema.EUR, 1000.00)
        runTest {
            useCase(expectedWalletEntity)
            verify(mockedWalletDAO).insertWallet(expectedWalletEntity)

        }
    }
}