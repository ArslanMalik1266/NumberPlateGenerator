package com.webscare.numberplategenerator.ui.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Explore : Screen("explore")
    object History : Screen("history")
    object Settings : Screen("settings")
    object Editor : Screen("editor")

}