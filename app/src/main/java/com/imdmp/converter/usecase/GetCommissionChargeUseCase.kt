package com.imdmp.converter.usecase

interface GetCommissionChargeUseCase {
    suspend operator fun invoke(value: Double): Double
}