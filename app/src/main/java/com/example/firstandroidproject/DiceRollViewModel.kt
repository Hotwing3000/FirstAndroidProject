package com.example.firstandroidproject

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.random.Random

class DiceRollViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(DiceUiState())
    val uiState: StateFlow<DiceUiState> = _uiState.asStateFlow()

    fun rollDice() {
        _uiState.update { currentState ->
            currentState.copy(
                firstDieValue = Random.nextInt(1, 7),
                secondDieValue = Random.nextInt(1, 7),
                numberOfRolls = currentState.numberOfRolls + 1,
                funFact = null
            )
        }
        fetchFunFact(_uiState.value.numberOfRolls)
    }

    private fun fetchFunFact(number: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isFetchingFact = true) }
            delay(1500)
            val simulatedAiResponse = "The number $number is fascinating!"
            _uiState.update { it.copy(funFact = simulatedAiResponse, isFetchingFact = false) }
        }
    }
}