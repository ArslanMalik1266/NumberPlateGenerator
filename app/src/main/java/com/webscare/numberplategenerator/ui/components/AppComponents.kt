package com.webscare.numberplategenerator.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.webscare.numberplategenerator.ui.navigation.Screen

@Composable
fun FloatingBottomBar(onNavigate: (String) -> Unit) {
    Surface(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(40),
        color = Color.White,
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { onNavigate(Screen.Home.route) }) {
                Icon(Icons.Default.Home, contentDescription = "Home")
            }
            IconButton(onClick = { onNavigate(Screen.Explore.route) }) {
                Icon(Icons.Default.List, contentDescription = "Explore")
            }
            Spacer(modifier = Modifier.width(48.dp))
            IconButton(onClick = { onNavigate(Screen.History.route) }) {
                Icon(Icons.Default.DateRange, contentDescription = "History")
            }
            IconButton(onClick = { onNavigate(Screen.Settings.route) }) {
                Icon(Icons.Default.Settings, contentDescription = "Settings")
            }
        }
    }
}
@Composable
fun AppFloatingActionButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    FloatingActionButton(
        onClick = onClick,
        shape = CircleShape,
        containerColor = Color.Red,
        modifier = modifier.border(
            width = 2.dp,
            color = Color.White,
            shape = CircleShape
        )
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Add",
            tint = Color.White
        )
    }
}