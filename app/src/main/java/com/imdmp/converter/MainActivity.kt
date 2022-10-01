package com.imdmp.converter

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.fragment.app.FragmentActivity
import com.imdmp.converter.features.mainscreen.ConverterScreen
import com.imdmp.converter.features.mainscreen.ConverterScreenActivityCallbacks
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
                ConverterScreen(converterViewModel, object: ConverterScreenActivityCallbacks {
                    override fun openCurrencyPicker(transactionType: TransactionType) {
                        openBottomSheet(transactionType)
                    }

                    override fun hideKeyboard() {
                        val view: View = this@MainActivity.findViewById(android.R.id.content)

                        val imm: InputMethodManager =
                            this@MainActivity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.hideSoftInputFromWindow(view.windowToken, 0)
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