package com.example.budgetbuddy.ui.screens.goals

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.budgetbuddy.ui.theme.BudgetBuddyBottomBar

@Composable
fun GoalsScreen(navController: NavController) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            BudgetBuddyBottomBar(navController = navController)
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { TODO("Add new goal") },
                containerColor = Color(0xFF3A6DF0),
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Goal")
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF3A6DF0))
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header
            item {
                Text(
                    text = "I tuoi obiettivi",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Goals List
            items(sampleGoals) { goal ->
                GoalCard(goal = goal)
            }

            // Nuovo obiettivo button
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.1f))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .background(
                                        Color.White.copy(alpha = 0.2f),
                                        CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Default.Add,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(24.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "Nuovo obiettivo",
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun GoalCard(goal: GoalItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Header con titolo e importo
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = goal.title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )

                    if (goal.location != null) {
                        Text(
                            text = goal.location,
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                }

                Text(
                    text = "${goal.currentAmount.toInt()}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF3A6DF0)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Progress Bar
            Column {
                LinearProgressIndicator(
                    progress = { goal.progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = goal.progressColor,
                    trackColor = goal.progressColor.copy(alpha = 0.2f),
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Progress info
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "${(goal.progress * 100).toInt()}%",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )

                    Text(
                        text = "€ ${goal.targetAmount.toInt()}",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Status e deadline
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Status badge
                Box(
                    modifier = Modifier
                        .background(
                            goal.statusColor.copy(alpha = 0.1f),
                            RoundedCornerShape(12.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = goal.status,
                        fontSize = 12.sp,
                        color = goal.statusColor,
                        fontWeight = FontWeight.Medium
                    )
                }

                if (goal.deadline != null) {
                    Text(
                        text = goal.deadline,
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}

// Data class per gli obiettivi
data class GoalItem(
    val id: String,
    val title: String,
    val location: String? = null,
    val currentAmount: Double,
    val targetAmount: Double,
    val progress: Float,
    val progressColor: Color,
    val status: String,
    val statusColor: Color,
    val deadline: String? = null
)

// Dati di esempio
private val sampleGoals = listOf(
    GoalItem(
        id = "1",
        title = "Viaggio a New York",
        location = "🗽",
        currentAmount = 1200.0,
        targetAmount = 2500.0,
        progress = 0.48f,
        progressColor = Color(0xFF4CAF50),
        status = "ATTIVO",
        statusColor = Color(0xFF4CAF50),
        deadline = "202-03"
    ),
    GoalItem(
        id = "2",
        title = "Regalo Giulia",
        currentAmount = 150.0,
        targetAmount = 300.0,
        progress = 0.50f,
        progressColor = Color(0xFFFF9800),
        status = "IN RITARDO",
        statusColor = Color(0xFFE53E3E),
        deadline = "2024-12"
    ),
    GoalItem(
        id = "3",
        title = "Quota riserva",
        currentAmount = 800.0,
        targetAmount = 1000.0,
        progress = 0.80f,
        progressColor = Color(0xFF2196F3),
        status = "QUASI FINITO",
        statusColor = Color(0xFF2196F3)
    ),
    GoalItem(
        id = "4",
        title = "Bullette marzo 2025",
        currentAmount = 45.0,
        targetAmount = 200.0,
        progress = 0.225f,
        progressColor = Color(0xFF9C27B0),
        status = "ATTIVO",
        statusColor = Color(0xFF4CAF50)
    ),
    GoalItem(
        id = "5",
        title = "Quota riserva",
        currentAmount = 300.0,
        targetAmount = 1000.0,
        progress = 0.30f,
        progressColor = Color(0xFF607D8B),
        status = "ATTIVO",
        statusColor = Color(0xFF4CAF50)
    )
)