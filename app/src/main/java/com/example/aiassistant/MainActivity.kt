package com.example.aiassistant

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.aiassistant.presentation.navigation.AppNavigation
import com.example.aiassistant.ui.theme.AiassistantTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * MainActivity - Entry point of the TaskIt app
 * Sets up the main theme and navigation system
 * Annotated with @AndroidEntryPoint to enable Hilt dependency injection
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AiassistantTheme {
                // Main app surface with theme colors
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Initialize app navigation system
                    AppNavigation()
                }
            }
        }
    }
}
