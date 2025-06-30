package com.example.budgetbuddy.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.budgetbuddy.data.database.TransactionType
import com.example.budgetbuddy.data.database.TransactionWithCategory
import com.example.budgetbuddy.data.repositories.GoalWithProgress
import org.koin.androidx.compose.koinViewModel
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToAddTransaction: () -> Unit,
    onNavigateToTransactions: () -> Unit,
    onNavigateToGoal: (Long) -> Unit,
    onNavigateToProfile: (Long) -> Unit,
    viewModel: HomeViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val recentTransactions by viewModel.recentTransactions.collectAsState(initial = emptyList())
    val activeGoals by viewModel.activeGoals.collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "BudgetBuddy",
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    IconButton(onClick = { /* TODO: Navigate to settings */ }) {
                        Icon(Icons.Default.Settings, contentDescription = "Impostazioni")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToAddTransaction,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Aggiungi transazione")
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                uiState.error != null -> {
                    ErrorMessage(
                        message = uiState.error!!,
                        onRetry = { viewModel.refreshData() },
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                else -> {
                    HomeContent(
                        uiState = uiState,
                        recentTransactions = recentTransactions,
                        activeGoals = activeGoals,
                        onTransactionClick = { /* TODO */ },
                        onSeeAllTransactionsClick = onNavigateToTransactions,
                        onGoalClick = onNavigateToGoal,
                        onProfileClick = onNavigateToProfile,
                        onRefresh = { viewModel.refreshData() }
                    )
                }
            }
        }
    }
}

@Composable
private fun HomeContent(
    uiState: HomeUiState,
    recentTransactions: List<TransactionWithCategory>,
    activeGoals: List<GoalWithProgress>,
    onTransactionClick: (Long) -> Unit,
    onSeeAllTransactionsClick: () -> Unit,
    onGoalClick: (Long) -> Unit,
    onProfileClick: (Long) -> Unit,
    onRefresh: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Balance Card
        item {
            BalanceCard(
                balance = uiState.balance,
                income = uiState.monthlyIncome,
                expenses = uiState.monthlyExpenses,
                balanceColor = uiState.balanceColor
            )
        }

        // Quick Stats
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                QuickStatCard(
                    title = "Risparmio",
                    value = "${uiState.savingsRate.toInt()}%",
                    icon = Icons.AutoMirrored.Filled.TrendingUp,
                    modifier = Modifier.weight(1f)
                )
                QuickStatCard(
                    title = "Transazioni",
                    value = recentTransactions.size.toString(),
                    icon = Icons.Default.Receipt,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Recent Transactions Section
        if (recentTransactions.isNotEmpty()) {
            item {
                SectionHeader(
                    title = "Transazioni Recenti",
                    onSeeAllClick = onSeeAllTransactionsClick
                )
            }

            items(recentTransactions) { transactionWithCategory ->
                TransactionItem(
                    transactionWithCategory = transactionWithCategory,
                    onClick = { onTransactionClick(transactionWithCategory.transaction.id) }
                )
            }
        }

        // Active Goals Section
        if (activeGoals.isNotEmpty()) {
            item {
                SectionHeader(
                    title = "Obiettivi Attivi",
                    onSeeAllClick = null
                )
            }

            items(activeGoals) { goalWithProgress ->
                GoalProgressCard(
                    goalWithProgress = goalWithProgress,
                    onClick = { onGoalClick(goalWithProgress.goal.id) }
                )
            }
        }

        // Empty state
        if (recentTransactions.isEmpty() && activeGoals.isEmpty()) {
            item {
                EmptyStateMessage()
            }
        }
    }
}

@Composable
private fun BalanceCard(
    balance: Double,
    income: Double,
    expenses: Double,
    balanceColor: BalanceColor
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Saldo Totale",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = String.format("€ %.2f", balance),
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.Bold,
                color = when (balanceColor) {
                    BalanceColor.POSITIVE -> Color(0xFF4CAF50)
                    BalanceColor.NEGATIVE -> Color(0xFFF44336)
                    BalanceColor.NEUTRAL -> MaterialTheme.colorScheme.onPrimaryContainer
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        Icons.Default.ArrowUpward,
                        contentDescription = null,
                        tint = Color(0xFF4CAF50)
                    )
                    Text(
                        text = String.format("€ %.2f", income),
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "Entrate",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        Icons.Default.ArrowDownward,
                        contentDescription = null,
                        tint = Color(0xFFF44336)
                    )
                    Text(
                        text = String.format("€ %.2f", expenses),
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "Uscite",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                    )
                }
            }
        }
    }
}

@Composable
private fun QuickStatCard(
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = value,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun SectionHeader(
    title: String,
    onSeeAllClick: (() -> Unit)?
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        onSeeAllClick?.let {
            TextButton(onClick = it) {
                Text("Vedi tutte")
            }
        }
    }
}

@Composable
private fun TransactionItem(
    transactionWithCategory: TransactionWithCategory,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Category Icon
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(android.graphics.Color.parseColor(transactionWithCategory.category.color)).copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = transactionWithCategory.category.icon,
                        fontSize = 20.sp
                    )
                }

                Column {
                    Text(
                        text = transactionWithCategory.category.name,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = transactionWithCategory.transaction.date.format(
                            DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
                        ),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Text(
                text = "${if (transactionWithCategory.transaction.type == TransactionType.EXPENSE) "-" else "+"} € ${String.format("%.2f", transactionWithCategory.transaction.amount)}",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = if (transactionWithCategory.transaction.type == TransactionType.EXPENSE)
                    Color(0xFFF44336) else Color(0xFF4CAF50)
            )
        }
    }
}

@Composable
private fun GoalProgressCard(
    goalWithProgress: GoalWithProgress,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = goalWithProgress.goal.title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.weight(1f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = "${goalWithProgress.progressPercentage.toInt()}%",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            LinearProgressIndicator(
                progress = { goalWithProgress.progressPercentage / 100f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "€ ${String.format("%.2f", goalWithProgress.goal.currentAmount)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Text(
                    text = "€ ${String.format("%.2f", goalWithProgress.goal.targetAmount)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun ErrorMessage(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )

        Button(onClick = onRetry) {
            Text("Riprova")
        }
    }
}

@Composable
private fun EmptyStateMessage() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Icon(
            Icons.Default.AccountBalanceWallet,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
        )

        Text(
            text = "Inizia ad aggiungere transazioni",
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center
        )

        Text(
            text = "Tocca il pulsante + per aggiungere la tua prima transazione",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}