package com.webscare.numberplategenerator.ui.navigation

import com.webscare.numberplategenerator.R

object BottomNavConfig {
    val items = listOf(
        BottomNavItem(Screen.Home.route, R.drawable.ic_home, "Home"),
        BottomNavItem(Screen.Explore.route, R.drawable.ic_explore, "Explore"),
        BottomNavItem(Screen.History.route, R.drawable.ic_history, "History"),
        BottomNavItem(Screen.Settings.route, R.drawable.ic_settings, "Settings")
    )
}