package com.imdmp.converter.features.mainscreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.SwapVerticalCircle
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.imdmp.converter.base.BaseViewModel
import com.imdmp.converter.schema.WalletSchema
import kotlinx.coroutines.flow.collectLatest

enum class TransactionType {
    SELL, RECEIVE
}

@Composable
fun ConverterScreen(
    converterViewModel: ConverterViewModel,
    converterScreenActivityCallbacks: ConverterScreenActivityCallbacks
) {
    val viewState =
        converterViewModel.converterViewState.observeAsState(ConverterViewState.init()).value
    val walletListState = converterViewModel.walletBalance.observeAsState().value
    val state = rememberScaffoldState()
    val isLoading = converterViewModel.isLoading.observeAsState().value ?: false

    LaunchedEffect(key1 = Unit) {
        converterViewModel.eventsFlow.collectLatest { value ->
            when (value) {
                // Handle events
                is BaseViewModel.Event.ShowSnackbarString -> {
                    state.snackbarHostState.showSnackbar(
                        value.message
                    )
                }

                is BaseViewModel.Event.ShowError -> {
                    state.snackbarHostState.showSnackbar(
                        "ERROR!"
                    )
                }
            }
        }
    }

    ConverterScreen(
        isLoading = isLoading,
        viewState = viewState,
        walletList = walletListState?.toList() ?: listOf(),
        converterScreenCallbacks = converterViewModel,
        switchCurrency = {
            converterViewModel.switchCurrency()
        },
        scaffoldState = state,
        convertData = {
            converterViewModel.convertCurrency()
            converterScreenActivityCallbacks.hideKeyboard()
        }) {
        converterScreenActivityCallbacks.openCurrencyPicker(transactionType = it)
    }
}

@Composable
private fun ConverterScreen(
    isLoading: Boolean,
    viewState: ConverterViewState,
    walletList: List<WalletSchema>,
    converterScreenCallbacks: ConverterScreenCallbacks,
    scaffoldState: ScaffoldState,
    convertData: () -> Unit,
    switchCurrency: () -> Unit,
    currencySelected: (t: TransactionType) -> Unit
) {

    val sellCurrency = viewState.sellCurrencyLabel
    val receiveCurrency = viewState.receiveCurrencyLabel

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = { TopAppBar(title = { Text("Currency Converter") }) }
    ) { it ->
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = it.calculateBottomPadding(),
                    start = 8.dp,
                    end = 8.dp,
                    bottom = 8.dp
                )
        ) {
            val (balanceRow, currencyLabel,
                sellRow, receiveRow,
                switchCurrencyButton, submitButton) = createRefs()

            BalanceRow(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .constrainAs(balanceRow) {
                        top.linkTo(parent.top)
                    },
                walletList = walletList
            )

            Text(
                modifier = Modifier.constrainAs(currencyLabel) {
                    top.linkTo(balanceRow.bottom, 16.dp)
                    start.linkTo(parent.start)
                }, text = "Currency Exchange"
            )

            ConvertRow(
                modifier = Modifier
                    .padding(top = 8.dp, bottom = 4.dp)
                    .constrainAs(sellRow) {
                        top.linkTo(currencyLabel.bottom, 16.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                currency = sellCurrency,
                data = viewState.sellCurrencyData.toString(),
                type = "Sell",
                onValueUpdate = { sellData ->
                    if (sellData.isNotBlank()) {
                        converterScreenCallbacks.onSellDataUpdated(sellData.toDouble())
                    }
                }
            ) {
                currencySelected(TransactionType.SELL)
            }

            IconButton(
                modifier = Modifier
                    .constrainAs(switchCurrencyButton) {
                        top.linkTo(sellRow.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .size(24.dp),
                onClick = {
                    switchCurrency()
                }) {
                Icon(Icons.Rounded.SwapVerticalCircle, "swap currency")
            }

            ConvertRow(
                modifier = Modifier
                    .padding(top = 4.dp, bottom = 8.dp)
                    .constrainAs(receiveRow) {
                        top.linkTo(switchCurrencyButton.bottom)
                    },
                currency = receiveCurrency,
                data = viewState.receiveCurrencyData.toString(),
                type = "Receive",
                onValueUpdate = { receiveData ->
                    if (receiveData.isNotBlank()) {
                        converterScreenCallbacks.onBuyDataUpdated(receiveData.toDouble())
                    }
                }
            ) {
                currencySelected(TransactionType.RECEIVE)
            }


            Button(modifier = Modifier
                .padding(top = 16.dp)
                .constrainAs(submitButton) {
                    top.linkTo(receiveRow.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
                onClick = {
                    convertData()
                }) {
                Text("Submit")
            }

        }
    }

    if (isLoading) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(style = MaterialTheme.typography.h1, text = "currently loading.")
        }
    }
}

@Composable
fun BalanceRow(modifier: Modifier, walletList: List<WalletSchema>) {
    val state = rememberScrollState()
    Column(modifier = modifier.fillMaxWidth()) {
        Text(modifier = Modifier.align(Alignment.Start), text = "My Balances")
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
                .horizontalScroll(state)
        ) {
            walletList.forEach {
                Text(
                    modifier = Modifier.padding(end = 16.dp),
                    text = "${it.currencyValue} ${it.currencyAbbrev}"
                )
            }
        }
    }
}

@Composable
fun ConvertRow(
    modifier: Modifier,
    type: String,
    data: String,
    currency: String,
    onValueUpdate: (s: String) -> Unit,
    onCurrencyClicked: () -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ConvertRowLabel(
            modifier.weight(0.2f),
            type = type
        )
        ConvertRowDataExchange(
            modifier.weight(0.8f),
            data = data,
            currency = currency,
            onValueUpdate = onValueUpdate,
            onCurrencyClicked = onCurrencyClicked
        )
    }
}

@Composable
private fun ConvertRowLabel(modifier: Modifier = Modifier, type: String) {
    Text(modifier = modifier, text = type)
}

@Composable
fun ConvertRowDataExchange(
    modifier: Modifier = Modifier,
    data: String,
    currency: String,
    onValueUpdate: (s: String) -> Unit = {},
    onCurrencyClicked: () -> Unit
) {

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            modifier = Modifier.width(240.dp),
            value = data,
            textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.End),
            keyboardOptions =
            KeyboardOptions.Default.copy(keyboardType = KeyboardType.Decimal),
            onValueChange = { s: String ->
                onValueUpdate(s)
            })

        Text(currency, Modifier
            .padding(start = 16.dp)
            .clickable {
                onCurrencyClicked()
            })
    }
}

@Preview
@Composable
fun PreviewConverterScreen() {
    ConverterScreen(
        isLoading = false,
        viewState = ConverterViewState.init(),
        listOf(),
        ConverterScreenCallbacks.default(),
        rememberScaffoldState(),
        {},
        {}) {}
}