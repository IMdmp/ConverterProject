package com.imdmp.converter.features.mainscreen.currencydisplay

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.SwapVerticalCircle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalTextInputService
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.imdmp.converter.features.mainscreen.SelectedInputBox
import com.imdmp.converter.features.mainscreen.TransactionType

interface CurrencyDisplayCallbacks {
    fun onSellDataUpdated(data: Double)
    fun onBuyDataUpdated(data: Double)
    fun switchCurrencyLabels()
    fun inputBoxSelected(selectedInputBox: SelectedInputBox)
}

data class CurrencyDisplayComposeModel(
    val sellCurrencyLabel: String,
    val sellCurrencyData: String,
    val receiveCurrencyLabel: String,
    val receiveCurrencyData: String,
)


@Composable
fun CurrencyDisplayScreen(
    modifier: Modifier = Modifier,
    model: CurrencyDisplayComposeModel,
    currencyDisplayCallbacks: CurrencyDisplayCallbacks,
    currencySelected: (t: TransactionType) -> Unit
) {
    Column(modifier = modifier) {
        ConvertRow(
            modifier = Modifier
                .padding(top = 8.dp, bottom = 4.dp),
            currency = model.sellCurrencyLabel,
            data = model.sellCurrencyData,
            type = "Sell",
            inputBoxSelected = { currencyDisplayCallbacks.inputBoxSelected(SelectedInputBox.SELL) },
            onValueUpdate = { sellData ->

            }
        ) {
            currencySelected(TransactionType.SELL)
        }
        IconButton(
            modifier = Modifier
                .size(24.dp),
            onClick = {
                currencyDisplayCallbacks.switchCurrencyLabels()
            }) {
            Icon(Icons.Rounded.SwapVerticalCircle, "swap currency")
        }
        ConvertRow(
            modifier = Modifier
                .padding(top = 4.dp, bottom = 8.dp),
            currency = model.receiveCurrencyLabel,
            data = model.receiveCurrencyData,
            type = "Receive",
            inputBoxSelected = {
                currencyDisplayCallbacks.inputBoxSelected(selectedInputBox = SelectedInputBox.RECEIVE)
            },
            onValueUpdate = { receiveData ->
            }
        ) {
            currencySelected(TransactionType.RECEIVE)
        }
    }


}

@Composable
fun ConvertRow(
    modifier: Modifier,
    type: String,
    data: String,
    currency: String,
    inputBoxSelected: () -> Unit,
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
            inputBoxSelected = inputBoxSelected,
            onValueUpdate = onValueUpdate,
            onCurrencyClicked = onCurrencyClicked
        )
    }
}

@Composable
fun ConvertRowDataExchange(
    modifier: Modifier = Modifier,
    data: String,
    currency: String,
    inputBoxSelected: () -> Unit,
    onValueUpdate: (s: String) -> Unit = {},
    onCurrencyClicked: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed: Boolean by interactionSource.collectIsPressedAsState()

    if (isPressed) {
        inputBoxSelected()
    }

    val tfv =
        TextFieldValue(text = data, selection = TextRange(data.length))

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        CompositionLocalProvider(
            LocalTextInputService provides null
        ) {
            TextField(
                modifier = Modifier
                    .width(240.dp),
                interactionSource = interactionSource,
                value = tfv,
                textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.End),
                keyboardOptions =
                KeyboardOptions.Default.copy(keyboardType = KeyboardType.Decimal),
                onValueChange = { s: TextFieldValue ->
                    onValueUpdate(s.text)
                })
        }

        Text(currency, Modifier
            .padding(start = 16.dp)
            .clickable {
                onCurrencyClicked()
            })
    }
}

@Composable
private fun ConvertRowLabel(modifier: Modifier = Modifier, type: String) {
    Text(modifier = modifier, text = type)
}
