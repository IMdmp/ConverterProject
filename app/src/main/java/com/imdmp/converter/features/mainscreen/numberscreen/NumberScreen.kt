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

@Composable
fun NumberScreen(modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxSize()) {
        NumberRow(listOf("1", "2", "3"))
        NumberRow(listOf("4", "5", "6"))
        NumberRow(listOf("7", "8", "9"))
        NumberRow(listOf(".", "0", "X"))
    }
}

@Composable
fun NumberRow(numberItems: List<String>) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        NumberItem(
            modifier = Modifier

                .weight(1f), number = numberItems[0]
        )
        NumberItem(
            modifier = Modifier
                .weight(1f), number = numberItems[1]
        )
        NumberItem(
            modifier = Modifier
                .weight(1f), number = numberItems[2]
        )
    }
}

@Composable
fun NumberItem(modifier: Modifier = Modifier, number: String) {
    Text(
        modifier = modifier
            .clickable {
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
    NumberScreen()
}