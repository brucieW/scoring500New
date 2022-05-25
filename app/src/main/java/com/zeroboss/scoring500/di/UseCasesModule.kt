package com.zeroboss.scoring500.di

import com.zeroboss.scoring500.domain.repository.ScoringRepository
import com.zeroboss.scoring500.domain.use_cases.*
import org.koin.dsl.module

val useCasesModule = module {
    single { provideScoringUseCases(get()) }
}

private fun provideScoringUseCases(
    repository: ScoringRepository
) : ScoringUseCases {
    return ScoringUseCases(
        getTeams = GetTeams(repository),
        getFilteredTeamNames = GetFilteredTeamNames(repository),
        getMatches = GetMatches(repository),
        createMatch = CreateMatch(repository),
        deleteMatch = DeleteMatch(repository),
        createGame = CreateGame(repository),
        deleteGame = DeleteGame(repository),
        createHand = CreateHand(repository),
        clearData = ClearData(repository),
        getPlayers = GetPlayers(repository),
        backupData = BackupData(repository),
        getBackupFiles = GetBackupFiles(repository),
        restoreBackup = RestoreBackup(repository),
        getFilteredPlayerNames = GetFilteredPlayerNames(repository),
        saveScoringRules = SaveScoringRules(repository)
    )
}

