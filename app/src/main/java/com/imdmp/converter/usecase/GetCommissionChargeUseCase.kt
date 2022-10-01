package com.imdmp.converter.usecase

import com.imdmp.converter.schema.WalletSchema

interface GetCommissionChargeUseCase {
    suspend operator fun invoke(sellData: WalletSchema): Double
}