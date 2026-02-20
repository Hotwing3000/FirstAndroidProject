package com.example.firstandroidproject

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.firstandroidproject.ui.theme.FirstAndroidProjectTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // 1. Get ViewModel & State using Compose-friendly tools
            val viewModel: DiceRollViewModel = viewModel()
            val state by viewModel.uiState.collectAsState()

            FirstAndroidProjectTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    // 2. Pass State to your UI Composable
                    DiceScreen(
                        firstDie = state.firstDieValue,
                        secondDie = state.secondDieValue,
                        numberOfRolls = state.numberOfRolls,
                        onRollClick = { viewModel.rollDice() },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

// Logic: Holds the data for the screen
data class DiceUiState(
    val firstDieValue: Int? = null,
    val secondDieValue: Int? = null,
    val numberOfRolls: Int = 0,
)

// Logic: Handles how the data changes
class DiceRollViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(DiceUiState())
    val uiState: StateFlow<DiceUiState> = _uiState.asStateFlow()

    fun rollDice() {
        _uiState.update { currentState ->
            currentState.copy(
                firstDieValue = Random.nextInt(from = 1, until = 7),
                secondDieValue = Random.nextInt(from = 1, until = 7),
                numberOfRolls = currentState.numberOfRolls + 1,
            )
        }
    }
}

// UI: The actual visual layout
@Composable
fun DiceScreen(
    firstDie: Int?,
    secondDie: Int?,
    numberOfRolls: Int,
    onRollClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Die 1: ${firstDie ?: "?"}", modifier = Modifier.padding(8.dp))
        Text(text = "Die 2: ${secondDie ?: "?"}", modifier = Modifier.padding(8.dp))
        Text(text = "Total Rolls: $numberOfRolls", modifier = Modifier.padding(16.dp))
        
        Button(onClick = onRollClick) {
            Text(text = "Roll Dice")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DicePreview() {
    FirstAndroidProjectTheme {
        DiceScreen(
            firstDie = 3,
            secondDie = 5,
            numberOfRolls = 10,
            onRollClick = {}
        )
    }
}
