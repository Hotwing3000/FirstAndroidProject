package com.example.firstandroidproject

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.firstandroidproject.ui.theme.FirstAndroidProjectTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
