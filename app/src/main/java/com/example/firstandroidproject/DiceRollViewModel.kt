package com.example.firstandroidproject

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.random.Random

class DiceRollViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(DiceUiState())
    val uiState: StateFlow<DiceUiState> = _uiState.asStateFlow()

    // Initialize the Gemini model
    private val generativeModel = GenerativeModel(
        modelName = "gemini-1.5-flash",
        apiKey = "YOUR_API_KEY" // REPLACE THIS WITH YOUR REAL KEY
    )

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
            
            try {
                val prompt = "Can you tell us a short, fun fact about the number $number?"
                val response = generativeModel.generateContent(prompt)
                val aiResponse = response.text ?: "I couldn't think of a fact for $number!"
                
                _uiState.update { it.copy(funFact = aiResponse, isFetchingFact = false) }
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(
                        funFact = "Error connecting to AI: ${e.localizedMessage}", 
                        isFetchingFact = false 
                    )
                }
            }
        }
    }
}
