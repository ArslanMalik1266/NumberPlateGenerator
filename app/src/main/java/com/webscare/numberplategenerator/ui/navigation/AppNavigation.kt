package com.webscare.numberplategenerator.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.webscare.numberplategenerator.ui.explore.ExploreScreen
import com.webscare.numberplategenerator.ui.history.HistoryScreen
import com.webscare.numberplategenerator.ui.home.HomeScreen
import com.webscare.numberplategenerator.ui.settings.SettingsScreen

fun NavGraphBuilder.appNavigation(
) {
    composable(Screen.Home.route) { HomeScreen() }
    composable(Screen.Explore.route) { ExploreScreen() }
    composable(Screen.History.route) { HistoryScreen() }
    composable(Screen.Settings.route) { SettingsScreen() }
}