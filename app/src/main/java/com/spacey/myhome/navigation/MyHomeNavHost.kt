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
import com.spacey.data.base.InputType
import com.spacey.data.service.Service
import com.spacey.myhome.R
import com.spacey.myhome.ScaffoldViewState
import com.spacey.myhome.expenseform.MyHomeFormScreen
import com.spacey.myhome.home.HomeScreen
import com.spacey.myhome.serviceform.ServiceFormScreen
import java.lang.IllegalArgumentException
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun MyHomeNavHost(navController: NavHostController, setScaffoldState: (ScaffoldViewState) -> Unit) {
    NavHost(navController, startDestination = NavRoute.Home.route) {
        composable(NavRoute.Home.route) {
            HomeScreen(navController) { scaffold ->
                setScaffoldState(scaffold)
            }
        }
        // TODO Make the route static
        composable(NavRoute.ExpenseForm(LocalDate.now(), "").route) {
            val date = it.arguments?.getString("date")
                ?.let { date -> LocalDate.parse(date, DateTimeFormatter.ISO_DATE) }
                ?: LocalDate.now()
            val service = it.arguments?.getString("service") ?: throw IllegalArgumentException()
            MyHomeFormScreen(date, navController, service) { scaffold ->
                setScaffoldState(scaffold)
            }
        }
        composable(NavRoute.ServiceForm(null).route) {
            val serviceName = it.arguments?.getString("service_name")?.takeIf { it != "null" }
            val inputType = it.arguments?.getString("input_type")?.takeIf { it != "null" }?.let { type -> InputType.valueOf(type) }
            val service = if (serviceName != null && inputType != null) {
                Service(serviceName, inputType)
            } else null
            ServiceFormScreen(service = service, navController = navController) { scaffold ->
                setScaffoldState(scaffold)
            }
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

