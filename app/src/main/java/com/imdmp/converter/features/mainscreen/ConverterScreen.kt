package com.imdmp.converter.features.mainscreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout

@Composable
fun ConverterScreen(converterViewModel: ConverterViewModel) {
    ConverterScreen(
        sellCurrency = converterViewModel.sellCurrency ?: "",
        receiveCurrency = converterViewModel.receiveCurrency ?: "",
        convertData = {
            converterViewModel.convertData(it.toString())
        })
}

@Composable
private fun ConverterScreen(
    sellCurrency: String,
    receiveCurrency: String,
    convertData: (d: Double) -> Unit
) {
    val sellData = remember { mutableStateOf("") }
    val receiveData = remember { mutableStateOf("") }
    Scaffold(
        topBar = { TopAppBar(title = { Text("Currency Converter") }) }
    ) {
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
            val (balanceRow, currencyLabel, sellRow, receiveRow, submitButton) = createRefs()

            BalanceRow(modifier = Modifier
                .padding(top = 16.dp)
                .constrainAs(balanceRow) {
                    top.linkTo(parent.top)
                })

            Text(
                modifier = Modifier.constrainAs(currencyLabel) {
                    top.linkTo(balanceRow.bottom, 16.dp)
                    start.linkTo(parent.start)
                }, text = "Currency Exchange"
            )

            SellRow(modifier = Modifier
                .padding(top = 8.dp, bottom = 4.dp)
                .constrainAs(sellRow) {
                    top.linkTo(currencyLabel.bottom, 16.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }, sellCurrency ?: "", sellData, {})

            ReceiveRow(modifier = Modifier
                .padding(top = 4.dp, bottom = 8.dp)
                .constrainAs(receiveRow) {
                    top.linkTo(sellRow.bottom)
                }, receiveCurrency ?: "", receiveData, {})


            Button(modifier = Modifier
                .padding(top = 16.dp)
                .constrainAs(submitButton) {
                    top.linkTo(receiveRow.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
                onClick = { convertData(sellData.value.toDouble()) }) {
                Text("Submit")
            }

        }
    }
}

@Composable
fun BalanceRow(modifier: Modifier) {
    val state = rememberScrollState()
    Column(modifier = modifier.fillMaxWidth()) {
        Text(modifier = Modifier.align(Alignment.Start), text = "My Balances")
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
                .horizontalScroll(state)
        ) {
            repeat(10) {
                Text(modifier = Modifier.padding(end = 16.dp), text = "1000.00 EUR")
            }
        }
    }
}

@Composable
fun ConvertRow(
    modifier: Modifier,
    type: String,
    data: MutableState<String>,
    currency: String,
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
    data: MutableState<String>,
    currency: String,
    onCurrencyClicked: () -> Unit
) {

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            modifier = Modifier.width(240.dp),
            value = data.value,
            textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.End),
            keyboardOptions =
            KeyboardOptions.Default.copy(keyboardType = KeyboardType.Decimal),
            onValueChange = { s: String -> data.value = s })

        Text(currency, Modifier
            .padding(start = 16.dp)
            .clickable {
                onCurrencyClicked()
            })
    }
}

@Composable
fun SellRow(
    modifier: Modifier,
    currency: String,
    data: MutableState<String>,
    onCurrencyClicked: () -> Unit
) {
    ConvertRow(modifier = modifier, "Sell", data, currency, onCurrencyClicked)
}

@Composable
fun ReceiveRow(
    modifier: Modifier,
    currency: String,
    data: MutableState<String>,
    onCurrencyClicked: () -> Unit
) {
    ConvertRow(modifier = modifier, "Receive", data, currency, onCurrencyClicked)
}

@Preview
@Composable
fun PreviewConverterScreen() {
    ConverterScreen(sellCurrency = "EUR", receiveCurrency = "USD") {}
}