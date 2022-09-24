package com.imdmp.converter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Text
import androidx.lifecycle.lifecycleScope
import com.imdmp.converter.ui.theme.ConverterTheme
import com.imdmp.converter.usecase.PullRatesAndSaveCurrenciesUseCase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity: ComponentActivity() {

    @Inject
    lateinit var pullRatesAndSaveCurrenciesUseCase: PullRatesAndSaveCurrenciesUseCase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            pullRatesAndSaveCurrenciesUseCase()
        }

        setContent {
            ConverterTheme {
                Text("test")
            }
        }
    }
}