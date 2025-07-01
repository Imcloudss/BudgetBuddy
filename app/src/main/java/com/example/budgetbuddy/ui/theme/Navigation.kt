package com.example.budgetbuddy.ui.theme

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import kotlinx.serialization.Serializable
import com.example.budgetbuddy.ui.screens.splash.SplashScreen
import com.example.budgetbuddy.ui.screens.auth.LoginScreen
import com.example.budgetbuddy.ui.screens.home.HomeScreen
import com.example.budgetbuddy.ui.screens.add.AddTransactionScreen
import com.example.budgetbuddy.ui.screens.stats.StatsScreen
import com.example.budgetbuddy.ui.screens.goals.GoalsScreen
import com.example.budgetbuddy.ui.screens.profile.ProfileScreen

// ========================================================================================
// NAVIGATION ROUTES - Pattern della prof con sealed interface
// ========================================================================================

sealed interface BudgetBuddyRoute {
    @Serializable data object Splash : BudgetBuddyRoute
    @Serializable data object Login : BudgetBuddyRoute
    @Serializable data object Home : BudgetBuddyRoute
    @Serializable data object Add : BudgetBuddyRoute
    @Serializable data object Stats : BudgetBuddyRoute
    @Serializable data object Goals : BudgetBuddyRoute
    @Serializable data object Profile : BudgetBuddyRoute
}

// ========================================================================================
// MAIN NAVIGATION GRAPH
// ========================================================================================

@Composable
fun BudgetBuddyNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = BudgetBuddyRoute.Splash
    ) {
        // Splash Screen
        composable<BudgetBuddyRoute.Splash> {
            SplashScreen(
                onSplashFinished = {
                    navController.navigate(BudgetBuddyRoute.Login) {
                        popUpTo(BudgetBuddyRoute.Splash) { inclusive = true }
                    }
                }
            )
        }

        // Login Screen
        composable<BudgetBuddyRoute.Login> {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(BudgetBuddyRoute.Home) {
                        popUpTo(BudgetBuddyRoute.Login) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    // Implementazione futura per registrazione separata
                }
            )
        }

        // Main App Screens
        composable<BudgetBuddyRoute.Home> {
            HomeScreen(navController)
        }
        composable<BudgetBuddyRoute.Add> {
            AddTransactionScreen(navController)
        }
        composable<BudgetBuddyRoute.Stats> {
            StatsScreen(navController)
        }
        composable<BudgetBuddyRoute.Goals> {
            GoalsScreen(navController)
        }
        composable<BudgetBuddyRoute.Profile> {
            ProfileScreen(navController)
        }
    }
}

// ========================================================================================
// MAIN APP NAVIGATION WRAPPER
// ========================================================================================

@Composable
fun BudgetBuddyNavigation(
    navController: NavHostController = rememberNavController()
) {
    BudgetBuddyNavGraph(navController = navController)
}

// ========================================================================================
// BOTTOM NAVIGATION HELPER FUNCTIONS
// ========================================================================================

// Bottom Navigation Item data class
data class BottomNavItem(
    val route: BudgetBuddyRoute,
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

// Bottom Navigation Items
val bottomNavItems = listOf(
    BottomNavItem(
        route = BudgetBuddyRoute.Home,
        title = "Home",
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home
    ),
    BottomNavItem(
        route = BudgetBuddyRoute.Add,
        title = "Add",
        selectedIcon = Icons.Filled.Add,
        unselectedIcon = Icons.Outlined.Add
    ),
    BottomNavItem(
        route = BudgetBuddyRoute.Stats,
        title = "Stats",
        selectedIcon = Icons.Filled.Star, // Icona temporanea
        unselectedIcon = Icons.Outlined.Star
    ),
    BottomNavItem(
        route = BudgetBuddyRoute.Goals,
        title = "Goals",
        selectedIcon = Icons.Filled.Star, // Icona temporanea
        unselectedIcon = Icons.Outlined.Star
    ),
    BottomNavItem(
        route = BudgetBuddyRoute.Profile,
        title = "Profile",
        selectedIcon = Icons.Filled.Person,
        unselectedIcon = Icons.Outlined.Person
    )
)

@Composable
fun BudgetBuddyBottomBar(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationBar {
        bottomNavItems.forEach { item ->
            val isSelected = currentDestination?.hierarchy?.any {
                it.route == item.route::class.qualifiedName
            } == true

            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                        contentDescription = item.title,
                        tint = if (isSelected) androidx.compose.ui.graphics.Color(0xFF3A6DF0) else LocalContentColor.current
                    )
                },
                label = {
                    Text(
                        text = item.title,
                        color = if (isSelected) androidx.compose.ui.graphics.Color(0xFF3A6DF0) else LocalContentColor.current
                    )
                },
                selected = isSelected,
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

// ========================================================================================
// UTILITY FUNCTIONS
// ========================================================================================

/**
 * Composable centralizzato per mostrare il nome della schermata
 * Usato dalle schermate temporanee
 */
@Composable
fun CenteredScreenTitle(
    title: String,
    modifier: Modifier) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium
        )
    }
}