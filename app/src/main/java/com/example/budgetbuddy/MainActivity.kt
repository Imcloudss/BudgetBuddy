package com.example.budgetbuddy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.example.budgetbuddy.ui.theme.BudgetBuddyNavigation
import com.example.budgetbuddy.ui.theme.BudgetBuddyTheme

class MainActivity : ComponentActivity() {

    // Database
    // private lateinit var database: BudgetBuddyDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inizializzazione del database
        // database = BudgetBuddyDatabase.getDatabase(this)

        setContent {
            BudgetBuddyTheme {
                val navController = rememberNavController()
                BudgetBuddyNavigation(navController)
            }
        }
    }
}