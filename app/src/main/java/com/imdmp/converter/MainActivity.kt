package com.imdmp.converter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.imdmp.converter.features.mainscreen.ConverterScreen
import com.imdmp.converter.features.mainscreen.ConverterViewModel
import com.imdmp.converter.ui.theme.ConverterTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity: ComponentActivity() {

    private val converterViewModel by viewModels<ConverterViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ConverterTheme {
                ConverterScreen(converterViewModel)
            }
        }
    }
}