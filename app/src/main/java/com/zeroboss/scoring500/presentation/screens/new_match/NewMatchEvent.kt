package com.zeroboss.scoring500.presentation.screens.new_match

sealed class NewMatchEvent {
    object ThreePlayerChanged : NewMatchEvent()

    data class ThreePlayerNameChanged(
        val playerId: Int,
        val text: String
    ) : NewMatchEvent()

    data class ThreePlayerDropDownClicked(
        val playerId: Int,
        val visible: Boolean
    ) : NewMatchEvent()

    object ClearAllThreePlayerLists : NewMatchEvent()

    data class PlayerNameChanged(
        val teamId: Int,
        val playerId: Int,
        val text: String
    ) : NewMatchEvent()

    data class SetPlayerNames(
        val teamId: Int,
        val playerNames: List<String>
    ) : NewMatchEvent()

    data class TeamDropDownClicked(
        val teamId: Int,
        val visible: Boolean
    ) : NewMatchEvent()

    data class PlayerDropDownClicked(
        val teamId: Int,
        val playerId: Int,
        val visible: Boolean
    ) : NewMatchEvent()

    object ClearAllTeamLists : NewMatchEvent()
    object ClearAllPlayerLists : NewMatchEvent()
    object SaveMatch : NewMatchEvent()

}
