package com.zeroboss.scoring500.presentation.common

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zeroboss.scoring500.ui.theme.Blue100
import com.zeroboss.scoring500.ui.theme.Shapes
import com.zeroboss.scoring500.R

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun GoogleButton(
    text: String = "Sign up with Google",
    loadingText: String = "Creating Account..."
) {
    var clicked by remember { mutableStateOf(false) }

    Surface(
        onClick = { clicked = !clicked },
        shape = Shapes.medium,
        border = BorderStroke(width = 1.dp, color = Color.LightGray),
        color = Blue100
    ) {
        Row(
            modifier = Modifier
                .padding(start = 12.dp, end = 16.dp, top = 12.dp, bottom = 12.dp)
                .animateContentSize(
                    animationSpec = tween(
                        durationMillis = 300,
                        easing = LinearOutSlowInEasing
                    )
                )
        ) {
            Icon(
                painter = painterResource(id = R.drawable.google),
                contentDescription = "google button",
                tint = Color.Unspecified
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(if (clicked) loadingText else text)

            if (clicked) {
                Spacer(modifier = Modifier.width(16.dp))
                CircularProgressIndicator(
                    modifier = Modifier
                        .height(16.dp)
                        .width(16.dp),
                    strokeWidth = 2.dp,
                    color = Color.Black
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewGoogleButton() {
    GoogleButton()
}