package com.imdmp.converter.features.mainscreen

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imdmp.converter.features.mainscreen.currencypicker.CurrencyModel
import com.imdmp.converter.usecase.ConvertCurrencyUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ConverterViewModel @Inject constructor(
    private val convertCurrencyUseCase: ConvertCurrencyUseCase
): ViewModel() {
    private val _sellCurrency: MutableLiveData<String> by lazy { MutableLiveData() }
    val sellCurrency get() = _sellCurrency

    private val _receiveCurrency: MutableLiveData<String> by lazy { MutableLiveData() }
    val receiveCurrency get() = _receiveCurrency

    init {
        _sellCurrency.value = "EUR"
        _receiveCurrency.value = "USD"
    }


    fun convertData(sellData: String) {

        sellData.let {
            viewModelScope.launch(Dispatchers.IO) {
                val resultData = convertCurrencyUseCase(sellData.toDouble(), "EUR", "USD")

                Timber.d("result data : $resultData")
            }
        }
    }

    fun updateCurrency(currencyModel: CurrencyModel, transactionType: TransactionType) {
        when (transactionType) {
            TransactionType.RECEIVE -> {
                if (currencyModel.abbrev == _sellCurrency.value) {
                    switchCurrency()
                } else {
                    _receiveCurrency.value = currencyModel.abbrev
                }
            }
            TransactionType.SELL -> {
                if (currencyModel.abbrev == _receiveCurrency.value) {
                    switchCurrency()
                } else {
                    _sellCurrency.value = currencyModel.abbrev
                }

            }
        }
    }

    fun switchCurrency() {
        val temp = _receiveCurrency.value
        _receiveCurrency.value = _sellCurrency.value
        _sellCurrency.value = temp
    }
}