package com.example.budgetbuddy.ui.theme

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.budgetbuddy.ui.screens.home.HomeScreen
import com.example.budgetbuddy.ui.screens.sign.SignInScreen
import com.example.budgetbuddy.ui.screens.sign.SignUpScreen
import com.example.budgetbuddy.ui.screens.splash.SplashScreen
import kotlinx.serialization.Serializable

sealed interface BudgetBuddyRoute {
    @Serializable data object Splash : BudgetBuddyRoute
    @Serializable data object SignIn : BudgetBuddyRoute
    @Serializable data object SignUp : BudgetBuddyRoute
    @Serializable data object Home : BudgetBuddyRoute
}

// Main Navigation Graph
@Composable
fun AlmAwareNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = BudgetBuddyRoute.Splash
    ) {
        // Splash Screen
        composable<BudgetBuddyRoute.Splash> {
            SplashScreen(
                onSplashFinished = {
                    navController.navigate(BudgetBuddyRoute.SignIn) {
                        popUpTo(BudgetBuddyRoute.Splash) { inclusive = true }
                    }
                }
            )
        }

        composable<BudgetBuddyRoute.SignUp> {
            SignUpScreen(navController)
        }
        composable<BudgetBuddyRoute.SignIn> {
            SignInScreen(navController)
        }

        // Main App Screens
        composable<BudgetBuddyRoute.Home> {
            HomeScreen(navController)
        }
    }
}

// Main App Navigation Wrapper
@Composable
fun BudgetBuddyNavigation(
    navController: NavHostController = rememberNavController()
) {
    AlmAwareNavGraph(navController = navController)
}