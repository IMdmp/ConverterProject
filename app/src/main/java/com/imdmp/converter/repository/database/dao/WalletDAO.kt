package com.imdmp.converter.repository.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.imdmp.converter.repository.database.entity.WalletEntity

@Dao
interface WalletDAO {

    @Query("SELECT * FROM UserWallet")
    fun getAllWalletData(): List<WalletEntity>?

    @Insert(onConflict = REPLACE)
    fun insertWallet(walletEntity: WalletEntity)

    @Insert(onConflict = REPLACE)
    fun insertAllWallet(walletEntity: List<WalletEntity>)

    @Query("SELECT * FROM UserWallet WHERE currencyId = :currencyId ")
    fun getWallet(currencyId: String): WalletEntity?
}