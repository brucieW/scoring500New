package com.zeroboss.scoring500.domain.use_cases

import com.zeroboss.scoring500.domain.model.Match
import com.zeroboss.scoring500.domain.repository.ScoringRepository

class CreateMatch(
    private val scoringRepository: ScoringRepository
) {

    operator fun invoke(
        players: List<String>
    ) : Match {
        return scoringRepository.createMatch(players)
    }
}