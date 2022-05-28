package com.zeroboss.scoring500.presentation.screens.tutorial

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.zeroboss.scoring500.ui.theme.smallerText

@Composable
fun IntroductionDetail() {
    Text(
        modifier = Modifier.padding(start = 20.dp, end = 10.dp),
        text = "Scoring 500 enables you to keep track of all your 500 games. It remembers scores between teams and players, and you can see details of each game played, i.e., the bidding history of each hand played within a game. It also gives a ranking of teams and players so you can see who’s the best in your group.",
        style = smallerText
    )

    Text(
        modifier = Modifier.padding(start = 20.dp, end = 10.dp, top = 10.dp),
        text = "Two types of games are supported. The first is with four players in two teams. The second is with three players playing against each other.",
        style = smallerText
    )

}