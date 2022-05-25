package com.zeroboss.scoring500.domain.use_cases

import android.content.Context
import com.zeroboss.scoring500.domain.repository.ScoringRepository

class ClearData(
    private val scoringRepository: ScoringRepository
) {
    operator fun invoke(context: Context) : Boolean {
        return scoringRepository.clearBoxStore(context)
    }
}