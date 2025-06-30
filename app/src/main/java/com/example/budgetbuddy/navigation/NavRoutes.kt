package com.example.budgetbuddy.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.ui.graphics.vector.ImageVector

// Route costanti
const val SPLASH_ROUTE = "splash"
const val LOGIN_ROUTE = "login"
const val MAIN_ROUTE = "main"

const val HOME_ROUTE = "home"
const val ADD_ROUTE = "add"
const val STATS_ROUTE = "stats"
const val GOALS_ROUTE = "goals"
const val PROFILE_ROUTE = "profile"

data class BottomNavItem(
    val route: String,
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

val bottomNavItems = listOf(
    BottomNavItem(HOME_ROUTE, "Home", Icons.Filled.Home, Icons.Outlined.Home),
    BottomNavItem(ADD_ROUTE, "Add", Icons.Filled.Add, Icons.Outlined.Add),
    BottomNavItem(STATS_ROUTE, "Stats", Icons.Filled.Star, Icons.Outlined.Star),
    BottomNavItem(GOALS_ROUTE, "Goals", Icons.Filled.Star, Icons.Outlined.Star),
    BottomNavItem(PROFILE_ROUTE, "Profile", Icons.Filled.Person, Icons.Outlined.Person)
)
