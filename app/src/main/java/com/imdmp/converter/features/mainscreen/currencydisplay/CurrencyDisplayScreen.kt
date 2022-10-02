package com.imdmp.converter.features.mainscreen.currencydisplay

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.rounded.UnfoldMore
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalTextInputService
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.imdmp.converter.features.mainscreen.SelectedInputBox
import com.imdmp.converter.features.mainscreen.TransactionType
import com.imdmp.converter.features.ui.theme.PurpleCustom
import com.imdmp.converter.features.ui.theme.Typography

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
        Box(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp)
                    .background(Color.LightGray)
                    .height(1.dp)
                    .align(Center)
            )
            IconButton(
                modifier = Modifier
                    .padding(top = 16.dp, bottom = 16.dp)
                    .size(24.dp)
                    .align(Center),
                onClick = {
                    currencyDisplayCallbacks.switchCurrencyLabels()
                }) {
                Icon(
                    Icons.Rounded.UnfoldMore, "swap currency",
                    tint = Color.White
                )
            }

        }



        ConvertRow(
            modifier = Modifier
                .padding(top = 4.dp, bottom = 8.dp),
            currency = model.receiveCurrencyLabel,
            data = model.receiveCurrencyData,
            type = "Buy",
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
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ConvertRowLabel(
            Modifier.weight(0.3f),
            type = type,
            currency = currency,
            onCurrencyClicked = onCurrencyClicked
        )
        ConvertRowDataExchange(
            Modifier.weight(0.7f),
            data = data,
            currency = currency,
            inputBoxSelected = inputBoxSelected,
            onValueUpdate = onValueUpdate,
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
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed: Boolean by interactionSource.collectIsPressedAsState()

    if (isPressed) {
        inputBoxSelected()
    }

    val tfv =
        TextFieldValue(text = data, selection = TextRange(data.length))

    Row(
        modifier = modifier.padding(start = 16.dp, end = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CompositionLocalProvider(
            LocalTextInputService provides null
        ) {
            TextField(
                modifier = Modifier.fillMaxWidth(),
                interactionSource = interactionSource,
                value = tfv,
                textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.End),
                keyboardOptions =
                KeyboardOptions.Default.copy(keyboardType = KeyboardType.Decimal),
                onValueChange = { s: TextFieldValue ->
                    onValueUpdate(s.text)
                })
        }


    }
}

@Composable
private fun ConvertRowLabel(
    modifier: Modifier = Modifier,
    type: String,
    currency: String,
    onCurrencyClicked: () -> Unit
) {
    ConstraintLayout(modifier = modifier
        .clickable {
            onCurrencyClicked()
        }
        .padding(start = 16.dp)) {
        val (currIcon, label, currLabel, actionButton) = createRefs()

        Box(modifier = Modifier
            .clip(CircleShape)
            .background(Color.White)
            .size(16.dp)
            .constrainAs(currIcon) {
                start.linkTo(parent.start)
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
            })

        Text(modifier = Modifier.constrainAs(label) {
            top.linkTo(parent.top)
            bottom.linkTo(currLabel.top)
            start.linkTo(currIcon.end, 16.dp)
        }, text = type, style = Typography.subtitle1)

        Text(
            text = currency, Modifier
                .constrainAs(currLabel) {
                    top.linkTo(label.bottom)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(label.start)
                }, style = Typography.h5
        )
        Icon(
            modifier = Modifier
                .size(16.dp)
                .constrainAs(actionButton) {
                    top.linkTo(currLabel.top)
                    start.linkTo(currLabel.end, 16.dp)
                    bottom.linkTo(currLabel.bottom)
                },
            imageVector = Icons.Default.KeyboardArrowDown, contentDescription = "",
            tint = Color.White
        )


    }
}


@Preview
@Composable
fun PreviewCurrencyDisplayScreen() {
    Surface(color = PurpleCustom) {

        CurrencyDisplayScreen(
            model = CurrencyDisplayComposeModel(
                sellCurrencyLabel = "USD",
                sellCurrencyData = "",
                receiveCurrencyLabel = "EUR",
                receiveCurrencyData = ""
            ),
            currencyDisplayCallbacks = object:
                CurrencyDisplayCallbacks {
                override fun onSellDataUpdated(data: Double) {
                    TODO("Not yet implemented")
                }

                override fun onBuyDataUpdated(data: Double) {
                    TODO("Not yet implemented")
                }

                override fun switchCurrencyLabels() {
                    TODO("Not yet implemented")
                }

                override fun inputBoxSelected(selectedInputBox: SelectedInputBox) {
                    TODO("Not yet implemented")
                }
            },
            currencySelected = {}
        )
    }
}