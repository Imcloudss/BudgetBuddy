package com.example.budgetbuddy.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.*
import com.example.budgetbuddy.ui.screens.home.HomeScreen


@Composable
fun MainAppContent(rootNavController: NavController) {
    val navController = rememberNavController()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            BudgetBuddyBottomBar(navController = navController)
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = HOME_ROUTE,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(HOME_ROUTE) {
                HomeScreen(
                    onNavigateToAddTransaction = {
                        navController.navigate(ADD_ROUTE)
                    },
                    onNavigateToTransactions = {
                        navController.navigate(STATS_ROUTE)
                    },
                    onNavigateToGoal = {
                        navController.navigate(GOALS_ROUTE)
                    },
                    onNavigateToProfile = {
                        navController.navigate(PROFILE_ROUTE)
                    }
                )
            }
            composable(ADD_ROUTE) { AddScreen() }
            composable(STATS_ROUTE) { StatsScreen() }
            composable(GOALS_ROUTE) { GoalsScreen() }
            composable(PROFILE_ROUTE) { ProfileScreen() }
        }
    }
}

// Schermate fittizie (andranno eliminate, per ora servono solo per verificare la navigazione correttamente)
// @Composable fun HomeScreen() = CenteredText("Home")
@Composable fun AddScreen() = CenteredText("Add Transaction")
@Composable fun StatsScreen() = CenteredText("Statistics")
@Composable fun GoalsScreen() = CenteredText("Goals")
@Composable fun ProfileScreen() = CenteredText("Profile")

@Composable
private fun CenteredText(text: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = text, style = MaterialTheme.typography.headlineMedium)
    }
}

@Composable
fun BudgetBuddyBottomBar(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationBar {
        bottomNavItems.forEach { item ->
            val isSelected = currentDestination?.route == item.route

            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                        contentDescription = item.title
                    )
                },
                label = { Text(text = item.title) },
                selected = isSelected,
                onClick = {
                    if (!isSelected) {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
        }
    }
}
