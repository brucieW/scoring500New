package com.zeroboss.scoring500.domain.use_cases

import com.zeroboss.scoring500.data.common.NonBiddingPointsType
import com.zeroboss.scoring500.domain.model.Match
import com.zeroboss.scoring500.domain.model.ScoringRules
import com.zeroboss.scoring500.domain.repository.ScoringRepository

class SaveScoringRules(
    private val scoringRepository: ScoringRepository
) {

    operator fun invoke(
        nonBiddingPointsType: NonBiddingPointsType,
        isTenTrickBonus: Boolean
    ): ScoringRules {
        return scoringRepository.saveScoringRules(nonBiddingPointsType, isTenTrickBonus)
    }
}