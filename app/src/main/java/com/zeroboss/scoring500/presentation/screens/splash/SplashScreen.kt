package com.zeroboss.scoring500.presentation.screens.splash

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.zeroboss.scoring500.ui.theme.Blue50
import kotlinx.coroutines.delay
import java.util.*
import com.zeroboss.scoring500.R
import com.zeroboss.scoring500.presentation.screens.destinations.HomeScreenDestination
import com.zeroboss.scoring500.presentation.screens.destinations.LoginScreenDestination
import kotlin.concurrent.schedule

@Destination(start = true)
@Composable
fun SplashScreen(
    navigator: DestinationsNavigator
) {
    SplashScreenContent()
    LaunchedEffect(
        key1 = "JumpToLogin",
        block = {
            delay(4000)
            navigator.navigate(HomeScreenDestination())
        }
    )
}

@Composable
fun SplashScreenContent() {
    val doRotation = remember { mutableStateOf(false) }

    val alpha: Float by animateFloatAsState(
        targetValue = if (doRotation.value) 1f else 0f,
        animationSpec = tween(
            durationMillis = 4000,
            easing = LinearEasing
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Blue50)
    ) {
        val screenWidth = LocalConfiguration.current.screenWidthDp

        var angle = -30F

        arrayOf(
            R.drawable.joker,
            R.drawable.ace,
            R.drawable.king,
            R.drawable.queen,
            R.drawable.jack
        ).forEach { image ->
            PlayingCard(
                screenWidth,
                image,
                angle,
                doRotation
            )
            angle += 20F
        }

        Text(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 100.dp)
                .alpha(alpha),
            text = "Scoring 500",
            fontSize = 55.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color.Black
        )
    }

    Timer("Wait", false).schedule(100) {
        doRotation.value = true
    }
}