package com.example.alphabetlauncher.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AlphabetBar(){

    val alphabet = ('A'..'Z').toList()

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .padding(
                vertical = 14.dp,
                horizontal = 8.dp
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = "★")

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            alphabet.forEach { letter ->

                Text(text = letter.toString())
            }
        }
        Text(text = "°")
    }
}