package com.imdmp.converter.repository.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.imdmp.converter.repository.database.dao.ConvertRecordDAO
import com.imdmp.converter.repository.database.dao.CurrencyDAO
import com.imdmp.converter.repository.database.dao.WalletDAO
import com.imdmp.converter.repository.database.entity.ConvertRecordEntity
import com.imdmp.converter.repository.database.entity.CurrencyEntity
import com.imdmp.converter.repository.database.entity.WalletEntity
import com.imdmp.converter.repository.database.typeconverter.ConvertRecordTypeConverter

@Database(
    entities = [CurrencyEntity::class, WalletEntity::class, ConvertRecordEntity::class],
    version = 1
)
@TypeConverters(ConvertRecordTypeConverter::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun currencyDao(): CurrencyDAO
    abstract fun walletDao(): WalletDAO
    abstract fun convertRecordDao(): ConvertRecordDAO
}