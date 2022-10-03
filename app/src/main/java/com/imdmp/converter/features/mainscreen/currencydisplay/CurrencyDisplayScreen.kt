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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.rounded.UnfoldMore
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalTextInputService
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.imdmp.converter.R
import com.imdmp.converter.features.mainscreen.SelectedInputBox
import com.imdmp.converter.features.mainscreen.TransactionType
import com.imdmp.converter.features.ui.theme.PurpleCustom
import com.imdmp.converter.features.ui.theme.Typography

interface CurrencyDisplayCallbacks {
    fun switchCurrencyLabels()
    fun inputBoxSelected(selectedInputBox: SelectedInputBox)
}

enum class CurrencyType(val resource: Int) {
    SELL(R.string.currency_display_type_sell), BUY(R.string.currency_display_type_buy)
}

data class CurrencyDisplayComposeModel(
    val sellCurrencyLabel: String,
    val sellCurrencyData: String,
    val receiveCurrencyLabel: String,
    val receiveCurrencyData: String,
    val retrievingRate: Boolean,
)

data class ConvertRowComposeModel(
    val label: String,
    val data: String,
    val retrievingRate: Boolean,
    val type: CurrencyType,
)


@Composable
fun CurrencyDisplayScreen(
    modifier: Modifier = Modifier,
    model: CurrencyDisplayComposeModel,
    currencyDisplayCallbacks: CurrencyDisplayCallbacks,
    currencySelected: (t: TransactionType) -> Unit
) {

    val sellModel = ConvertRowComposeModel(
        label = model.sellCurrencyLabel,
        data = model.sellCurrencyData,
        retrievingRate = model.retrievingRate,
        type = CurrencyType.SELL
    )

    val buyModel = ConvertRowComposeModel(
        label = model.receiveCurrencyLabel,
        data = model.receiveCurrencyData,
        retrievingRate = model.retrievingRate,
        type = CurrencyType.BUY
    )

    Column(modifier = modifier) {
        ConvertRow(
            modifier = Modifier
                .padding(top = 8.dp, bottom = 4.dp),
            model = sellModel,
            inputBoxSelected = {
                currencyDisplayCallbacks.inputBoxSelected(SelectedInputBox.SELL)
            },
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
                    Icons.Rounded.UnfoldMore, stringResource(R.string.swap_currency),
                    tint = Color.White
                )
            }

        }

        ConvertRow(
            modifier = Modifier
                .padding(top = 4.dp, bottom = 8.dp),
            model = buyModel,
            inputBoxSelected = {
                currencyDisplayCallbacks.inputBoxSelected(selectedInputBox = SelectedInputBox.RECEIVE)
            },
        ) {
            currencySelected(TransactionType.RECEIVE)
        }
    }


}

@Composable
fun ConvertRow(
    modifier: Modifier,
    model: ConvertRowComposeModel,
    inputBoxSelected: () -> Unit,
    onCurrencyClicked: () -> Unit
) {

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ConvertRowLabel(
            Modifier.weight(0.3f),
            type = model.type.resource,
            currency = model.label,
            onCurrencyClicked = onCurrencyClicked
        )
        ConvertRowDataExchange(
            Modifier.weight(0.7f),
            data = model.data,
            rateLoading = model.retrievingRate,
            inputBoxSelected = inputBoxSelected,
            model = model
        )
    }
}

@Composable
fun ConvertRowDataExchange(
    modifier: Modifier = Modifier,
    data: String,
    inputBoxSelected: () -> Unit,
    rateLoading: Boolean,
    model: ConvertRowComposeModel,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed: Boolean by interactionSource.collectIsPressedAsState()

    if (isPressed) {
        inputBoxSelected()
    }

    val tfv =
        TextFieldValue(text = data, selection = TextRange(data.length))

    val focusRequester = remember {
        FocusRequester()
    }

    if (model.type == CurrencyType.SELL) {
        LaunchedEffect(key1 = Unit) {
            focusRequester.requestFocus()
        }
    }
    Column(
        modifier = modifier
            .padding(start = 16.dp, end = 16.dp)
            .focusRequester(focusRequester),
        verticalArrangement = Arrangement.SpaceAround
    ) {
        CompositionLocalProvider(
            LocalTextInputService provides null
        ) {
            BasicTextField(
                modifier = Modifier.fillMaxWidth(),
                interactionSource = interactionSource,
                value = tfv,
                cursorBrush = SolidColor(Color.White),
                textStyle = Typography.body1.copy(textAlign = TextAlign.End),
                keyboardOptions =
                KeyboardOptions.Default.copy(keyboardType = KeyboardType.Decimal),
                onValueChange = { s: TextFieldValue ->
                })
        }

        if (rateLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(12.dp)
                    .align(Alignment.End),
                color = Color.White
            )
        }

    }
}

@Composable
private fun ConvertRowLabel(
    modifier: Modifier = Modifier,
    type: Int,
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
        }, text = stringResource(id = type), style = Typography.subtitle1)

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
                receiveCurrencyData = "",
                retrievingRate = true,
            ),
            currencyDisplayCallbacks = object:
                CurrencyDisplayCallbacks {

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