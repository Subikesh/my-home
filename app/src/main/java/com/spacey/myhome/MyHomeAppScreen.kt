package com.spacey.myhome

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.spacey.myhome.navigation.MyHomeNavHost
import com.spacey.myhome.navigation.NavItem
import com.spacey.myhome.navigation.NavRoute

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyHomeAppScreen(navController: NavHostController) {
    val navList = listOf(
        NavItem.Home,
        NavItem.Report,
        NavItem.Settings
    )
    val topBarActions = listOf(
        NavItem.Notification
    )
    var selectedItem by remember { mutableIntStateOf(0) }
    val haptics = LocalHapticFeedback.current
    Scaffold(
        topBar = {
            TopAppBar(title = {
                val titleItem = NavItem.Home
                IconButton(onClick = {
                    navController.popBackStack(titleItem.route, inclusive = false)
                    haptics.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                }) {
                    titleItem.Icon()
                }
            }, actions = {
                topBarActions.forEach { navItem ->
                    IconButton(onClick = { navController.navigate(navItem.route) }) { navItem.Icon() }
                }
            })
        },
        bottomBar = {
            NavigationBar {
                navList.forEachIndexed { i, navItem ->
                    NavigationBarItem(
                        selected = navController.currentDestination?.route == navItem.route,
                        onClick = {
                            selectedItem = i
                            navController.navigate(navItem.route)
                            haptics.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        },
                        icon = {
                            when (navItem) {
                                NavItem.Notification -> {
                                    BadgedBox(badge = {
                                        val count = getNotificationCount()
                                        if (count > 0) {
                                            Badge { Text(getNotificationCount().toString()) }
                                        }
                                    }) {
                                        navItem.Icon()
                                    }
                                }
                                else -> {
                                    navItem.Icon()
                                }
                            }
                        },
                        label = { Text(navItem.label) },
                        alwaysShowLabel = false
                    )
                }
            }
        }, floatingActionButton = {
            LargeFloatingActionButton(onClick = {
                navController.navigate(NavRoute.FORM)
                haptics.performHapticFeedback(HapticFeedbackType.LongPress)
            }, shape = RoundedCornerShape(20)) {
                Icon(Icons.Default.Add, contentDescription = "Form")
            }
        }, contentWindowInsets = WindowInsets(16.dp, 8.dp, 16.dp, 8.dp)
    ) {
        Surface(Modifier.padding(it)) {
            MyHomeNavHost(navController)
        }
    }
}

@Preview
@Composable
fun Preview() {
    val nav = rememberNavController()
    MyHomeAppScreen(navController = nav)
}

private fun getNotificationCount() = 0