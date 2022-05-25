package com.zeroboss.scoring500.domain.use_cases

import com.zeroboss.scoring500.domain.model.Match
import com.zeroboss.scoring500.domain.repository.ScoringRepository

class GetFilteredTeamNames(
    private val scoringRepository: ScoringRepository
) {

    operator fun invoke() : List<Match> {
        return scoringRepository.getMatches()
    }
}