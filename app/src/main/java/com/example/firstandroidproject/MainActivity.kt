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
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.firstandroidproject.ui.theme.FirstAndroidProjectTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: DiceRollViewModel = viewModel()
            val state by viewModel.uiState.collectAsState()

            FirstAndroidProjectTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    DiceScreen(
                        state = state,
                        onRollClick = { viewModel.rollDice() },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

data class DiceUiState(
    val firstDieValue: Int? = null,
    val secondDieValue: Int? = null,
    val numberOfRolls: Int = 0,
    val funFact: String? = null,
    val isFetchingFact: Boolean = false
)

class DiceRollViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(DiceUiState())
    val uiState: StateFlow<DiceUiState> = _uiState.asStateFlow()

    fun rollDice() {
        _uiState.update { currentState ->
            currentState.copy(
                firstDieValue = Random.nextInt(1, 7),
                secondDieValue = Random.nextInt(1, 7),
                numberOfRolls = currentState.numberOfRolls + 1,
                funFact = null // Reset fact on new roll
            )
        }
        // Trigger the "AI" request
        fetchFunFact(_uiState.value.numberOfRolls)
    }

    private fun fetchFunFact(number: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isFetchingFact = true) }
            
            // SIMULATING AI: In a real app, you'd call Gemini or OpenAI API here
            delay(1500) 
            val simulatedAiResponse = "The number $number is fascinating! It's often associated with balance and harmony in various cultures."
            
            _uiState.update { it.copy(funFact = simulatedAiResponse, isFetchingFact = false) }
        }
    }
}

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
                color = androidx.compose.ui.graphics.Color.Gray
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DicePreview() {
    FirstAndroidProjectTheme {
        DiceScreen(
            state = DiceUiState(firstDieValue = 3, secondDieValue = 4, numberOfRolls = 5, funFact = "Five is a prime number!"),
            onRollClick = {}
        )
    }
}
