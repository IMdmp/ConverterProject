package com.imdmp.converter.features.mainscreen

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.imdmp.converter.base.BaseViewModel
import com.imdmp.converter.features.mainscreen.currencypicker.CurrencyModel
import com.imdmp.converter.features.mainscreen.numberscreen.CANCEL_CHAR
import com.imdmp.converter.schema.ConvertUserWalletResultSchema
import com.imdmp.converter.schema.WalletSchema
import com.imdmp.converter.usecase.ConvertCurrencyUseCase
import com.imdmp.converter.usecase.ConvertUserWalletCurrencyUseCase
import com.imdmp.converter.usecase.GetAvailableCurrenciesUseCase
import com.imdmp.converter.usecase.GetWalletBalanceUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ConverterViewModel @Inject constructor(
    private val convertCurrencyUseCase: ConvertCurrencyUseCase,
    private val getWalletBalanceUseCase: GetWalletBalanceUseCase,
    private val convertUserWalletCurrencyUseCase: ConvertUserWalletCurrencyUseCase,
    private val getAvailableCurrenciesUseCase: GetAvailableCurrenciesUseCase,
): BaseViewModel(), ConverterScreenCallbacks {
    private val _converterViewState = MutableLiveData(ConverterViewState.init())
    val converterViewState: LiveData<ConverterViewState> get() = _converterViewState

    private val _walletBalance = MutableLiveData<SnapshotStateList<WalletSchema>>()
    val walletBalance: LiveData<SnapshotStateList<WalletSchema>> get() = _walletBalance

    var selectedInputBox = SelectedInputBox.NONE
    var submitButtonEnabled = mutableStateOf(false)

    private var characterInput = MutableSharedFlow<Char>()
    var showBalanceConvertSuccess = mutableStateOf(Pair(false, ""))

    init {
        viewModelScope.launch(Dispatchers.IO) {
            updateWalletBalance()
            getAvailableCurrenciesUseCase(true)

            characterInput.collect {
                val value = converterViewState.value!!

                when (selectedInputBox) {
                    SelectedInputBox.RECEIVE -> {
                        val result = sanitizeData(value.receiveCurrencyData, it)
                        _converterViewState.postValue(
                            value.copy(
                                receiveCurrencyData = result
                            )
                        )
                        if (result.isNotBlank()) {
                            onBuyDataUpdated(result.toDouble())
                            submitButtonEnabled.value = true
                        }
                    }

                    SelectedInputBox.SELL -> {
                        val result = sanitizeData(value.sellCurrencyData, it)
                        _converterViewState.postValue(
                            value.copy(
                                sellCurrencyData = result
                            )
                        )
                        if (result.isNotBlank()) {
                            onSellDataUpdated(result.toDouble())
                            submitButtonEnabled.value = true
                        }
                    }
                    else -> {}
                }
            }
        }
    }

    private fun inputFieldsHasData(): Boolean {
        return (converterViewState.value?.receiveCurrencyData?.isNotBlank() == true || converterViewState.value?.sellCurrencyData?.isNotBlank() == true)
    }

    private fun sanitizeData(existingStringData: String, char: Char): String {
        if (char == '.' && existingStringData.contains('.')) {
            return existingStringData
        } else if (char == CANCEL_CHAR && existingStringData.isNotEmpty()) {
            return existingStringData.dropLast(1)
        }

        return existingStringData.plus(char)
    }

    fun convertCurrency() {
        viewModelScope.launch(Dispatchers.IO) {
            val value = converterViewState.value!!
            val sellWalletSchema = WalletSchema(
                currencyAbbrev = value.sellCurrencyLabel,
                currencyValue = value.sellCurrencyData.toDouble()
            )
            val buyWalletSchema = WalletSchema(
                currencyAbbrev = value.receiveCurrencyLabel,
                currencyValue = value.receiveCurrencyData.toDouble()
            )

            convertUserWalletCurrencyUseCase(
                sellWalletSchema = sellWalletSchema,
                buyWalletSchema = buyWalletSchema,
            ).collect { result ->
                when (result) {
                    is ConvertUserWalletResultSchema.Error -> {
                        _converterViewState.postValue(
                            converterViewState.value?.copy(
                                converterDataLoading = false,
                            )
                        )
                        sendEvent(Event.ShowError)
                    }
                    is ConvertUserWalletResultSchema.Loading -> {
                        _converterViewState.postValue(
                            converterViewState.value?.copy(
                                converterDataLoading = true,
                            )
                        )
                        submitButtonEnabled.value = false
                    }
                    is ConvertUserWalletResultSchema.Success -> {
                        _converterViewState.postValue(
                            converterViewState.value?.copy(
                                converterDataLoading = false,
                            )
                        )
                        updateWalletBalance()
                        emptyFields()
                        showBalanceConvertSuccess.value = Pair(
                            true,
                            "You have converted ${result.sellData.currencyValue} ${result.sellData.currencyAbbrev} " +
                                "to ${result.buyData.currencyValue} ${result.buyData.currencyAbbrev}. Commission Fee - ${result.commissionCharged} ${result.sellData.currencyAbbrev}."
                        )
                        delay(3000)
                        showBalanceConvertSuccess.value = Pair(false, "")
                    }
                }
            }
        }
    }

    private fun emptyFields() {
        _converterViewState.postValue(
            converterViewState.value?.copy(receiveCurrencyData = "", sellCurrencyData = "")
        )
        submitButtonEnabled.value = false

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

    override fun convertAgainSelected() {
        showBalanceConvertSuccess.value = Pair(false, "")
    }


    override fun onSellDataUpdated(data: Double) {
        viewModelScope.launch(Dispatchers.IO) {
            converterViewState.value?.let {
                val resultData =
                    convertCurrencyUseCase(data, it.sellCurrencyLabel, it.receiveCurrencyLabel)
                withContext(Dispatchers.Main) {
                    _converterViewState.value = converterViewState.value?.copy(
                        receiveCurrencyData = resultData.toString(),
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
                        sellCurrencyData = resultData.toString(),
                    )
                }
            }
        }
    }

    override fun switchCurrencyLabels() {
        converterViewState.value?.also {
            val temp = it.receiveCurrencyLabel
            _converterViewState.postValue(
                it.copy(
                    receiveCurrencyLabel = it.sellCurrencyLabel,
                    sellCurrencyLabel = temp
                )
            )
        }
    }

    override fun inputBoxSelected(selectedInputBox: SelectedInputBox) {
        this.selectedInputBox = selectedInputBox

        when (selectedInputBox) {
            SelectedInputBox.NONE -> {}
            SelectedInputBox.SELL -> {
                emptyFields()
            }
            SelectedInputBox.RECEIVE -> {
                emptyFields()
            }
        }
    }

    override fun characterEmitted(c: Char) {
        viewModelScope.launch(Dispatchers.IO) {
            characterInput.emit(c)
        }
    }

}