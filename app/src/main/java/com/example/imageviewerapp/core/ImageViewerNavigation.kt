package com.example.imageviewerapp.core

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.imageviewerapp.ui.journeys.details.DetailScreen
import com.example.imageviewerapp.ui.journeys.Favorites.FavoritesScreen
import com.example.imageviewerapp.ui.journeys.home.HomeScreen
import com.example.imageviewerapp.utility.Constants.DETAILS_ROUTE
import com.example.imageviewerapp.utility.Constants.FAVORITES_ROUTE
import com.example.imageviewerapp.utility.Constants.HOME_ROUTE

@Composable
fun ImageViewerNavigation() {
    val navController = rememberNavController()

    val bottomNavItems = listOf(
        BottomNavItem(
            route = HOME_ROUTE,
            icon = Icons.Default.Home,
            label = "Home"
        ),
        BottomNavItem(
            route = FAVORITES_ROUTE,
            icon = Icons.Default.Favorite,
            label = "Favorites"
        )
    )

    val bottomNavRoutes = bottomNavItems.map { it.route }

    Scaffold(
        bottomBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination

            // Only show bottom nav when we're on a top-level destination
            val showBottomNav = currentDestination?.route in bottomNavRoutes

            if (showBottomNav) {
                NavigationBar {
                    bottomNavItems.forEach { item ->
                        NavigationBarItem(
                            icon = { Icon(item.icon, contentDescription = item.label) },
                            label = { Text(item.label) },
                            selected = currentDestination?.hierarchy?.any { it.route == item.route } == true,
                            onClick = {
                                navController.navigate(item.route) {
                                    // Pop up to the start destination of the graph to
                                    // avoid building up a large stack of destinations
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    // Avoid multiple copies of the same destination when
                                    // reselecting the same item
                                    launchSingleTop = true
                                    // Restore state when reselecting a previously selected item
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = HOME_ROUTE,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(HOME_ROUTE) {
                HomeScreen(
                    navigateToDetail = { imageId ->
                        navController.navigate("details/$imageId")
                    }
                )
            }

            composable(FAVORITES_ROUTE) {
                FavoritesScreen(
                    navigateToDetail = { imageId ->
                        navController.navigate("details/$imageId")
                    }
                )
            }

            composable(
                route = DETAILS_ROUTE,
                arguments = listOf(
                    navArgument("imageId") {
                        type = NavType.StringType
                    }
                )
            ) {
                DetailScreen(
                    navigateBack = { navController.popBackStack() }
                )
            }
        }
    }
}

data class BottomNavItem(
    val route: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val label: String
)