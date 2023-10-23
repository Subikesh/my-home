package com.spacey.myhome.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.InsertChart
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.spacey.myhome.home.HomeScreen

@Composable
fun MyHomeNavHost(navController: NavHostController) {
    NavHost(navController, startDestination = NavRoute.HOME) {
        composable(NavRoute.HOME) {
            HomeScreen()
        }
        composable(NavRoute.REPORT) {
            Text("Hello Android! We are in Reports screen")
        }
        composable(NavRoute.FORM) {
            Text("Hello Android! We are in Form screen")
        }
        composable(NavRoute.NOTIFICATION) {
            Text("Hello Android! We are in Notification screen")
        }
        composable(NavRoute.SETTINGS) {
            Text("Hello Android! We are in Settings screen")
        }
    }
}


enum class NavItem(val label: String, private val icon: ImageVector, val route: String) {
    Home("Home", Icons.Default.Home, NavRoute.HOME),
    Report("Report", Icons.Default.InsertChart, NavRoute.REPORT),
    Notification("Notification", Icons.Default.Notifications, NavRoute.NOTIFICATION),
    Settings("Settings", Icons.Default.Settings, NavRoute.SETTINGS);

    @Composable
    fun Icon() = Icon(icon, contentDescription = label)
}

