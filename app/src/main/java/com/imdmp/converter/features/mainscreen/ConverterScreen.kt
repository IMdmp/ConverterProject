package com.imdmp.converter.features.mainscreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout

@Composable
fun ConverterScreen(converterViewModel: ConverterViewModel) {
    val sellData = remember { mutableStateOf("") }
    val receiveData = remember { mutableStateOf("") }

    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val (balanceRow, sellRow, receiveRow, submitButton) = createRefs()

        BalanceRow(modifier = Modifier.constrainAs(balanceRow) {
            top.linkTo(parent.top)
        })

        SellRow(modifier = Modifier.constrainAs(sellRow) {
            top.linkTo(balanceRow.bottom, 16.dp)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        }, converterViewModel.sellCurrency ?: "", sellData, {})

        ReceiveRow(modifier = Modifier.constrainAs(receiveRow) {
            top.linkTo(sellRow.bottom)
        }, converterViewModel.receiveCurrency ?: "", receiveData, {})


        Button(modifier = Modifier.constrainAs(submitButton) {
            top.linkTo(receiveRow.bottom)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        },
            onClick = { converterViewModel.convertData(sellData.value) }) {
            Text("Submit")
        }

    }
}

@Composable
fun BalanceRow(modifier: Modifier) {
    val state = rememberScrollState()
    Column(modifier = modifier.fillMaxWidth()) {
        Text(modifier = Modifier.align(Alignment.CenterHorizontally), text = "Balances")
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(state)
        ) {
            repeat(10) {
                Text(modifier = Modifier.padding(end = 16.dp), text = "1000 EUR")
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
    Row(modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(type)

        TextField(
            value = data.value,
            keyboardOptions =
            KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            onValueChange = { s: String -> data.value = s })

        Text(currency, Modifier.clickable {
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
//    ConverterScreen(ConverterViewModel())
}