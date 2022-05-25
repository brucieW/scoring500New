package com.zeroboss.scoring500.domain.use_cases

import android.content.Context
import com.zeroboss.scoring500.data.common.BackupItem
import com.zeroboss.scoring500.domain.repository.ScoringRepository

class GetBackupFiles(
    private val scoringRepository: ScoringRepository
) {
    operator fun invoke(
        context: Context
    ) : List<BackupItem> {
        return scoringRepository.getBackupFiles(context)
    }
}