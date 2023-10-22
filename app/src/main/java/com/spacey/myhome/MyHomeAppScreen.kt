package com.spacey.myhome

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.spacey.myhome.navigation.MyHomeNavHost
import com.spacey.myhome.navigation.NavItem
import com.spacey.myhome.navigation.NavRoute

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyHomeAppScreen(navController: NavHostController) {
    val navList = listOf(
        NavItem.Home,
        NavItem.Report,
        NavItem.Notification
    )
    var selectedItem by remember { mutableIntStateOf(0) }
    Scaffold(
        topBar = {
            TopAppBar(title = { Icon(Icons.Filled.Home, contentDescription = "Home") })
        },
        bottomBar = {
            NavigationBar {
                navList.forEachIndexed { i, navItem ->
                    NavigationBarItem(
                        selected = navController.currentDestination?.route == navItem.route,
                        onClick = {
                            selectedItem = i
                            navController.navigate(navItem.route)
                        },
                        icon = {
                            if (navItem == NavItem.Notification) {
                                BadgedBox(badge = {
                                    val count = getNotificationCount()
                                    if (count > 0) {
                                        Badge { Text(getNotificationCount().toString()) }
                                    }
                                }) {
                                    Icon(navItem.icon, navItem.label)
                                }
                            } else {
                                Icon(navItem.icon, contentDescription = navItem.label)
                            }
                        },
                        label = { Text(navItem.label) },
                        alwaysShowLabel = false
                    )
                }
            }
        }, floatingActionButton = {
            FloatingActionButton(onClick = {
                navController.navigate(NavRoute.FORM)
            }, shape = RoundedCornerShape(20.dp)) {
                Icon(Icons.Default.Add, contentDescription = "Form")
            }
        }, contentWindowInsets = WindowInsets(16.dp, 8.dp, 16.dp, 8.dp)
    ) {
        Surface(Modifier.padding(it)) {
            MyHomeNavHost(navController)
        }
    }
}

private fun getNotificationCount() = 0