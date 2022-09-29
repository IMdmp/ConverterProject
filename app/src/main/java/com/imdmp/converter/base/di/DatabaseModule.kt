package com.imdmp.converter.base.di

import android.app.Application
import androidx.room.Room
import com.google.gson.Gson
import com.imdmp.converter.repository.database.AppDatabase
import com.imdmp.converter.repository.database.dao.ConvertRecordDAO
import com.imdmp.converter.repository.database.dao.CurrencyDAO
import com.imdmp.converter.repository.database.dao.WalletDAO
import com.imdmp.converter.repository.database.typeconverter.ConvertRecordTypeConverter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    @Provides
    fun provideDb(applicationContext: Application): AppDatabase {
        return Room.databaseBuilder(applicationContext, AppDatabase::class.java, "converter")
            .addTypeConverter(ConvertRecordTypeConverter(Gson()))
            .build()
    }

    @Provides
    fun provideCurrencyDao(db: AppDatabase): CurrencyDAO {
        return db.currencyDao()
    }

    @Provides
    fun provideWalletDao(db: AppDatabase): WalletDAO {
        return db.walletDao()
    }

    @Provides
    fun provideConvertRecordDao(db: AppDatabase): ConvertRecordDAO {
        return db.convertRecordDao()
    }
}