package com.imdmp.converter

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class ConverterApp: Application() {


    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())

        FlipperInit.initFlipper(this)

    }
}