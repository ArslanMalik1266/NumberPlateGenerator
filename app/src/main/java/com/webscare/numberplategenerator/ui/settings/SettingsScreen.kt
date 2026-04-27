package com.webscare.numberplategenerator.ui.settings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun SettingsScreen() {
    // Box use karke hum screen ko occupy karte hain
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center // Ye text ko screen ke center mein rakhega
    ) {
        Text(text = "SettingsScreen")
    }
}