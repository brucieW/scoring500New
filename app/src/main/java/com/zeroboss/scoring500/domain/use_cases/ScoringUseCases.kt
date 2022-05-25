package com.zeroboss.scoring500.domain.use_cases

data class ScoringUseCases(
    val getTeams: GetTeams,
    val getPlayers: GetPlayers,
    val createMatch: CreateMatch,
    val getMatches: GetMatches,
    val deleteMatch: DeleteMatch,
    val createGame: CreateGame,
    val deleteGame: DeleteGame,
    val createHand: CreateHand,
    val clearData: ClearData,
    val backupData: BackupData,
    val getBackupFiles: GetBackupFiles,
    val restoreBackup: RestoreBackup,
    val getFilteredTeamNames: GetFilteredTeamNames,
    val getFilteredPlayerNames: GetFilteredPlayerNames,
    val saveScoringRules: SaveScoringRules
)
