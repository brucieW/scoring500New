package com.zeroboss.scoring500.domain.use_cases

import com.zeroboss.scoring500.domain.model.Game
import com.zeroboss.scoring500.domain.model.Hand
import com.zeroboss.scoring500.domain.model.Hand_.game
import com.zeroboss.scoring500.domain.model.Match
import com.zeroboss.scoring500.domain.repository.ScoringRepository
import com.zeroboss.scoring500.presentation.common.Trump

class CreateHand(
    private val scoringRepository: ScoringRepository
) {

    operator fun invoke(
        game: Game,
        bid: Trump
    ) : Hand {
        return scoringRepository.createHand(game, bid)
    }
}