package com.spacey.myhome.navigation

import androidx.navigation.NavController
import androidx.navigation.navOptions

enum class NavRoute(val route: String) {
    HOME("home"),
    FORM("form"),
    REPORT("report"),
    SETTINGS("settings"),
    NOTIFICATION("notification")
}

fun NavController.navigateTo(navRoute: NavRoute) {
    val navigateFromHome = navOptions {
        popUpTo(NavRoute.HOME.route) { inclusive = false }
    }
    val navigateToHome = navOptions {
        popUpTo(NavRoute.HOME.route) { inclusive = true }
    }
    when (navRoute) {
        NavRoute.HOME -> this.navigate(navRoute.route, navigateToHome)
        // Bottom nav onBackPressed should return to home
        NavRoute.REPORT, NavRoute.SETTINGS -> this.navigate(navRoute.route, navigateFromHome)
        else -> this.navigate(navRoute.route)
    }
}