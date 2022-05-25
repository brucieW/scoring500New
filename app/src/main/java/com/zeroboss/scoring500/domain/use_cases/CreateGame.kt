package com.zeroboss.scoring500.domain.use_cases

import com.zeroboss.scoring500.domain.model.Game
import com.zeroboss.scoring500.domain.model.Match
import com.zeroboss.scoring500.domain.repository.ScoringRepository

class CreateGame(
    private val scoringRepository: ScoringRepository
) {

    operator fun invoke(
        match: Match
    ) : Game {
        return scoringRepository.createGame(match)
    }
}