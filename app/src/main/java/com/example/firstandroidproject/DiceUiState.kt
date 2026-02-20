package com.example.firstandroidproject

data class DiceUiState(
    val firstDieValue: Int? = null,
    val secondDieValue: Int? = null,
    val numberOfRolls: Int = 0,
    val funFact: String? = null,
    val isFetchingFact: Boolean = false
)