package com.example.budgetbuddy.data.repositories

import com.example.budgetbuddy.data.database.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import java.time.LocalDate
import java.time.YearMonth

/**
 * Repository principale che coordina tutti i dati dell'app
 * Utile per operazioni che coinvolgono più entità
 */
class BudgetRepository(
    private val categoryRepository: CategoryRepository,
    private val transactionRepository: TransactionRepository,
    private val goalRepository: GoalRepository
) {

    /**
     * Ottiene una panoramica completa del budget
     */
    suspend fun getBudgetOverview(): BudgetOverview {
        val balance = transactionRepository.getBalance()
        val totalIncome = transactionRepository.getTotalByType(TransactionType.INCOME)
        val totalExpenses = transactionRepository.getTotalByType(TransactionType.EXPENSE)
        val transactionCount = transactionRepository.getTotalTransactionCount()

        return BudgetOverview(
            balance = balance,
            totalIncome = totalIncome,
            totalExpenses = totalExpenses,
            transactionCount = transactionCount
        )
    }

    /**
     * Ottiene le statistiche mensili per il mese corrente
     */
    suspend fun getCurrentMonthStats(): MonthlyStats {
        val currentMonth = YearMonth.now()
        val startDate = currentMonth.atDay(1)
        val endDate = currentMonth.atEndOfMonth()

        return getMonthlyStats(startDate, endDate)
    }

    /**
     * Ottiene le statistiche per un periodo specifico
     */
    suspend fun getMonthlyStats(startDate: LocalDate, endDate: LocalDate): MonthlyStats {
        // Qui potresti implementare query più complesse
        // Per ora usiamo i metodi esistenti
        val income = transactionRepository.getTotalByType(TransactionType.INCOME)
        val expenses = transactionRepository.getTotalByType(TransactionType.EXPENSE)

        return MonthlyStats(
            period = "$startDate - $endDate",
            income = income,
            expenses = expenses,
            balance = income - expenses,
            savingsRate = if (income > 0) ((income - expenses) / income * 100).toFloat() else 0f
        )
    }

    /**
     * Ottiene un riepilogo completo per la dashboard
     */
    fun getDashboardData(): Flow<DashboardData> {
        return combine(
            transactionRepository.getRecentTransactions(5),
            goalRepository.getActiveGoalsWithProgress(),
            categoryRepository.getAllCategories()
        ) { recentTransactions, activeGoals, categories ->
            DashboardData(
                recentTransactions = recentTransactions,
                activeGoals = activeGoals,
                categoriesCount = categories.size,
                incomeCategories = categories.count { it.type == TransactionType.INCOME },
                expenseCategories = categories.count { it.type == TransactionType.EXPENSE }
            )
        }
    }
}

/**
 * Data class per la panoramica del budget
 */
data class BudgetOverview(
    val balance: Double,
    val totalIncome: Double,
    val totalExpenses: Double,
    val transactionCount: Int
)

/**
 * Data class per le statistiche mensili
 */
data class MonthlyStats(
    val period: String,
    val income: Double,
    val expenses: Double,
    val balance: Double,
    val savingsRate: Float
)

/**
 * Data class per i dati della dashboard
 */
data class DashboardData(
    val recentTransactions: List<Transaction>,
    val activeGoals: List<GoalWithProgress>,
    val categoriesCount: Int,
    val incomeCategories: Int,
    val expenseCategories: Int
)