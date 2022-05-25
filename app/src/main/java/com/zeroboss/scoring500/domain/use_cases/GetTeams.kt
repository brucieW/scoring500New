package com.zeroboss.scoring500.domain.use_cases

import com.zeroboss.scoring500.domain.model.Team
import com.zeroboss.scoring500.domain.repository.ScoringRepository

class GetTeams(
    private val scoringRepository: ScoringRepository
) {
    operator fun invoke() : List<Team> {
        return scoringRepository.getTeams()
    }
}