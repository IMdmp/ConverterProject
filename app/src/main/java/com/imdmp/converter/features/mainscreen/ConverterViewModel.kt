package com.imdmp.converter.features.mainscreen

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    val sellCurrency get() = _sellCurrency.value

    private val _receiveCurrency: MutableLiveData<String> by lazy { MutableLiveData() }
    val receiveCurrency get() = _receiveCurrency.value

    init {
        _sellCurrency.value = "EUR"
        _receiveCurrency.value = "EUR"
    }


    fun convertData(sellData: String) {

        sellData.let {
            viewModelScope.launch(Dispatchers.IO) {
                val resultData = convertCurrencyUseCase(sellData.toDouble(), "EUR", "USD")

                Timber.d("result data : $resultData")
            }
        }
    }
}