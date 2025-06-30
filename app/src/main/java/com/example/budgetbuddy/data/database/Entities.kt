package com.example.budgetbuddy.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.TypeConverter
import java.time.LocalDate
import java.time.LocalDateTime

// ========================================================================================
// ENUMS E TYPE CONVERTERS
// ========================================================================================

enum class TransactionType {
    INCOME,    // Entrata
    EXPENSE    // Uscita
}

class Converters {
    @TypeConverter
    fun fromTransactionType(type: TransactionType): String = type.name

    @TypeConverter
    fun toTransactionType(type: String): TransactionType = TransactionType.valueOf(type)

    @TypeConverter
    fun fromLocalDate(date: LocalDate?): String? = date?.toString()

    @TypeConverter
    fun toLocalDate(dateString: String?): LocalDate? =
        dateString?.let { LocalDate.parse(it) }

    @TypeConverter
    fun fromLocalDateTime(dateTime: LocalDateTime?): String? = dateTime?.toString()

    @TypeConverter
    fun toLocalDateTime(dateTimeString: String?): LocalDateTime? =
        dateTimeString?.let { LocalDateTime.parse(it) }
}

// ========================================================================================
// ENTITÀ PRINCIPALI (SEMPLIFICATE)
// ========================================================================================

/**
 * Entità per le categorie di transazioni
 */
@Entity(tableName = "categories")
data class Category(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    val name: String,           // Nome categoria (es. "Alimentari", "Stipendio")
    val icon: String,           // Emoji (es. "🍕", "💰")
    val color: String,          // Colore in hex (es. "#FF5722")
    val type: TransactionType   // INCOME o EXPENSE
)

/**
 * Entità per le transazioni finanziarie
 */
@Entity(
    tableName = "transactions",
    foreignKeys = [
        ForeignKey(
            entity = Category::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["categoryId"])]
)
data class Transaction(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    val amount: Double,         // Importo (sempre positivo)
    val type: TransactionType,  // INCOME o EXPENSE
    val categoryId: Long,       // FK verso Category
    val date: LocalDate,        // Data della transazione
    val note: String? = null    // Note opzionali
)

/**
 * Entità per gli obiettivi di risparmio
 */
@Entity(tableName = "goals")
data class Goal(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    val title: String,          // Titolo obiettivo (es. "Vacanze Estate")
    val targetAmount: Double,   // Importo target da raggiungere
    val currentAmount: Double = 0.0, // Importo attualmente risparmiato
    val deadline: LocalDate? = null, // Data entro cui raggiungere l'obiettivo
    val isCompleted: Boolean = false // Se è completato o no
)

// ========================================================================================
// DATA CLASSES PER JOIN (SOLO QUELLE NECESSARIE)
// ========================================================================================

/**
 * Transazione con informazioni categoria
 */
data class TransactionWithCategory(
    val transaction: Transaction,
    val category: Category
)