package com.example.budgetbuddy.data.repositories

import com.example.budgetbuddy.data.database.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import java.time.LocalDate

/**
 * Repository per la gestione delle transazioni
 * Single Source of Truth per i dati delle transazioni
 */
class TransactionRepository(
    private val transactionDao: TransactionDao,
    private val categoryDao: CategoryDao
) {

    /**
     * Ottiene tutte le transazioni ordinate per data (più recenti prima)
     */
    fun getAllTransactions(): Flow<List<Transaction>> =
        transactionDao.getAllTransactions()

    /**
     * Ottiene le transazioni con le informazioni della categoria
     * Combina i Flow di transazioni e categorie
     */
    fun getAllTransactionsWithCategory(): Flow<List<TransactionWithCategory>> {
        return combine(
            transactionDao.getAllTransactions(),
            categoryDao.getAllCategories()
        ) { transactions, categories ->
            transactions.map { transaction ->
                TransactionWithCategory(
                    transaction = transaction,
                    category = categories.first { it.id == transaction.categoryId }
                )
            }
        }
    }

    /**
     * Ottiene le transazioni recenti con limite
     */
    fun getRecentTransactions(limit: Int = 10): Flow<List<Transaction>> =
        transactionDao.getRecentTransactions(limit)

    /**
     * Ottiene le transazioni filtrate per tipo
     */
    fun getTransactionsByType(type: TransactionType): Flow<List<Transaction>> =
        transactionDao.getTransactionsByType(type)

    /**
     * Ottiene le transazioni di una specifica categoria
     */
    fun getTransactionsByCategory(categoryId: Long): Flow<List<Transaction>> =
        transactionDao.getTransactionsByCategory(categoryId)

    /**
     * Ottiene una transazione specifica per ID
     */
    suspend fun getTransactionById(id: Long): Transaction? =
        transactionDao.getTransactionById(id)

    /**
     * Inserisce una nuova transazione
     * @return ID della transazione inserita
     */
    suspend fun insertTransaction(transaction: Transaction): Long =
        transactionDao.insertTransaction(transaction)

    /**
     * Aggiorna una transazione esistente
     */
    suspend fun updateTransaction(transaction: Transaction) =
        transactionDao.updateTransaction(transaction)

    /**
     * Elimina una transazione
     */
    suspend fun deleteTransaction(transaction: Transaction) =
        transactionDao.deleteTransaction(transaction)

    /**
     * Crea una nuova transazione con validazione
     */
    suspend fun createTransaction(
        amount: Double,
        type: TransactionType,
        categoryId: Long,
        date: LocalDate,
        note: String? = null
    ): Result<Long> {
        return try {
            // Validazione
            require(amount > 0) { "L'importo deve essere positivo" }

            // Verifica che la categoria esista
            val category = categoryDao.getCategoryById(categoryId)
            requireNotNull(category) { "Categoria non trovata" }
            require(category.type == type) { "Il tipo della categoria non corrisponde al tipo della transazione" }

            val transaction = Transaction(
                amount = amount,
                type = type,
                categoryId = categoryId,
                date = date,
                note = note?.trim()?.takeIf { it.isNotEmpty() }
            )

            val id = transactionDao.insertTransaction(transaction)
            Result.success(id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Ottiene le statistiche per tipo di transazione
     */
    suspend fun getTotalByType(type: TransactionType): Double =
        transactionDao.getTotalByType(type) ?: 0.0

    /**
     * Ottiene il bilancio totale (entrate - uscite)
     */
    suspend fun getBalance(): Double {
        val income = getTotalByType(TransactionType.INCOME)
        val expenses = getTotalByType(TransactionType.EXPENSE)
        return income - expenses
    }

    /**
     * Ottiene il numero totale di transazioni
     */
    suspend fun getTotalTransactionCount(): Int =
        transactionDao.getTotalCount()
}