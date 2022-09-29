package com.imdmp.converter.repository.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "convertrecord")
data class ConvertRecordEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val sellData: WalletEntity,
    val buyData: WalletEntity,
) {
}