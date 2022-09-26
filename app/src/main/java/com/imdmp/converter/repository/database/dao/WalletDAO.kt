package com.imdmp.converter.repository.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.imdmp.converter.repository.database.entity.WalletEntity

@Dao
interface WalletDAO {

    @Query("SELECT * FROM userwallet")
    fun getAllWalletData(): List<WalletEntity>

    @Insert(onConflict = REPLACE)
    fun insertWallet(walletEntity: WalletEntity)
}