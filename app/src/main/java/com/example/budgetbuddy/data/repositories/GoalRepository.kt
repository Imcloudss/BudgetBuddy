package com.example.budgetbuddy.data.repositories

import com.example.budgetbuddy.data.database.Goal
import com.example.budgetbuddy.data.database.GoalDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.time.temporal.ChronoUnit

/**
 * Repository per la gestione degli obiettivi di risparmio
 * Single Source of Truth per i dati degli obiettivi
 */
class GoalRepository(
    private val goalDao: GoalDao
) {

    /**
     * Ottiene tutti gli obiettivi ordinati per scadenza
     */
    fun getAllGoals(): Flow<List<Goal>> =
        goalDao.getAllGoals()

    /**
     * Ottiene solo gli obiettivi attivi (non completati)
     */
    fun getActiveGoals(): Flow<List<Goal>> =
        goalDao.getActiveGoals()

    /**
     * Ottiene gli obiettivi attivi con informazioni sul progresso
     */
    fun getActiveGoalsWithProgress(): Flow<List<GoalWithProgress>> {
        return goalDao.getActiveGoals().map { goals ->
            goals.map { goal ->
                GoalWithProgress(
                    goal = goal,
                    progressPercentage = calculateProgressPercentage(goal),
                    remainingAmount = goal.targetAmount - goal.currentAmount,
                    daysUntilDeadline = goal.deadline?.let {
                        ChronoUnit.DAYS.between(LocalDate.now(), it)
                    }
                )
            }
        }
    }

    /**
     * Ottiene un obiettivo specifico per ID
     */
    suspend fun getGoalById(id: Long): Goal? =
        goalDao.getGoalById(id)

    /**
     * Inserisce un nuovo obiettivo
     * @return ID dell'obiettivo inserito
     */
    suspend fun insertGoal(goal: Goal): Long =
        goalDao.insertGoal(goal)

    /**
     * Aggiorna un obiettivo esistente
     */
    suspend fun updateGoal(goal: Goal) =
        goalDao.updateGoal(goal)

    /**
     * Elimina un obiettivo
     */
    suspend fun deleteGoal(goal: Goal) =
        goalDao.deleteGoal(goal)

    /**
     * Segna un obiettivo come completato
     */
    suspend fun markGoalAsCompleted(goalId: Long) =
        goalDao.markGoalAsCompleted(goalId)

    /**
     * Aggiunge un importo all'obiettivo
     */
    suspend fun addAmountToGoal(goalId: Long, amount: Double): Result<Unit> {
        return try {
            require(amount > 0) { "L'importo deve essere positivo" }

            val goal = goalDao.getGoalById(goalId)
            requireNotNull(goal) { "Obiettivo non trovato" }
            require(!goal.isCompleted) { "L'obiettivo è già completato" }

            val newAmount = goal.currentAmount + amount
            goalDao.updateCurrentAmount(goalId, newAmount)

            // Se l'obiettivo è stato raggiunto, segnalo come completato
            if (newAmount >= goal.targetAmount) {
                goalDao.markGoalAsCompleted(goalId)
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Crea un nuovo obiettivo con validazione
     */
    suspend fun createGoal(
        title: String,
        targetAmount: Double,
        currentAmount: Double = 0.0,
        deadline: LocalDate? = null
    ): Result<Long> {
        return try {
            // Validazione
            require(title.isNotBlank()) { "Il titolo non può essere vuoto" }
            require(targetAmount > 0) { "L'importo target deve essere positivo" }
            require(currentAmount >= 0) { "L'importo corrente non può essere negativo" }
            require(currentAmount <= targetAmount) { "L'importo corrente non può superare il target" }
            deadline?.let {
                require(it.isAfter(LocalDate.now())) { "La scadenza deve essere futura" }
            }

            val goal = Goal(
                title = title.trim(),
                targetAmount = targetAmount,
                currentAmount = currentAmount,
                deadline = deadline,
                isCompleted = currentAmount >= targetAmount
            )

            val id = goalDao.insertGoal(goal)
            Result.success(id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Calcola la percentuale di progresso di un obiettivo
     */
    private fun calculateProgressPercentage(goal: Goal): Float {
        return if (goal.targetAmount > 0) {
            ((goal.currentAmount / goal.targetAmount) * 100).toFloat().coerceIn(0f, 100f)
        } else {
            0f
        }
    }
}

/**
 * Data class che combina un obiettivo con le informazioni sul progresso
 */
data class GoalWithProgress(
    val goal: Goal,
    val progressPercentage: Float,
    val remainingAmount: Double,
    val daysUntilDeadline: Long?
)