package com.example.budgetbuddy

import com.example.budgetbuddy.data.database.BudgetBuddyDatabase
import com.example.budgetbuddy.data.repositories.*
import com.example.budgetbuddy.ui.screens.home.HomeViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Modulo Koin per la configurazione della dependency injection
 */
val appModule = module {

    // Database - Singleton
    single { BudgetBuddyDatabase.getDatabase(androidContext()) }

    // DAOs
    single { get<BudgetBuddyDatabase>().categoryDao() }
    single { get<BudgetBuddyDatabase>().transactionDao() }
    single { get<BudgetBuddyDatabase>().goalDao() }

    // Repositories
    single { CategoryRepository(get()) }
    single { TransactionRepository(get(), get()) }
    single { GoalRepository(get()) }
    single { BudgetRepository(get(), get(), get()) }

    // ViewModels
    viewModel { HomeViewModel(get(), get(), get()) }
}