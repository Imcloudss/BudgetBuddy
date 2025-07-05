package com.example.budgetbuddy.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import com.example.budgetbuddy.ui.composables.TopAppBar

@Composable
fun HomeScreen(
    navController: NavHostController
) {
    var selectedTab by remember { mutableIntStateOf(0) }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column {
            TopAppBar(
                navController = navController,
                "home"
            )
            Spacer(modifier = Modifier.weight(1f))
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp)
                .padding(top = 195.dp)
                .height(500.dp)
                .zIndex(1f),
            shape = RoundedCornerShape(15),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF474A52)),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                Column {
                    TabRow(
                        selectedTabIndex = selectedTab,
                        containerColor = Color.Transparent,
                        contentColor = Color.White,
                        indicator = { tabPositions ->
                            Box(
                                Modifier
                                    .tabIndicatorOffset(tabPositions[selectedTab])
                                    .fillMaxWidth()
                                    .padding(horizontal = 20.dp)
                                    .height(3.dp)
                                    .background(Color(0xFF4ECDC4))
                            )
                        },
                        divider = {},
                        modifier = Modifier.padding(vertical = 15.dp)
                    ) {
                        Tab(
                            selected = selectedTab == 0,
                            onClick = { selectedTab = 0 },
                            text = {
                                Text(
                                    "Giorno",
                                    fontSize = 16.sp,
                                    fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal,
                                    color = if (selectedTab == 0) Color.White else Color.White.copy(alpha = 0.7f)
                                )
                            }
                        )
                        Tab(
                            selected = selectedTab == 1,
                            onClick = { selectedTab = 1 },
                            text = {
                                Text(
                                    "Mese",
                                    fontSize = 16.sp,
                                    fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal,
                                    color = if (selectedTab == 1) Color.White else Color.White.copy(alpha = 0.7f)
                                )
                            }
                        )
                        Tab(
                            selected = selectedTab == 2,
                            onClick = { selectedTab = 2 },
                            text = {
                                Text(
                                    "Anno",
                                    fontSize = 16.sp,
                                    fontWeight = if (selectedTab == 2) FontWeight.Bold else FontWeight.Normal,
                                    color = if (selectedTab == 2) Color.White else Color.White.copy(alpha = 0.7f)
                                )
                            }
                        )
                        Tab(
                            selected = selectedTab == 3,
                            onClick = { selectedTab = 3 },
                            text = {
                                Text(
                                    "Periodo",
                                    fontSize = 16.sp,
                                    fontWeight = if (selectedTab == 3) FontWeight.Bold else FontWeight.Normal,
                                    color = if (selectedTab == 3) Color.White else Color.White.copy(alpha = 0.7f)
                                )
                            }
                        )
                    }
                }

                FloatingActionButton(
                    onClick = { /* Handle click */ },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(25.dp),
                    containerColor = Color(0xFFFFD700),
                    contentColor = Color.Black
                ) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = "Add a new transaction",
                    )
                }
            }
        }
    }
}