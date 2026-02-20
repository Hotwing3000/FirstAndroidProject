package com.example.firstandroidproject

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.firstandroidproject.ui.theme.FirstAndroidProjectTheme

@Composable
fun DiceScreen(
    state: DiceUiState,
    onRollClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Die 1: ${state.firstDieValue ?: "?"}", modifier = Modifier.padding(8.dp))
        Text(text = "Die 2: ${state.secondDieValue ?: "?"}", modifier = Modifier.padding(8.dp))
        Text(text = "Total Rolls: ${state.numberOfRolls}", modifier = Modifier.padding(16.dp))

        Button(onClick = onRollClick, enabled = !state.isFetchingFact) {
            Text(text = "Roll Dice")
        }

        Text(
            text = "Summary: Die 1 is ${state.firstDieValue ?: "?"}, " +
                    "Die 2 is ${state.secondDieValue ?: "?"}, " +
                    "The sum is ${(state.firstDieValue ?: 0) + (state.secondDieValue ?: 0)}",
            modifier = Modifier.padding(16.dp)
        )

        // The AI Response Section
        if (state.isFetchingFact) {
            CircularProgressIndicator(modifier = Modifier.padding(16.dp))
            Text(text = "Asking AI for a fun fact...")
        } else if (state.funFact != null) {
            Text(
                text = "Something about the number ${state.numberOfRolls} : ${state.funFact}",
                modifier = Modifier.padding(16.dp),
                color = Color.Gray
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DicePreview() {
    FirstAndroidProjectTheme {
        DiceScreen(
            state = DiceUiState(
                firstDieValue = 3,
                secondDieValue = 4,
                numberOfRolls = 5,
                funFact = "The number 5 is fascinating!"
            ),
            onRollClick = {}
        )
    }
}
