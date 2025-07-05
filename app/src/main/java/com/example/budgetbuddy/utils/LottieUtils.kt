package com.example.budgetbuddy.utils

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition

@Composable
fun SimpleLottieAnimation(
    animationUrl: String,
    modifier: Modifier = Modifier,
    size: Dp = 200.dp,
    iterations: Int = LottieConstants.IterateForever,
    speed: Float = 1f
) {
    val composition by rememberLottieComposition(
        spec = LottieCompositionSpec.Url(animationUrl)
    )

    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = iterations,
        speed = speed
    )

    LottieAnimation(
        composition = composition,
        progress = { progress },
        modifier = modifier.size(size)
    )
}

// URL delle animazioni
object AnimationUrls {
    const val SPLASH_ANIMATION =
        "https://lottie.host/144d64cf-15b2-4ef5-816d-1f06a1dea320/IIykuP2BlK.lottie"
    const val SIGN_IN_ANIMATION =
        "https://lottie.host/95aaf697-45df-46a2-8a96-c7c63942bd00/nCJKa0SySQ.lottie"
    const val SIGN_UP_ANIMATION =
        "https://lottie.host/4e69bc39-16a7-491f-9c2d-98f16ee60acd/PS04Gq9eSg.lottie"
}