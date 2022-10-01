package com.imdmp.converter.repository.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.imdmp.converter.repository.database.entity.CurrencyEntity

@Dao
interface CurrencyDAO {

    @Query("SELECT * FROM currency")
    fun getAllCurrencies(): List<CurrencyEntity>?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAllCurrencies(currencyEntityList: List<CurrencyEntity>)
}