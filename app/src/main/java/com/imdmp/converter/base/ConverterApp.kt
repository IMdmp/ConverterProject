package com.imdmp.converter.base

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.imdmp.converter.FlipperInit
import com.imdmp.converter.repository.database.entity.WalletEntity
import com.imdmp.converter.schema.WalletSchema
import com.imdmp.converter.usecase.FirstTimeSetupUseCase
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class ConverterApp: Application() {

    @Inject
    lateinit var firstTimeSetupUseCase: FirstTimeSetupUseCase

    companion object {
        const val WALLET_KEY = "wallet_key"
        const val FIRST_TIME_SETUP_DONE = "fts"
    }

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())

        FlipperInit.initFlipper(this)

        preConfig()
    }

    private fun preConfig() {
        val sp = this.getSharedPreferences(
            WALLET_KEY, Context.MODE_PRIVATE
        )

        if (firstTimeSetupNotFinished(sp)) {
            MainScope().launch(Dispatchers.IO) {
                setupWallet()
                finishSetupPreConfig(sp)
            }
        }
    }

    private fun firstTimeSetupNotFinished(sp: SharedPreferences): Boolean {
        return sp.getBoolean(FIRST_TIME_SETUP_DONE, false).not()
    }

    private suspend fun setupWallet() {

        firstTimeSetupUseCase(
            listOf(
                WalletEntity(WalletSchema.EUR, 1000.00),
                WalletEntity(WalletSchema.USD, 0.0),
                WalletEntity(WalletSchema.BGN, 0.0),
                WalletEntity(WalletSchema.PHP, 0.0),
                WalletEntity(WalletSchema.YEN, 0.0),

                )
        )


    }


    private fun finishSetupPreConfig(sharedPreferences: SharedPreferences) {
        with(sharedPreferences.edit()) {
            putBoolean(FIRST_TIME_SETUP_DONE, true)
        }
    }
}