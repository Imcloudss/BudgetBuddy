package com.example.budgetbuddy.ui.screens.splash

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import com.example.budgetbuddy.utils.SimpleLottieAnimation
import com.example.budgetbuddy.utils.AnimationUrls
import com.airbnb.lottie.compose.rememberLottieComposition
import com.airbnb.lottie.compose.LottieCompositionSpec

@Composable
fun SplashScreen(
    onSplashFinished: () -> Unit
) {
    // Preload dell'animazione Lottie
    val composition by rememberLottieComposition(
        spec = LottieCompositionSpec.Url(AnimationUrls.SPLASH_ANIMATION)
    )

    // Animazioni per testo
    val textAlpha = remember { Animatable(0f) }
    val textScale = remember { Animatable(0.8f) }

    // Effetto per avviare le animazioni SOLO quando l'animazione è caricata
    LaunchedEffect(composition) {
        if (composition != null) {
            // Animazione di entrata del testo
            delay(800) // Ridotto perché l'animazione è già caricata

            textAlpha.animateTo(
                targetValue = 1f,
                animationSpec = tween(
                    durationMillis = 800,
                    easing = EaseOutCubic
                )
            )

            textScale.animateTo(
                targetValue = 1f,
                animationSpec = tween(
                    durationMillis = 600,
                    easing = EaseOutBack
                )
            )

            // Pausa per far vedere l'animazione completa
            delay(2500)

            // Animazione di uscita
            textAlpha.animateTo(
                targetValue = 0f,
                animationSpec = tween(durationMillis = 500)
            )

            // Finisci splash
            onSplashFinished()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF3A6DF0)), // Sfondo blu del brand
        contentAlignment = Alignment.Center
    ) {
        if (composition != null) {
            // Mostra l'animazione solo quando è completamente caricata
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Animazione Lottie
                SimpleLottieAnimation(
                    animationUrl = AnimationUrls.SPLASH_ANIMATION,
                    size = 200.dp,
                    speed = 1.2f
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Testo con animazione
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .alpha(textAlpha.value)
                        .scale(textScale.value)
                ) {
                    Text(
                        text = "BudgetBuddy",
                        color = Color.White,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Il tuo assistente finanziario",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 16.sp
                    )
                }
            }
        } else {
            // Loading indicator elegante mentre carica l'animazione
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator(
                    color = Color.White,
                    modifier = Modifier.size(60.dp),
                    strokeWidth = 4.dp
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "BudgetBuddy",
                    color = Color.White,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Caricamento...",
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 16.sp
                )
            }
        }
    }
}