package com.zeroboss.scoring500.presentation.screens.home

import com.zeroboss.scoring500.domain.model.Match

data class HomeState(
    val matches: List<Match> = emptyList(),
    var expandedCard: Match? = null,
    var showDeleteMatch: Boolean = false,
    var showDeleteGame: Boolean = false
)