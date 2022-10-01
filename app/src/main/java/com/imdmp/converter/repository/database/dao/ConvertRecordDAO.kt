package com.imdmp.converter.repository.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.imdmp.converter.repository.database.entity.ConvertRecordEntity
import com.imdmp.converter.repository.database.entity.WalletEntity

@Dao
interface ConvertRecordDAO {

    @Query("SELECT * FROM convertrecord")
    fun getAllRecords(): List<ConvertRecordEntity>?

    @Query("SELECT COUNT(*) FROM convertrecord")
    fun getRecordCount(): Int?

    @Insert
    fun insertRecord(convertRecordEntity: ConvertRecordEntity)

    @Query("SELECT * FROM convertrecord WHERE sellData =:walletEntity")
    fun getTransactionsWithCurrencyLike(walletEntity: WalletEntity): List<ConvertRecordEntity>

}