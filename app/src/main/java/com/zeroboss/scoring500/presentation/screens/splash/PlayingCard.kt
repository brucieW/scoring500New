package com.zeroboss.scoring500.presentation.screens.splash

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun PlayingCard(
    screenWidth: Int,
    image: Int,
    targetAngle: Float,
    doRotation: MutableState<Boolean>
) {

    val angle: Float by animateFloatAsState(
        targetValue = if (doRotation.value) targetAngle else 0F,
        animationSpec = tween(
            durationMillis = 4000,
            easing = FastOutSlowInEasing
        )
    )

    val width = (screenWidth / 3).dp
    val height = (screenWidth + (screenWidth / 3)).dp

    Box(
        Modifier
            .padding(start = width)
            .width(width)
            .height(height)
            .rotate(angle)
    ) {
        Image(
            painterResource(image),
            modifier = Modifier.fillMaxSize(),
            contentDescription = "card",
            contentScale = ContentScale.Fit
        )
    }
}

