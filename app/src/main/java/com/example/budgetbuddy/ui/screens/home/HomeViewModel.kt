package com.example.budgetbuddy.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.budgetbuddy.data.database.Transaction
import com.example.budgetbuddy.data.database.TransactionWithCategory
import com.example.budgetbuddy.data.repositories.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * ViewModel per la schermata Home
 * Gestisce lo stato e la logica della dashboard principale
 */
class HomeViewModel(
    private val budgetRepository: BudgetRepository,
    private val transactionRepository: TransactionRepository,
    private val goalRepository: GoalRepository
) : ViewModel() {

    // UI State
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    // Transazioni recenti con categoria
    val recentTransactions: Flow<List<TransactionWithCategory>> =
        transactionRepository.getAllTransactionsWithCategory()
            .map { transactions ->
                transactions.take(5) // Ultime 5 transazioni
            }
            .catch { e ->
                _uiState.update { it.copy(error = e.message) }
                emit(emptyList<TransactionWithCategory>())
            }

    // Obiettivi attivi con progresso
    val activeGoals: Flow<List<GoalWithProgress>> =
        goalRepository.getActiveGoalsWithProgress()
            .catch { e ->
                _uiState.update { it.copy(error = e.message) }
                emit(emptyList<GoalWithProgress>())
            }

    init {
        loadDashboardData()
        observeBalanceChanges()
    }

    /**
     * Carica i dati iniziali della dashboard
     */
    private fun loadDashboardData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            try {
                // Carica overview del budget
                val overview = budgetRepository.getBudgetOverview()

                // Carica statistiche del mese corrente
                val monthlyStats = budgetRepository.getCurrentMonthStats()

                _uiState.update { state ->
                    state.copy(
                        balance = overview.balance,
                        totalIncome = overview.totalIncome,
                        totalExpenses = overview.totalExpenses,
                        monthlyIncome = monthlyStats.income,
                        monthlyExpenses = monthlyStats.expenses,
                        savingsRate = monthlyStats.savingsRate,
                        isLoading = false,
                        error = null
                    )
                }
            } catch (e: Exception) {
                _uiState.update { state ->
                    state.copy(
                        isLoading = false,
                        error = e.message ?: "Errore nel caricamento dei dati"
                    )
                }
            }
        }
    }

    /**
     * Osserva i cambiamenti del bilancio in tempo reale
     */
    private fun observeBalanceChanges() {
        // Quando ci sono nuove transazioni, aggiorna il bilancio
        transactionRepository.getAllTransactions()
            .onEach {
                // Ricarica i dati quando ci sono cambiamenti
                loadDashboardData()
            }
            .launchIn(viewModelScope)
    }

    /**
     * Aggiorna i dati (pull-to-refresh)
     */
    fun refreshData() {
        loadDashboardData()
    }

    /**
     * Elimina una transazione
     */
    fun deleteTransaction(transaction: Transaction) {
        viewModelScope.launch {
            try {
                transactionRepository.deleteTransaction(transaction)
                // I dati si aggiorneranno automaticamente grazie al Flow
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message) }
            }
        }
    }

    /**
     * Naviga alla schermata di aggiunta transazione
     */
    fun onAddTransactionClick() {
        // Gestito dalla Navigation nel Composable
    }

    /**
     * Naviga ai dettagli di un obiettivo
     */
    fun onGoalClick(goalId: Long) {
        // Gestito dalla Navigation nel Composable
    }

    /**
     * Naviga alla schermata di tutte le transazioni
     */
    fun onSeeAllTransactionsClick() {
        // Gestito dalla Navigation nel Composable
    }

    /**
     * Pulisce eventuali errori
     */
    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}

/**
 * UI State per la schermata Home
 */
data class HomeUiState(
    val balance: Double = 0.0,
    val totalIncome: Double = 0.0,
    val totalExpenses: Double = 0.0,
    val monthlyIncome: Double = 0.0,
    val monthlyExpenses: Double = 0.0,
    val savingsRate: Float = 0f,
    val isLoading: Boolean = false,
    val error: String? = null
) {
    // Proprietà calcolate per la UI
    val formattedBalance: String
        get() = String.format("€ %.2f", balance)

    val formattedTotalIncome: String
        get() = String.format("€ %.2f", totalIncome)

    val formattedTotalExpenses: String
        get() = String.format("€ %.2f", totalExpenses)

    val balanceColor: BalanceColor
        get() = when {
            balance > 0 -> BalanceColor.POSITIVE
            balance < 0 -> BalanceColor.NEGATIVE
            else -> BalanceColor.NEUTRAL
        }
}

/**
 * Enum per il colore del bilancio
 */
enum class BalanceColor {
    POSITIVE,
    NEGATIVE,
    NEUTRAL
}