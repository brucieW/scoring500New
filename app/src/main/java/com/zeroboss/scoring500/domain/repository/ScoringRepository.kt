package com.zeroboss.scoring500.domain.repository

import android.content.Context
import com.zeroboss.scoring500.data.common.BackupItem
import com.zeroboss.scoring500.data.common.NonBiddingPointsType
import com.zeroboss.scoring500.domain.model.*
import com.zeroboss.scoring500.presentation.common.Trump
import com.zeroboss.scoring500.presentation.screens.statistics.Ranking
import com.zeroboss.scoring500.presentation.screens.statistics.Rankings
import java.time.LocalDateTime

interface ScoringRepository {

    fun clearBoxStore(context: Context) : Boolean

    fun closeBoxStore()

    fun deletePlayer(player: Player)

    fun getPlayerFromName(playerName: String) : Player?

    fun createTeam(
        players: List<String>,
        match: Match
    ) : Team

    fun getTeamFromPlayers(players: List<String>): Team?

    fun addTeam(
        teamRankings: MutableList<Ranking>,
        name: String,
        wins: Int,
        losses: Int
    )

    fun deleteTeam(team: Team)

    fun getTeams() : List<Team>

    fun createMatch(
        players: List<String>,
        lastPlayed: LocalDateTime = LocalDateTime.now()
    ) : Match

    fun getMatches() : MutableList<Match>

    fun getLastMatch() : Match?

    fun deleteMatch(match: Match)

    fun createGame(match: Match) : Game

    fun deleteGame(match: Match, game: Game)

    fun createHand(
        game: Game,
        bid: Trump
    ) : Hand

    fun deleteHand(hand: Hand)

    fun deleteActivePlayer()

    fun deleteActiveGame()

    fun getPlayers() : List<Player?>

    fun addPlayer(
        playerRankings: MutableList<Ranking>,
        name: String,
        wins: Int,
        losses: Int
    )

    fun getPlayerCount() : Long

    fun getFilteredTeamNames(exclude: List<String>) : List<String>

    fun getFilteredPlayerNames(exclude: List<String>) : List<String>

    fun getTeamCount() : Long

    fun getMatchCount() : Long

    fun getPlayer(name: String) : Player?

    fun getScoringRules() : ScoringRules

    fun saveScoringRules(
        nonBiddingPointsType: NonBiddingPointsType,
        isTenTrickBonus: Boolean
    ) : ScoringRules

    fun getRankings() : Rankings

    fun backupData(
        context: Context,
        name: String
    ) : Boolean

    fun restoreBackup(
        context: Context,
        name: String
    ) : Boolean

    fun getBackupFiles(context: Context) : List<BackupItem>

    fun clearBackups(context: Context) : Boolean

    fun clearData(context: Context)
}