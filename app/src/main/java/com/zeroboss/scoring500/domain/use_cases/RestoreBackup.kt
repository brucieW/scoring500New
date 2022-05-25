package com.zeroboss.scoring500.domain.use_cases

import android.content.Context
import com.zeroboss.scoring500.domain.repository.ScoringRepository

class RestoreBackup(
    private val scoringRepository: ScoringRepository
) {
    operator fun invoke(
        context: Context,
        name: String
    ) : Boolean {
        return scoringRepository.restoreBackup(context, name)
    }
}