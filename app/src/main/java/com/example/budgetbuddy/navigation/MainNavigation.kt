package com.example.budgetbuddy.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.*
import com.example.budgetbuddy.ui.screens.auth.LoginScreen
import com.example.budgetbuddy.ui.screens.splash.SplashScreen

@Composable
fun BudgetBuddyNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = SPLASH_ROUTE) {
        composable(SPLASH_ROUTE) {
            SplashScreen(
                onSplashFinished = {
                    navController.navigate(LOGIN_ROUTE) {
                        popUpTo(SPLASH_ROUTE) { inclusive = true }
                    }
                }
            )
        }
        composable(LOGIN_ROUTE) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(MAIN_ROUTE) {
                        popUpTo(LOGIN_ROUTE) { inclusive = true }
                    }
                },
                onNavigateToRegister = {}
            )
        }
        composable(MAIN_ROUTE) {
            MainAppContent(rootNavController = navController)
        }
    }
}

