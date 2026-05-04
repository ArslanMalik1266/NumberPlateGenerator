package com.webscare.numberplategenerator.ui.mainscreen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.webscare.numberplategenerator.ui.EditorStates
import com.webscare.numberplategenerator.ui.MainViewModel
import com.webscare.numberplategenerator.ui.PlateType
import com.webscare.numberplategenerator.ui.components.ExpandableFabMenu
import com.webscare.numberplategenerator.ui.components.FloatingBottomBar
import com.webscare.numberplategenerator.ui.navigation.BottomNavConfig
import com.webscare.numberplategenerator.ui.navigation.Screen
import com.webscare.numberplategenerator.ui.navigation.appNavigation
import org.koin.compose.viewmodel.koinActivityViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MainScreen(
    viewModel: MainViewModel = koinActivityViewModel()
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val isEditorScreen = currentRoute == Screen.Editor.route
    var isMenuExpanded by remember { mutableStateOf(false) }

    BackHandler(enabled = isMenuExpanded) {
        isMenuExpanded = false
    }

    Box(modifier = Modifier.fillMaxSize()) {
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.fillMaxSize()
        ) {
            appNavigation(navController)
        }

        // --- Yahan se changes hain ---
        if (!isEditorScreen) {
            // 1. Background Overlay (Sirf menu khulne par background click block karega)
            if (isMenuExpanded) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.3f))
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) { isMenuExpanded = false }
                )
            }

            // 2. Bottom UI Container
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 12.dp)
                    .fillMaxWidth(),
                contentAlignment = Alignment.BottomCenter // Items bottom se align honge
            ) {
                // Bottom Bar apni jagah fix rahega
                FloatingBottomBar(
                    currentRoute = currentRoute,
                    items = BottomNavConfig.items,
                    onNavigate = { route ->
                        navController.navigate(route) {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )

                // Expandable Menu ko hum bar ke "Upar" overlay kar rahe hain
                ExpandableFabMenu(
                    isExpanded = isMenuExpanded,
                    onToggle = { isMenuExpanded = !isMenuExpanded },
                    onBikeClick = {
                        isMenuExpanded = false
                        viewModel.updatePlateType(PlateType.BIKE)
                        navController.navigate(Screen.Editor.route)
                    },
                    onCarClick = {
                        isMenuExpanded = false
                        viewModel.updatePlateType(PlateType.CAR)
                        navController.navigate(Screen.Editor.route)
                    },
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .offset(y = (-24).dp)
                )
            }
        }
    }
}
