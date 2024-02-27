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
import com.spacey.myhome.R
import com.spacey.myhome.form.MyHomeFormScreen
import com.spacey.myhome.home.HomeScreen
import java.lang.IllegalArgumentException
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun MyHomeNavHost(navController: NavHostController) {
    NavHost(navController, startDestination = NavRoute.Home.route) {
        composable(NavRoute.Home.route) {
            HomeScreen(navController)
        }
        // TODO Make the route static
        composable(NavRoute.Form(LocalDate.now(), "").route) {
            val date = it.arguments?.getString("date")
                ?.let { date -> LocalDate.parse(date, DateTimeFormatter.ISO_DATE) }
                ?: LocalDate.now()
            val service = it.arguments?.getString("service") ?: throw IllegalArgumentException()
            MyHomeFormScreen(date, navController, service)
        }
        composable(NavRoute.Report.route) {
            Text("Hello Android! We are in Reports screen")
        }
        composable(NavRoute.Notification.route) {
            Text("Hello Android! We are in Notification screen")
        }
        composable(NavRoute.Settings.route) {
            Text("Hello Android! We are in Settings screen")
        }
    }
}


enum class NavItem(val label: String, private val icon: ImageVector, val route: NavRoute) {
    Home("Home", Icons.Default.Home, NavRoute.Home),
    Report("Report", Icons.Default.InsertChart, NavRoute.Report),
    Notification("Notification", Icons.Default.Notifications, NavRoute.Notification),
    Settings("Settings", Icons.Default.Settings, NavRoute.Settings);

    @Composable
    fun Icon() = when (this) {
        Home -> Icon(
            painter = painterResource(id = R.drawable.ic_home_icon),
            contentDescription = "Home"
        )

        else -> Icon(icon, contentDescription = label)
    }
}

