package com.imdmp.converter

import android.app.Application
import com.facebook.flipper.android.AndroidFlipperClient
import com.facebook.flipper.android.utils.FlipperUtils
import com.facebook.flipper.plugins.databases.DatabasesFlipperPlugin
import com.facebook.flipper.plugins.inspector.DescriptorMapping
import com.facebook.flipper.plugins.inspector.InspectorFlipperPlugin
import com.facebook.soloader.SoLoader

object FlipperInit {
    fun initFlipper(applicationContext: Application) {
        SoLoader.init(applicationContext, false);

        if (BuildConfig.DEBUG && FlipperUtils.shouldEnableFlipper(applicationContext)) {
            val client = AndroidFlipperClient.getInstance(applicationContext)
            client.addPlugin(
                InspectorFlipperPlugin(
                    applicationContext,
                    DescriptorMapping.withDefaults()
                )
            )
            client.addPlugin(DatabasesFlipperPlugin(applicationContext))
            client.start()
        }
    }
}