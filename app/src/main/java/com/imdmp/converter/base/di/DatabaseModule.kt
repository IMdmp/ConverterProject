package com.imdmp.converter.base.di

import android.app.Application
import androidx.room.Room
import com.imdmp.converter.repository.database.AppDatabase
import com.imdmp.converter.repository.database.dao.CurrencyDAO
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
            .build()
    }

    @Provides
    fun provideCurrencyDao(db: AppDatabase): CurrencyDAO {
        return db.currencyDao()
    }
}