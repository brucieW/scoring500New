package com.zeroboss.scoring500.domain.use_cases

import com.zeroboss.scoring500.domain.repository.ScoringRepository

class GetFilteredPlayerNames(
    private val scoringRepository: ScoringRepository
) {

    operator fun invoke(exclusions: List<String>) : List<String> {
        return scoringRepository.getFilteredPlayerNames(exclusions)
    }
}