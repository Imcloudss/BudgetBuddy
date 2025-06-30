package com.example.budgetbuddy.data.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import android.content.Context

@Database(
    entities = [
        Category::class,
        Transaction::class,
        Goal::class
    ],
    version = 1,
    exportSchema = false // Semplifichiamo anche questo per ora
)
@TypeConverters(Converters::class)
abstract class BudgetBuddyDatabase : RoomDatabase() {

    // Abstract DAO methods
    abstract fun categoryDao(): CategoryDao
    abstract fun transactionDao(): TransactionDao
    abstract fun goalDao(): GoalDao

    companion object {
        @Volatile
        private var INSTANCE: BudgetBuddyDatabase? = null

        fun getDatabase(context: Context): BudgetBuddyDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BudgetBuddyDatabase::class.java,
                    "budget_buddy_database"
                )
                    .addCallback(DatabaseCallback())
                    // CORREZIONE: Usa la versione non deprecata
                    .fallbackToDestructiveMigration(dropAllTables = true) // Per development
                    .build()
                INSTANCE = instance
                instance
            }
        }

        // Callback semplificato per dati iniziali
        private class DatabaseCallback : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                populateDefaultCategories(db)
            }
        }

        private fun populateDefaultCategories(db: SupportSQLiteDatabase) {
            // Categorie EXPENSE (Uscite) - Solo le essenziali
            val expenseCategories = listOf(
                "('Alimentari', '🛒', '#FF5722', 'EXPENSE')",
                "('Trasporti', '🚗', '#2196F3', 'EXPENSE')",
                "('Casa', '🏠', '#795548', 'EXPENSE')",
                "('Intrattenimento', '🎬', '#9C27B0', 'EXPENSE')",
                "('Salute', '🏥', '#E91E63', 'EXPENSE')",
                "('Altro', '📝', '#757575', 'EXPENSE')"
            )

            // Categorie INCOME (Entrate) - Solo le essenziali
            val incomeCategories = listOf(
                "('Stipendio', '💰', '#4CAF50', 'INCOME')",
                "('Freelance', '💼', '#2196F3', 'INCOME')",
                "('Bonus', '🎉', '#FF9800', 'INCOME')",
                "('Altro', '📝', '#757575', 'INCOME')"
            )

            val allCategories = expenseCategories + incomeCategories

            allCategories.forEach { category ->
                db.execSQL("""
                    INSERT INTO categories (name, icon, color, type) 
                    VALUES $category
                """)
            }
        }
    }
}