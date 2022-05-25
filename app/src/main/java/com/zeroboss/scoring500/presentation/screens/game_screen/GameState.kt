package com.zeroboss.scoring500.presentation.screens.game_screen

import com.zeroboss.scoring500.domain.model.Hand
import com.zeroboss.scoring500.domain.model.Match

data class GameState(
    val hands: List<Hand> = emptyList(),
)