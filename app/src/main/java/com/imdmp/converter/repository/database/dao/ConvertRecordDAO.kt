package com.imdmp.converter.repository.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.imdmp.converter.repository.database.entity.ConvertRecordEntity

@Dao
interface ConvertRecordDAO {

    @Query("SELECT * FROM convertrecord")
    fun getAllRecords(): List<ConvertRecordEntity>

    @Query("SELECT COUNT(*) FROM convertrecord")
    fun getRecordCount(): Int

    @Insert
    fun insertRecord(convertRecordEntity: ConvertRecordEntity)

}