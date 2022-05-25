package com.zeroboss.scoring500.presentation.screens.home

import com.zeroboss.scoring500.data.common.NonBiddingPointsType
import com.zeroboss.scoring500.domain.model.Game
import com.zeroboss.scoring500.domain.model.Match

sealed class HomeEvent {
    data class MatchItemClicked(val match: Match) : HomeEvent()
    data class CardArrowClicked(val match: Match) : HomeEvent()
    data class DeleteMatch(val match: Match?) : HomeEvent()
    data class CreateNewGame(val match: Match?) : HomeEvent()
    object DeleteGame : HomeEvent()
    data class ShowDeleteMatchDialog(val show: Boolean) : HomeEvent()
    data class ShowDeleteGameDialog(val show: Boolean) : HomeEvent()
    data class SaveScoringRules(
        val nonBiddingPointsType: NonBiddingPointsType,
        val isTenTrickBonus: Boolean
    ) : HomeEvent()
}
