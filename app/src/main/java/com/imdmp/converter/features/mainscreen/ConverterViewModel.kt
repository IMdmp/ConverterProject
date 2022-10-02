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
import com.imdmp.converter.schema.ConvertCurrencyFlowSchema
import com.imdmp.converter.schema.ConvertCurrencySchema
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
import kotlinx.coroutines.flow.collectLatest
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

            characterInput.collectLatest {
                val value = converterViewState.value!!

                when (selectedInputBox) {
                    SelectedInputBox.RECEIVE -> {
                        val result = sanitizeData(value.receiveCurrencyData, it)
                        runMain {
                            _converterViewState.value =
                                value.copy(
                                    receiveCurrencyData = result
                                )
                        }

                        if (result.isNotBlank()) {
                            retrieveData(result.toDouble()) { convertSchema ->
                                _converterViewState.postValue(
                                    converterViewState.value?.copy(
                                        receiveCurrencyData = convertSchema.convertedCurrencyResult.toString()
                                    )
                                )
                            }

                            submitButtonEnabled.value = true
                        } else {
                            runMain {
                                _converterViewState.value = converterViewState.value?.copy(
                                    sellCurrencyData = "",
                                    retrievingRate = false
                                )
                            }
                        }
                    }

                    SelectedInputBox.SELL -> {
                        val result = sanitizeData(value.sellCurrencyData, it)
                        runMain {
                            _converterViewState.value =
                                value.copy(
                                    sellCurrencyData = result
                                )
                        }



                        if (result.isNotBlank()) {
                            retrieveData(result.toDouble()) { convertSchema ->
                                _converterViewState.postValue(
                                    converterViewState.value?.copy(
                                        receiveCurrencyData = convertSchema.convertedCurrencyResult.toString()
                                    )
                                )
                            }

                            submitButtonEnabled.value = true
                        } else {
                            runMain {
                                _converterViewState.value = converterViewState.value?.copy(
                                    receiveCurrencyData = "",
                                    retrievingRate = false
                                )
                            }
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
        } else if (char == CANCEL_CHAR && existingStringData.isEmpty()) {
            return existingStringData
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
                        _converterViewState.value = (
                            converterViewState.value?.copy(
                                converterDataLoading = false,
                            )
                            )
                        sendEvent(Event.ShowError("Error converting"))
                    }
                    is ConvertUserWalletResultSchema.Loading -> {
                        _converterViewState.value = (
                            converterViewState.value?.copy(
                                converterDataLoading = true,
                            )
                            )
                        submitButtonEnabled.value = false
                    }
                    is ConvertUserWalletResultSchema.Success -> {
                        _converterViewState.value = (
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

    private suspend fun retrieveData(
        data: Double,
        onSuccess: (convert: ConvertCurrencySchema) -> Unit
    ) {
        converterViewState.value?.let {
            val resultData =
                convertCurrencyUseCase(data, it.sellCurrencyLabel, it.receiveCurrencyLabel)

            resultData.collect { convertSchema ->
                when (convertSchema) {
                    is ConvertCurrencyFlowSchema.Error -> {
                        runMain {

                            _converterViewState.value = (
                                converterViewState.value?.copy(retrievingRate = false)
                                )
                        }
                        sendEvent(Event.ShowError("Error getting exchange rates"))

                    }
                    is ConvertCurrencyFlowSchema.Loading ->
                        runMain {
                            _converterViewState.value = (
                                converterViewState.value?.copy(retrievingRate = true)
                                )
                        }
                    is ConvertCurrencyFlowSchema.Success -> {
                        runMain {
                            _converterViewState.value = (
                                converterViewState.value?.copy(retrievingRate = false)
                                )
                        }
                        onSuccess(
                            ConvertCurrencySchema(
                                convertSchema.convertedCurrencyResult,
                                convertSchema.timeStamp,
                                convertSchema.rate
                            )
                        )
                    }
                }
            }
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

    private suspend fun runMain(unit: () -> Unit) {
        withContext(Dispatchers.Main) {
            unit()
        }
    }
}