package com.imdmp.converter.features.mainscreen

import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.imdmp.converter.base.BaseViewModel
import com.imdmp.converter.features.mainscreen.currencypicker.CurrencyModel
import com.imdmp.converter.schema.ConvertUserWalletResultSchema
import com.imdmp.converter.schema.WalletSchema
import com.imdmp.converter.usecase.ConvertCurrencyUseCase
import com.imdmp.converter.usecase.ConvertUserWalletCurrencyUseCase
import com.imdmp.converter.usecase.GetWalletBalanceUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ConverterViewModel @Inject constructor(
    private val convertCurrencyUseCase: ConvertCurrencyUseCase,
    private val getWalletBalanceUseCase: GetWalletBalanceUseCase,
    private val convertUserWalletCurrencyUseCase: ConvertUserWalletCurrencyUseCase,
): BaseViewModel(), ConverterScreenCallbacks {
    private val _converterViewState = MutableLiveData(ConverterViewState.init())
    val converterViewState: LiveData<ConverterViewState> get() = _converterViewState

    private val _walletBalance = MutableLiveData<SnapshotStateList<WalletSchema>>()
    val walletBalance: LiveData<SnapshotStateList<WalletSchema>> get() = _walletBalance

    var isLoading = MutableLiveData(false)

    init {
        viewModelScope.launch(Dispatchers.IO) {
            updateWalletBalance()
        }
    }

    fun convertCurrency() {
        viewModelScope.launch(Dispatchers.IO) {
            val value = converterViewState.value!!
            val sellWalletSchema = WalletSchema(
                currencyAbbrev = value.sellCurrencyLabel,
                currencyValue = value.sellCurrencyData
            )
            val buyWalletSchema = WalletSchema(
                currencyAbbrev = value.receiveCurrencyLabel,
                currencyValue = value.receiveCurrencyData
            )

            convertUserWalletCurrencyUseCase(
                sellWalletSchema = sellWalletSchema,
                buyWalletSchema = buyWalletSchema,
            ).collect { result ->
                when (result) {
                    is ConvertUserWalletResultSchema.Error -> {
                        isLoading.postValue(false)
                        sendEvent(Event.ShowError)
                    }
                    is ConvertUserWalletResultSchema.Loading -> {
                        isLoading.postValue(true)
                    }
                    is ConvertUserWalletResultSchema.Success -> {
                        isLoading.postValue(false)
                        updateWalletBalance()
                        sendEvent(
                            Event.ShowSnackbarString(
                                "You have converted ${result.sellData.currencyValue} ${result.sellData.currencyAbbrev} " +
                                    "to ${result.buyData.currencyValue} ${result.buyData.currencyAbbrev}. Commission Fee - ${result.commissionCharged} ${result.sellData.currencyAbbrev}."
                            )
                        )
                    }
                }
            }
        }
    }

    private suspend fun updateWalletBalance() {
        val walletData = getWalletBalanceUseCase().toMutableStateList()
        _walletBalance.postValue(walletData)
    }

    fun updateCurrency(currencyModel: CurrencyModel, transactionType: TransactionType) {
        when (transactionType) {
            TransactionType.RECEIVE -> {
                if (currencyModel.abbrev == converterViewState.value?.sellCurrencyLabel) {
                    switchCurrencyLabels()
                } else {
                    _converterViewState.value =
                        converterViewState.value?.copy(receiveCurrencyLabel = currencyModel.abbrev)
                }
            }
            TransactionType.SELL -> {
                if (currencyModel.abbrev == converterViewState.value?.receiveCurrencyLabel) {
                    switchCurrencyLabels()
                } else {
                    _converterViewState.value =
                        converterViewState.value?.copy(sellCurrencyLabel = currencyModel.abbrev)
                }
            }
        }
    }



    override fun onSellDataUpdated(data: Double) {
        viewModelScope.launch(Dispatchers.IO) {
            converterViewState.value?.let {
                val resultData =
                    convertCurrencyUseCase(data, it.sellCurrencyLabel, it.receiveCurrencyLabel)
                withContext(Dispatchers.Main) {
                    _converterViewState.value = converterViewState.value?.copy(
                        receiveCurrencyData = resultData,
                        sellCurrencyData = data
                    )
                }
            }
        }
    }

    override fun onBuyDataUpdated(data: Double) {
        viewModelScope.launch(Dispatchers.IO) {
            converterViewState.value?.let {
                val resultData =
                    convertCurrencyUseCase(data, it.sellCurrencyLabel, it.receiveCurrencyLabel)
                withContext(Dispatchers.Main) {
                    _converterViewState.value = converterViewState.value?.copy(
                        sellCurrencyData = resultData,
                        receiveCurrencyData = data,
                    )
                }
            }
        }
    }

    override fun switchCurrencyLabels() {
        converterViewState.value?.also {
            val temp = it.receiveCurrencyLabel
            _converterViewState.value = it.copy(
                receiveCurrencyLabel = it.sellCurrencyLabel,
                sellCurrencyLabel = temp
            )
        }
    }

}