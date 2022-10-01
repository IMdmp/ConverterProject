package com.imdmp.converter.repository.database.typeconverter

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.imdmp.converter.repository.database.entity.WalletEntity
import javax.inject.Inject

@ProvidedTypeConverter
class ConvertRecordTypeConverter @Inject constructor(private val gson: Gson) {

    @TypeConverter
    fun StringToWalletEntity(string: String): WalletEntity {
        return gson.fromJson(string, WalletEntity::class.java)
    }

    @TypeConverter
    fun WalletEntityToString(walletEntity: WalletEntity): String {
        return gson.toJson(walletEntity)
    }

}