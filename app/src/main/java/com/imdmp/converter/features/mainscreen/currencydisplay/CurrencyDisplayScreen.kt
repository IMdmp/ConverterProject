package com.imdmp.converter.features.mainscreen.currencydisplay

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.SwapVerticalCircle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.imdmp.converter.features.mainscreen.ConvertRowDataExchange
import com.imdmp.converter.features.mainscreen.TransactionType

interface CurrencyDisplayCallbacks {
    fun onSellDataUpdated(data: Double)
    fun onBuyDataUpdated(data: Double)
    fun switchCurrencyLabels()
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
            onValueUpdate = { sellData ->
                if (sellData.isNotBlank()) {
                    currencyDisplayCallbacks.onSellDataUpdated(sellData.toDouble())
                }
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
            onValueUpdate = { receiveData ->
                if (receiveData.isNotBlank()) {
                    currencyDisplayCallbacks.onBuyDataUpdated(receiveData.toDouble())
                }
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
