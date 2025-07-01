package com.example.budgetbuddy.ui.screens.add

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.budgetbuddy.ui.theme.BudgetBuddyBottomBar

@Composable
fun AddTransactionScreen(navController: NavController) {
    val isEntrata by remember { mutableStateOf(false) }
    val selectedCategory by remember { mutableStateOf<CategoryItem?>(null) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            BudgetBuddyBottomBar(navController = navController)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF3A6DF0))
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            // Header
            Text(
                text = "Aggiungi transazione",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Toggle Entrata/Uscita
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                ) {
                    // Entrata
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(
                                if (isEntrata) Color(0xFF4CAF50) else Color.Transparent
                            )
                            .clickable { TODO("Toggle to Entrata") }
                            .padding(12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Entrata",
                            color = if (isEntrata) Color.White else Color.Black,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    // Uscita
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(
                                if (!isEntrata) Color(0xFFE53E3E) else Color.Transparent
                            )
                            .clickable { TODO("Toggle to Uscita") }
                            .padding(12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Uscita",
                            color = if (!isEntrata) Color.White else Color.Black,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Campo Totale
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Totale",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )

                    OutlinedTextField(
                        value = "50,00",
                        onValueChange = { TODO("Update amount") },
                        modifier = Modifier.fillMaxWidth(),
                        textStyle = androidx.compose.ui.text.TextStyle(
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        suffix = { Text("€", fontSize = 24.sp) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF3A6DF0),
                            unfocusedBorderColor = Color.Gray.copy(alpha = 0.3f)
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Categorie
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Categoria",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Griglia categorie
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(4),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.height(200.dp)
                    ) {
                        items(sampleCategories) { category ->
                            CategoryIcon(
                                category = category,
                                isSelected = selectedCategory == category,
                                onCategorySelected = { TODO("Select category") }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Campo Data
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Data",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )

                    OutlinedTextField(
                        value = "Oggi",
                        onValueChange = { TODO("Update date") },
                        modifier = Modifier.fillMaxWidth(),
                        readOnly = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF3A6DF0),
                            unfocusedBorderColor = Color.Gray.copy(alpha = 0.3f)
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Campo Note
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Note",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )

                    OutlinedTextField(
                        value = "",
                        onValueChange = { TODO("Update notes") },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Aggiungi una nota...") },
                        maxLines = 3,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF3A6DF0),
                            unfocusedBorderColor = Color.Gray.copy(alpha = 0.3f)
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Pulsante Salva
            Button(
                onClick = { TODO("Save transaction") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color(0xFF3A6DF0)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Salva",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
private fun CategoryIcon(
    category: CategoryItem,
    isSelected: Boolean,
    onCategorySelected: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onCategorySelected() }
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(
                    color = if (isSelected) Color(0xFF3A6DF0) else Color(0xFFF5F5F5),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = category.icon,
                fontSize = 20.sp
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = category.name,
            fontSize = 10.sp,
            textAlign = TextAlign.Center,
            maxLines = 1
        )
    }
}

// Data class e dati di esempio
data class CategoryItem(
    val id: String,
    val name: String,
    val icon: String,
    val color: String
)

private val sampleCategories = listOf(
    CategoryItem("1", "Casa", "🏠", "#795548"),
    CategoryItem("2", "Trasporti", "🚗", "#2196F3"),
    CategoryItem("3", "Shopping", "🛍️", "#E91E63"),
    CategoryItem("4", "Persone", "👥", "#FF9800"),
    CategoryItem("5", "Casa", "🏠", "#795548"),
    CategoryItem("6", "Shopping", "🛍️", "#E91E63"),
    CategoryItem("7", "Persone", "👥", "#FF9800"),
    CategoryItem("8", "Altro", "📝", "#757575")
)