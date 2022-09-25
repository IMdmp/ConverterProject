package com.imdmp.converter

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.fragment.app.FragmentActivity
import com.imdmp.converter.features.mainscreen.ConverterScreen
import com.imdmp.converter.features.mainscreen.ConverterScreenCallbacks
import com.imdmp.converter.features.mainscreen.ConverterViewModel
import com.imdmp.converter.features.mainscreen.TransactionType
import com.imdmp.converter.features.mainscreen.currencypicker.CurrencyModel
import com.imdmp.converter.features.mainscreen.currencypicker.CurrencyPickerBottomDialogFragment
import com.imdmp.converter.features.mainscreen.currencypicker.CurrencyPickerListener
import com.imdmp.converter.features.ui.theme.ConverterTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity: FragmentActivity() {
    companion object {
        private const val CURRENCY_PICKER_DIALOG_TAG = "currencyPicker"
    }

    private val converterViewModel by viewModels<ConverterViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ConverterTheme {
                ConverterScreen(converterViewModel, object: ConverterScreenCallbacks {
                    override fun openCurrencyPicker(transactionType: TransactionType) {
                        openBottomSheet(transactionType)
                    }
                })
            }
        }
    }

    private fun openBottomSheet(transactionType: TransactionType) {
        CurrencyPickerBottomDialogFragment().apply {

            listener = object: CurrencyPickerListener {
                override fun currencySelected(currencyModel: CurrencyModel) {
                    converterViewModel.updateCurrency(currencyModel, transactionType)
                }
            }

            show(
                this@MainActivity.supportFragmentManager,
                CURRENCY_PICKER_DIALOG_TAG
            )
        }
    }
}