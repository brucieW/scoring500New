package com.zeroboss.scoring500.domain.use_cases

import com.zeroboss.scoring500.domain.model.Game
import com.zeroboss.scoring500.domain.model.Match
import com.zeroboss.scoring500.domain.repository.ScoringRepository

class DeleteGame(
    private val scoringRepository: ScoringRepository
) {

    operator fun invoke(match: Match, game: Game) {
        return scoringRepository.deleteGame(match, game)
    }
}