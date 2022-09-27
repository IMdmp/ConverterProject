package com.imdmp.converter.features.mainscreen

import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imdmp.converter.features.mainscreen.currencypicker.CurrencyModel
import com.imdmp.converter.schema.WalletSchema
import com.imdmp.converter.usecase.ConvertCurrencyUseCase
import com.imdmp.converter.usecase.GetWalletBalanceUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ConverterViewModel @Inject constructor(
    private val convertCurrencyUseCase: ConvertCurrencyUseCase,
    private val getWalletBalanceUseCase: GetWalletBalanceUseCase,
): ViewModel() {
    private val _converterViewState = MutableLiveData<ConverterViewState>()
    val converterViewState: LiveData<ConverterViewState> get() = _converterViewState

    private val _walletBalance = MutableLiveData<SnapshotStateList<WalletSchema>>()
    val walletBalance: LiveData<SnapshotStateList<WalletSchema>> get() = _walletBalance

    init {
        _converterViewState.value = ConverterViewState.init().copy(
            sellCurrencyLabel = "PHP",
            sellCurrencyData = 0.0,
            receiveCurrencyLabel = "MYR",
            receiveCurrencyData = 0.0,
        )
        viewModelScope.launch(Dispatchers.IO) {
            val walletData = getWalletBalanceUseCase().toMutableStateList()

            _walletBalance.postValue(walletData)
        }
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
                if (currencyModel.abbrev == converterViewState.value?.sellCurrencyLabel) {
                    switchCurrency()
                } else {
                    _converterViewState.value =
                        converterViewState.value?.copy(receiveCurrencyLabel = currencyModel.abbrev)
                }
            }
            TransactionType.SELL -> {
                if (currencyModel.abbrev == converterViewState.value?.receiveCurrencyLabel) {
                    switchCurrency()
                } else {
                    _converterViewState.value =
                        converterViewState.value?.copy(sellCurrencyLabel = currencyModel.abbrev)
                }
            }
        }
    }

    fun switchCurrency() {
        converterViewState.value?.also {
            val temp = it.receiveCurrencyLabel
            _converterViewState.value = it.copy(
                receiveCurrencyLabel = it.sellCurrencyLabel,
                sellCurrencyLabel = temp
            )
        }
    }
}