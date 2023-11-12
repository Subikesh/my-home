package com.spacey.myhome.navigation

import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.navOptions

enum class NavRoute(val route: String) {
    HOME("home"),
    FORM("form"),
    REPORT("report"),
    SETTINGS("settings"),
    NOTIFICATION("notification")
}

fun NavController.navigateTo(navRoute: NavRoute) {
    val navigateToHome = navOptions {
        popUpTo(NavRoute.HOME.route) { inclusive = true }
    }
    when (navRoute) {
        NavRoute.HOME -> this.navigate(navRoute.route, navigateToHome)
        // Bottom nav onBackPressed should return to home
        NavRoute.REPORT, NavRoute.SETTINGS -> this.navigate(navRoute.route, navigateRouteOptions {
            popUpTo(navRoute.route) { inclusive = true }
        })

        else -> this.navigate(navRoute.route, navOptions { popUpTo(navRoute.route) { inclusive = false } })
    }
}

fun navigateRouteOptions(homeInclusive: Boolean = false, block: NavOptionsBuilder.() -> Unit) =
    navOptions {
        popUpTo(NavRoute.HOME.route) { inclusive = homeInclusive }
        block()
    }