package com.imdmp.converter.base

import android.app.Application
import com.imdmp.converter.FlipperInit
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