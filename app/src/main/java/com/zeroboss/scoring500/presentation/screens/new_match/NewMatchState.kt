package com.zeroboss.scoring500app.presentation.screens.new_match

data class NewMatchState(
    val isThreePlayer: Boolean = false,
    val isTeamListVisible: List<Boolean> = mutableListOf(false, false),
    val isPlayerListVisible: List<Boolean> = mutableListOf(false, false, false, false),
    val isDataValid: Boolean = false,
    val isUniquePlayerNames: Boolean = true,
)


