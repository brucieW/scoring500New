package com.zeroboss.scoring500.domain.use_cases

import com.zeroboss.scoring500.domain.model.Match
import com.zeroboss.scoring500.domain.repository.ScoringRepository

class DeleteMatch(
    private val scoringRepository: ScoringRepository
) {

    operator fun invoke(match: Match) {
        return scoringRepository.deleteMatch(match)
    }
}