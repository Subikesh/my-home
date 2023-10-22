package com.spacey.myhome.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.InsertChart
import androidx.compose.material.icons.filled.Notifications
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
    }
}


enum class NavItem(val label: String, val icon: ImageVector, val route: String) {
    Home("Home", Icons.Default.Home, NavRoute.HOME),
    Report("Report", Icons.Default.InsertChart, NavRoute.REPORT),
    Notification("Notification", Icons.Default.Notifications, NavRoute.NOTIFICATION)
}

