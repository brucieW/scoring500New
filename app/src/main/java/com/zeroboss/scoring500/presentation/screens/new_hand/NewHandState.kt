package com.zeroboss.scoring500.presentation.screens.new_hand

import com.zeroboss.scoring500.domain.model.Player
import com.zeroboss.scoring500.domain.model.Team
import com.zeroboss.scoring500.presentation.common.Trump

data class NewHandState(
    val selectedTeam: Team? = null,
    val selectedOffset: Int = 0,
    val opposingTeamOffset: Int = 1,

    val selectionTypes: List<SelectionType> = listOf(
        SelectionType.NotSelected,
        SelectionType.NotSelected,
        SelectionType.NotSelected
    ),

    val selectedPlayer: Player? = null,

    val selectedBid: Trump? = null,

    val isDataValid: Boolean = false,
)
