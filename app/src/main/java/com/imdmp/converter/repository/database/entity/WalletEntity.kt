package com.imdmp.converter.repository.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "UserWallet")
data class WalletEntity(
    @PrimaryKey val currencyId: String,
    val balance: Double
) {


}