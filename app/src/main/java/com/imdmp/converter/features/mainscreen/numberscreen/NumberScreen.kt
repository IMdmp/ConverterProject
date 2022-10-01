package com.imdmp.converter.features.mainscreen.numberscreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

interface NumberScreenCallbacks {
    fun characterEmitted(c: Char)
}

@Composable
fun NumberScreen(modifier: Modifier = Modifier, numberScreenCallbacks: NumberScreenCallbacks) {
    Column(modifier = modifier.fillMaxSize()) {
        NumberRow(listOf("1", "2", "3")) { s -> numberScreenCallbacks.characterEmitted(s.first()) }
        NumberRow(listOf("4", "5", "6")) { s -> numberScreenCallbacks.characterEmitted(s.first()) }
        NumberRow(listOf("7", "8", "9")) { s -> numberScreenCallbacks.characterEmitted(s.first()) }
        NumberRow(listOf(".", "0", "X")) { s -> numberScreenCallbacks.characterEmitted(s.first()) }
    }
}

@Composable
fun NumberRow(numberItems: List<String>, itemEmit: (s: String) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        numberItems.forEach {
            NumberItem(
                modifier = Modifier
                    .weight(1f),
                number = it,
                itemEmit = itemEmit,
            )
        }
    }
}

@Composable
fun NumberItem(modifier: Modifier = Modifier, number: String, itemEmit: (s: String) -> Unit) {
    Text(
        modifier = modifier
            .clickable {
                itemEmit(number)
            }
            .padding(top = 8.dp, bottom = 8.dp),
        text = number,
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.h4
    )
}

@Preview
@Composable
fun PreviewNumberScreen() {
    NumberScreen(numberScreenCallbacks = object: NumberScreenCallbacks {
        override fun characterEmitted(c: Char) {
            TODO("Not yet implemented")
        }

    })
}