package com.zeroboss.scoring500.domain.use_cases

import com.zeroboss.scoring500.domain.model.Player
import com.zeroboss.scoring500.domain.repository.ScoringRepository

class GetPlayers(
    private val scoringRepository: ScoringRepository
) {

    operator fun invoke() : List<Player?> {
        return scoringRepository.getPlayers()
    }
}