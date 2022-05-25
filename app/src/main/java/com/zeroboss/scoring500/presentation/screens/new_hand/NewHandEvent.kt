package com.zeroboss.scoring500.presentation.screens.new_hand

import com.zeroboss.scoring500.domain.model.Player
import com.zeroboss.scoring500.domain.model.Team
import com.zeroboss.scoring500.presentation.common.Trump

sealed class NewHandEvent {
    data class SelectTeam(val team: Team, val teamId: Int) : NewHandEvent()
    data class SelectPlayer(val player: Player, val playerId: Int) : NewHandEvent()
    data class SelectedBid(val trump: Trump) : NewHandEvent()
    data class SetTeamTricksWon(val teamId: Int, val tricks: Int) : NewHandEvent()
    data class SetPlayerTricksWon(val playerId: Int, val tricks: Int) : NewHandEvent()
    object SaveHand : NewHandEvent()
}
