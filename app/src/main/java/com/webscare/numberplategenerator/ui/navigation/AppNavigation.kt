package com.webscare.numberplategenerator.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.webscare.numberplategenerator.ui.editor.EditorScreen
import com.webscare.numberplategenerator.ui.explore.ExploreScreen
import com.webscare.numberplategenerator.ui.history.HistoryScreen
import com.webscare.numberplategenerator.ui.home.HomeScreen
import com.webscare.numberplategenerator.ui.settings.SettingsScreen

fun NavGraphBuilder.appNavigation(
    navController: NavController
) {
    composable(Screen.Home.route) {
        HomeScreen(
            onNotificationClick = { /* Handle logic */ }
        )
    }

    composable(Screen.Explore.route) {
        ExploreScreen(
            onBackClick = {
                navController.popBackStack()
            }
        )
    }
    composable(Screen.History.route) {
        HistoryScreen(
            onBackClick = {
                navController.popBackStack()
            }
        )
    }
    composable(Screen.Settings.route) { SettingsScreen() }
    composable(Screen.Editor.route) { EditorScreen() }

}