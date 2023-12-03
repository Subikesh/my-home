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
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.spacey.myhome.MyHomeViewModel
import com.spacey.myhome.R
import com.spacey.myhome.form.MyHomeFormScreen
import com.spacey.myhome.home.HomeScreen
import java.time.LocalDate

@Composable
fun MyHomeNavHost(navController: NavHostController, viewModel: MyHomeViewModel) {
    NavHost(navController, startDestination = NavRoute.HOME.route) {
        composable(NavRoute.HOME.route) {
            HomeScreen()
        }
        composable(NavRoute.REPORT.route) {
            Text("Hello Android! We are in Reports screen")
        }
        composable(NavRoute.FORM.route) {
            MyHomeFormScreen(currentDate = LocalDate.now()) { expense ->
                viewModel.addExpense(expense)
            }
        }
        composable(NavRoute.NOTIFICATION.route) {
            Text("Hello Android! We are in Notification screen")
        }
        composable(NavRoute.SETTINGS.route) {
            Text("Hello Android! We are in Settings screen")
        }
    }
}


enum class NavItem(val label: String, private val icon: ImageVector, val route: NavRoute) {
    Home("Home", Icons.Default.Home, NavRoute.HOME),
    Report("Report", Icons.Default.InsertChart, NavRoute.REPORT),
    Notification("Notification", Icons.Default.Notifications, NavRoute.NOTIFICATION),
    Settings("Settings", Icons.Default.Settings, NavRoute.SETTINGS);

    @Composable
    fun Icon() = when (this) {
        Home -> Icon(painter = painterResource(id = R.drawable.ic_home_icon), contentDescription = "Home")
        else -> Icon(icon, contentDescription = label)
    }
}

