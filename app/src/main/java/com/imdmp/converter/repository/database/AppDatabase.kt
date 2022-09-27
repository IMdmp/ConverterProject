package com.imdmp.converter.repository.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.imdmp.converter.repository.database.dao.CurrencyDAO
import com.imdmp.converter.repository.database.dao.WalletDAO
import com.imdmp.converter.repository.database.entity.CurrencyEntity
import com.imdmp.converter.repository.database.entity.WalletEntity

@Database(entities = [CurrencyEntity::class, WalletEntity::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun currencyDao(): CurrencyDAO
    abstract fun walletDao(): WalletDAO
}