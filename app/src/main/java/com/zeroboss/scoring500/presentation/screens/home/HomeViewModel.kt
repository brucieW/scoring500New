package com.zeroboss.scoring500.presentation.screens.home

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.zeroboss.scoring500.data.common.ActiveStatus.activeGame
import com.zeroboss.scoring500.data.common.ActiveStatus.activeMatch
import com.zeroboss.scoring500.domain.model.Match
import com.zeroboss.scoring500.domain.use_cases.ScoringUseCases

class HomeViewModel(
    private val scoringUseCases: ScoringUseCases
) : ViewModel() {

    private val _homeState = mutableStateOf(HomeState())
    val homeState = _homeState

    private val _menuState = mutableStateOf(DropDownMenuState())
    val menuState = _menuState

    init {
        val matches = getMatches()
        _homeState.value = homeState.value.copy(
            matches = matches,
            expandedCard = activeMatch
        )
    }

    private fun getMatches() : List<Match> {
        val matches = scoringUseCases.getMatches()
        activeMatch = if (matches.size == 1) matches.first() else null

        return matches
    }

    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.MatchItemClicked -> {
                onCardArrowClicked(event.match)
            }

            is HomeEvent.CardArrowClicked -> {
                onCardArrowClicked(event.match)
            }

            is HomeEvent.DeleteMatch -> {
                scoringUseCases.deleteMatch(activeMatch!!)
                val matches = getMatches()
                _homeState.value = homeState.value.copy(
                    showDeleteMatch = false,
                    matches = matches,
                    expandedCard = activeMatch
                )
            }

            is HomeEvent.CreateNewGame -> {
                activeMatch = event.match
                activeGame = scoringUseCases.createGame(activeMatch!!)
            }

            is HomeEvent.DeleteGame -> {
                scoringUseCases.deleteGame(activeMatch!!, activeGame!!)
                activeGame = null
            }

            is HomeEvent.ShowDeleteMatchDialog -> {
                _homeState.value = homeState.value.copy(showDeleteMatch = event.show)
            }

            is HomeEvent.ShowDeleteGameDialog -> {
                _homeState.value = homeState.value.copy(showDeleteGame = event.show)
            }

            is HomeEvent.SaveScoringRules -> {
                scoringUseCases.saveScoringRules(event.nonBiddingPointsType, event.isTenTrickBonus)
            }
        }
    }

    fun onMenuEvent(event: MenuEvent) {
        if (event != MenuEvent.MenuOpen(true)) {
            _menuState.value.expanded = false
        }

        when (event) {
            is MenuEvent.MenuOpen -> _menuState.value = menuState.value.copy(expanded = event.open)

            is MenuEvent.ShowScoringRules -> _menuState.value = menuState.value.copy(
                showScoringRules = event.show
            )

            is MenuEvent.ShowBackupDialog -> _menuState.value = menuState.value.copy(
                showBackupDialog = event.show
            )

            is MenuEvent.ShowRestoreDataDialog -> _menuState.value = menuState.value.copy(
                showRestoreDataDialog = event.show
            )

            is MenuEvent.ShowClearBackupsDialog -> _menuState.value = menuState.value.copy(
                showClearBackupsDialog = event.show
            )

            is MenuEvent.ShowClearDataDialog -> _menuState.value = menuState.value.copy(
                showClearDataDialog = event.show)

            is MenuEvent.ShowAboutDialog -> _menuState.value = menuState.value.copy(
                showAbout = event.show
            )
        }
    }

    private fun onCardArrowClicked(match: Match) {
        _homeState.value = homeState.value.copy(
            expandedCard = if (_homeState.value.expandedCard == match) null else match
        )
    }
}