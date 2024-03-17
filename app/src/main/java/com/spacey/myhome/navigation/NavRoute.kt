package com.spacey.myhome.navigation

import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.navOptions
import com.spacey.data.service.Service
import java.time.LocalDate

sealed class NavRoute(val route: String, val specificRoute: String = route) {
    data object Home : NavRoute("home")
    data class ExpenseForm(val selectedDate: LocalDate, val service: String) : NavRoute("form/{date}/{service}", "form/$selectedDate/$service")
    data class ServiceForm(val service: Service?) : NavRoute("service_form/{service_name}/{input_type}", "service_form/${service?.name}/${service?.type?.name}")
    data object Report : NavRoute("report")
    data object Settings : NavRoute("settings")
    data object Notification : NavRoute("notification")
}

fun NavController.navigateTo(navRoute: NavRoute) {
    val navigateToHome = navOptions {
        popUpTo(NavRoute.Home.specificRoute) { inclusive = true }
    }
    when (navRoute) {
        NavRoute.Home -> this.navigate(navRoute.specificRoute, navigateToHome)
        // Bottom nav onBackPressed should return to home
        NavRoute.Report, NavRoute.Settings -> this.navigate(navRoute.specificRoute, navigateRouteOptions {
            popUpTo(navRoute.specificRoute) { inclusive = true }
        })

        else -> this.navigate(navRoute.specificRoute, navOptions { popUpTo(navRoute.specificRoute) { inclusive = false } })
    }
}

fun navigateRouteOptions(homeInclusive: Boolean = false, block: NavOptionsBuilder.() -> Unit) =
    navOptions {
        popUpTo(NavRoute.Home.specificRoute) { inclusive = homeInclusive }
        block()
    }