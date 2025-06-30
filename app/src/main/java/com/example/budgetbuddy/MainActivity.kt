package com.example.budgetbuddy

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import com.example.budgetbuddy.data.database.BudgetBuddyDatabase
import com.example.budgetbuddy.navigation.BudgetBuddyNavigation
import com.example.budgetbuddy.ui.theme.BudgetBuddyTheme
import kotlinx.coroutines.launch
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.budgetbuddy.data.database.*
import kotlinx.coroutines.flow.first
import java.time.LocalDate

class MainActivity : ComponentActivity() {

    // Variabile per il database
    private lateinit var database: BudgetBuddyDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inizializza il database
        database = BudgetBuddyDatabase.getDatabase(this)

        // Test del database
        testDatabase()

        setContent {
            BudgetBuddyTheme {
                BudgetBuddyNavigation()
                // DatabaseTestScreen()
            }
        }
    }

    private fun testDatabase() {
        lifecycleScope.launch {
            try {
                // Test 1: Verifica categorie pre-caricate
                Log.d("DB_TEST", "=== TEST CATEGORIE ===")
                val categories = database.categoryDao().getAllCategories().first()
                Log.d("DB_TEST", "Numero categorie caricate: ${categories.size}")
                categories.forEach { category ->
                    Log.d("DB_TEST", "Categoria: ${category.name} ${category.icon} - Tipo: ${category.type}")
                }

                // Test 2: Inserisci una transazione di test
                Log.d("DB_TEST", "\n=== TEST TRANSAZIONE ===")
                val expenseCategory = categories.find { it.type == TransactionType.EXPENSE }
                if (expenseCategory != null) {
                    val testTransaction = Transaction(
                        amount = 25.50,
                        type = TransactionType.EXPENSE,
                        categoryId = expenseCategory.id,
                        date = LocalDate.now(),
                        note = "Test spesa supermercato"
                    )
                    val transactionId = database.transactionDao().insertTransaction(testTransaction)
                    Log.d("DB_TEST", "Transazione inserita con ID: $transactionId")

                    // Verifica transazione inserita
                    val allTransactions = database.transactionDao().getAllTransactions().first()
                    Log.d("DB_TEST", "Numero transazioni totali: ${allTransactions.size}")

                    // Test statistiche
                    val totalExpenses = database.transactionDao().getTotalByType(TransactionType.EXPENSE)
                    Log.d("DB_TEST", "Totale spese: €$totalExpenses")
                }

                // Test 3: Inserisci un obiettivo
                Log.d("DB_TEST", "\n=== TEST OBIETTIVO ===")
                val testGoal = Goal(
                    title = "Vacanze Estate 2024",
                    targetAmount = 1500.0,
                    currentAmount = 250.0,
                    deadline = LocalDate.now().plusMonths(6),
                    isCompleted = false
                )
                val goalId = database.goalDao().insertGoal(testGoal)
                Log.d("DB_TEST", "Obiettivo inserito con ID: $goalId")

                // Verifica obiettivi attivi
                val activeGoals = database.goalDao().getActiveGoals().first()
                Log.d("DB_TEST", "Numero obiettivi attivi: ${activeGoals.size}")
                activeGoals.forEach { goal ->
                    Log.d("DB_TEST", "Obiettivo: ${goal.title} - Target: €${goal.targetAmount}")
                }

                Log.d("DB_TEST", "\n=== TUTTI I TEST COMPLETATI CON SUCCESSO ===")

            } catch (e: Exception) {
                Log.e("DB_TEST", "ERRORE nel test del database", e)
            }
        }
    }
}

@Composable
fun DatabaseTestScreen() {
    var testResult by remember { mutableStateOf("Test del database in corso...") }

    LaunchedEffect(Unit) {
        // Qui potresti aggiungere ulteriori test o mostrare i risultati nell'UI
        testResult = "Controlla il Logcat per i risultati del test!"
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Test Database BudgetBuddy",
                style = MaterialTheme.typography.headlineMedium
            )

            Text(
                text = testResult,
                style = MaterialTheme.typography.bodyLarge
            )

            Text(
                text = "Apri Logcat e filtra per 'DB_TEST'",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
