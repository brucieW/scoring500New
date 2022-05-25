package com.zeroboss.scoring500.presentation.screens.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zeroboss.scoring500.presentation.screens.statistics.Ranking
import com.zeroboss.scoring500.domain.repository.ScoringRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class StatisticsViewModel(
    scoringRepository: ScoringRepository
) : ViewModel() {
    private val rankingsList = scoringRepository.getRankings()

    private val _teamRankings = MutableStateFlow(listOf<Ranking>())
    val teamRankings: StateFlow<List<Ranking>> get() = _teamRankings

    private val _playerRankings = MutableStateFlow(listOf<Ranking>())
    val playerRankings: StateFlow<List<Ranking>> get() = _playerRankings

    init {
        viewModelScope.launch(Dispatchers.Default) {
            buildLists()
        }
    }

    private suspend fun buildLists() {
        _teamRankings.emit(rankingsList.teamRanking)
        _playerRankings.emit(rankingsList.playerRanking)
    }
}