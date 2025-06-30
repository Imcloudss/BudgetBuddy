package com.example.budgetbuddy.data.repositories

import com.example.budgetbuddy.data.database.Category
import com.example.budgetbuddy.data.database.CategoryDao
import com.example.budgetbuddy.data.database.TransactionType
import kotlinx.coroutines.flow.Flow

/**
 * Repository per la gestione delle categorie
 * Single Source of Truth per i dati delle categorie
 */
class CategoryRepository(
    private val categoryDao: CategoryDao
) {

    /**
     * Ottiene tutte le categorie ordinate per nome
     */
    fun getAllCategories(): Flow<List<Category>> =
        categoryDao.getAllCategories()

    /**
     * Ottiene le categorie filtrate per tipo (INCOME o EXPENSE)
     */
    fun getCategoriesByType(type: TransactionType): Flow<List<Category>> =
        categoryDao.getCategoriesByType(type)

    /**
     * Ottiene una categoria specifica per ID
     */
    suspend fun getCategoryById(id: Long): Category? =
        categoryDao.getCategoryById(id)

    /**
     * Inserisce una nuova categoria
     * @return ID della categoria inserita
     */
    suspend fun insertCategory(category: Category): Long =
        categoryDao.insertCategory(category)

    /**
     * Aggiorna una categoria esistente
     */
    suspend fun updateCategory(category: Category) =
        categoryDao.updateCategory(category)

    /**
     * Elimina una categoria
     * Nota: fallirà se ci sono transazioni associate (per via del ForeignKey)
     */
    suspend fun deleteCategory(category: Category) =
        categoryDao.deleteCategory(category)

    /**
     * Crea una nuova categoria con validazione
     */
    suspend fun createCategory(
        name: String,
        icon: String,
        color: String,
        type: TransactionType
    ): Result<Long> {
        return try {
            // Validazione base
            require(name.isNotBlank()) { "Il nome della categoria non può essere vuoto" }
            require(icon.isNotBlank()) { "L'icona non può essere vuota" }
            require(color.matches(Regex("^#[0-9A-Fa-f]{6}$"))) { "Colore non valido" }

            val category = Category(
                name = name.trim(),
                icon = icon,
                color = color.uppercase(),
                type = type
            )

            val id = categoryDao.insertCategory(category)
            Result.success(id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}