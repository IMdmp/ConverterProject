package com.imdmp.converter.repository.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "currency")
data class CurrencyEntity(
    @PrimaryKey val currencyId: String
) {
}