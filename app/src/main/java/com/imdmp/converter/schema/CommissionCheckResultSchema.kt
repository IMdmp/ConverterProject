package com.imdmp.converter.schema

sealed class CommissionCheckResultSchema(

) {
    object Loading: CommissionCheckResultSchema()

    data class Error(val message: String): CommissionCheckResultSchema()

    object Success: CommissionCheckResultSchema()
}