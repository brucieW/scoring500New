package com.zeroboss.scoring500.data.repository

import android.content.Context
import android.service.notification.NotificationListenerService
import android.widget.Toast
import com.zeroboss.scoring500.data.common.ActiveStatus.activeGame
import com.zeroboss.scoring500.data.common.ActiveStatus.activeMatch
import com.zeroboss.scoring500.data.common.ActiveStatus.activePlayer
import com.zeroboss.scoring500.data.common.ActiveStatus.isTenTricksBonus
import com.zeroboss.scoring500.data.common.ActiveStatus.nonBiddingPointsType
import com.zeroboss.scoring500.data.common.BackupItem
import com.zeroboss.scoring500.data.common.DATE_PATTERN
import com.zeroboss.scoring500.data.common.NonBiddingPointsType
import com.zeroboss.scoring500.di.liveFile
import com.zeroboss.scoring500.domain.model.*
import com.zeroboss.scoring500.domain.repository.ScoringRepository
import com.zeroboss.scoring500.presentation.common.RestartApp
import com.zeroboss.scoring500.presentation.screens.statistics.Ranking
import com.zeroboss.scoring500.presentation.common.Trump
import com.zeroboss.scoring500.presentation.screens.statistics.Rankings
import io.objectbox.BoxStore
import io.objectbox.kotlin.boxFor
import io.objectbox.query.Query
import io.objectbox.query.QueryBuilder
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.Comparator

class ScoringRepositoryImpl(
    private val boxStore: BoxStore
) : ScoringRepository {

    private val scoringPreferencesBox = boxStore.boxFor(ScoringRules::class)
    private val matchBox = boxStore.boxFor(Match::class)
    private val gameBox = boxStore.boxFor(Game::class)
    private val teamBox = boxStore.boxFor(Team::class)
    private val playerBox = boxStore.boxFor(Player::class)
    private val handBox = boxStore.boxFor(Hand::class)

    private var playerQuery: Query<Player?>? = null

    init {
        var preferences = scoringPreferencesBox.get(1)

        if (preferences == null) {
           preferences = ScoringRules()
           scoringPreferencesBox.put(preferences)
        }

        nonBiddingPointsType = preferences.nonBiddingPointsType
        isTenTricksBonus = preferences.isTenTricksBonus
    }

    override fun clearBoxStore(
        context: Context
    ) : Boolean {
        closeBoxStore()
        BoxStore.deleteAllFiles(context, "scoring500")

        return true
    }

    override fun getBackupFiles(
        context: Context
    ): List<BackupItem> {
        val items = mutableListOf<BackupItem>()

        val fileNames = mutableListOf<String>()

        val path = getFilesPath(context, "backups")
        path.walk().forEach { file ->
            if (file.isDirectory && !file.name.equals("backups")) {
                fileNames.add(file.name)
            }
        }

        fileNames.sortWith { first, second ->
            val date1 = getDate(first)
            val date2 = getDate(second)

            date2.compareTo(date1)
        }

        fileNames.forEachIndexed { index, fileName ->
            items.add(BackupItem(index, fileName))
        }

        return items
    }

    override fun backupData(
        context: Context,
        name: String
    ) : Boolean {
        return try {
            val path = getFilesDir(context)
            val source = "$path/$liveFile/data.mdb"
            val targetPath = Paths.get("$path/backups/$name/")
            Files.createDirectories(targetPath)
            val target = "$targetPath/data.mdb"
            File(source).copyTo(File(target))
            Toast.makeText(
                context,
                "Backup Saved",
                Toast.LENGTH_LONG
            ).show()
            true
        } catch (ex: IOException) {
            ex.printStackTrace()
            Toast.makeText(
                context,
                "Error: ${ex.message}",
                Toast.LENGTH_LONG
            ).show()
            false
        }
    }

    private fun getFilesDir(
        context: Context
    ) : String {
        return context.getFilesDir().absolutePath + "/objectbox"
    }

    override fun restoreBackup(
        context: Context,
        name: String
    ): Boolean {
        closeBoxStore()
        return try {
            val path = getFilesDir(context)
            val source = "$path/backups/$name/data.mdb"
            val target = "$path/$liveFile/data.mdb"
            File(target).delete()
            File(source).copyTo(File(target))
            RestartApp.restart(context)
            true
        } catch (ex: IOException) {
            ex.printStackTrace()
            Toast.makeText(
                context,
                "Error: ${ex.message}",
                Toast.LENGTH_LONG
            ).show()
            false
        }
    }

    private fun getFilesPath(
        context: Context,
        destinationPath: String,
        fileName: String? = null
    ) : File {
        var path = context.getFilesDir().absolutePath + "/objectbox/$destinationPath"

        if (fileName != null) {
            path += "/$fileName"
        }

        return File(path)
    }

    override fun closeBoxStore() {
        boxStore.close()
    }

    override fun deletePlayer(
        player: Player
    ) {
        // Remove teams that contain this player
        val teams = teamBox.query().contains(Team_.name, player.name, QueryBuilder.StringOrder.CASE_INSENSITIVE).build().find()
        teams.forEach {
            deleteTeam(it)
        }

        playerBox.remove(player)
    }

    override fun getPlayerFromName(
        playerName: String
    ): Player? {
        return playerBox.query()
            .equal(Player_.name, playerName, QueryBuilder.StringOrder.CASE_INSENSITIVE).build().findFirst()
    }

    override fun createTeam(
        players: List<String>,
        match: Match
    ) : Team {
        var team = Team()
        team.name = buildTeamName(players)
        team.players.add(getPlayer(players[0]))
        team.players.add(getPlayer(players[1]))
        team.matches.add(match)

        val currentTeam = getTeamFromPlayers(players)

        if (currentTeam == null) {
            teamBox.put(team)
        } else {
            team = currentTeam
        }

        // Add team to players if not already set.
        team.players.forEach { player ->
            if (player.teams.find { it.id == team.id } == null) {
                player.teams.add(team)
                playerBox.put(player)
            }
        }

        return team
    }

    override fun getTeamFromPlayers(players: List<String>): Team? {
        return teamBox.query()
            .equal(Team_.name, buildTeamName(players), QueryBuilder.StringOrder.CASE_INSENSITIVE).build().findFirst()
    }

    override fun addTeam(
        teamRankings: MutableList<Ranking>,
        name: String,
        wins: Int,
        losses: Int
    ) {
        val team = teamRankings.find { ranking -> ranking.name == name }

        if (team == null) {
            teamRankings.add(Ranking(name, wins, losses))
        } else {
            team.wins += wins
            team.losses += losses
        }
    }

    override fun deleteTeam(team: Team) {
        team.matches.toList().forEach { match ->
            match.players.forEach { player ->
                player.teams.remove(team)
            }

            match.games.toList().forEach { game ->
                deleteGame(match, game)
            }

            matchBox.remove(match)
        }

        teamBox.remove(team)
    }

    override fun getTeams(): List<Team> {
        return teamBox.all
    }

    private fun buildTeamName(
        names: List<String>
    ): String {
        return names[0] + "/" + names[1]
    }

    override fun createMatch(
        players: List<String>,
        lastPlayed: LocalDateTime
    ) : Match {
        // Capitalize names.
        val capitalNames = players.map { name -> name.capitalizeWords() }
        val match = Match()

        // Find matching teams (if any)
        val team1Name = buildTeamName(capitalNames)
        val team2Name = buildTeamName(capitalNames.subList(2, 4))

        matchBox.all.forEach { nextMatch ->
            if (nextMatch.teams[0].name == team1Name &&
                nextMatch.teams[1].name == team2Name
            ) {
                return nextMatch
            }
        }

        match.teams.add(createTeam(capitalNames, match))
        match.teams.add(createTeam(capitalNames.subList(2, 4), match))
        match.lastPlayed = lastPlayed

        matchBox.put(match)
        activeMatch = match

        return match
    }

    private fun String.capitalizeWords(): String =
        split(" ").joinToString(" ") {
            it.replaceFirstChar { c ->
                if (c.isLowerCase()) c.titlecase(Locale.getDefault()) else c.toString()
            }
        }

    override fun getMatches(): MutableList<Match> {
        return matchBox.query().order(Match_.lastPlayed, QueryBuilder.DESCENDING).build().find()
    }

    override fun getLastMatch(): Match? {
        return matchBox.query().order(Match_.lastPlayed, QueryBuilder.DESCENDING).build()
            .findFirst()
    }

    override fun deleteMatch(match: Match) {
        matchBox.remove(match)
    }

    override fun createGame(
        match: Match
    ) : Game {
        val game = Game()
        game.match.target = match
        match.games.add(game)
        match.lastPlayed = LocalDateTime.now()
        gameBox.put(game)
        matchBox.put(match)
        activeGame = game

        return game
    }

    override fun deleteGame(match: Match, game: Game) {
        match.games.remove(game)
        matchBox.put(match)
        gameBox.remove(game)
    }

    override fun createHand(game: Game, bid: Trump): Hand {
        val hand = Hand(bid = bid)
        hand.game.target = game
        game.addHand(hand)

        gameBox.put(game)

        return hand
    }

    override fun deleteHand(hand: Hand) {
        handBox.remove(hand)
    }

    override fun deleteActivePlayer() {
        // Remove teams associated with this player
        activePlayer!!.teams.forEach { team ->
            team.players[0].teams.remove(team)
            team.players[1].teams.remove(team)
            teamBox.remove(team.id)
        }

        playerBox.remove(activePlayer!!)
        activePlayer = null
    }

    override fun deleteActiveGame() {
        activeGame!!.hands.forEach { hand ->
            handBox.remove(hand)
        }

        gameBox.remove(activeGame!!)
        activeMatch!!.games.remove(activeGame!!)
        matchBox.put(activeMatch!!)

        activeGame = null
    }

    override fun getPlayers(): List<Player?> {
        return playerQuery()!!.find()
    }

    private fun playerQuery(): Query<Player?>? {
        if (playerQuery == null) {
            playerQuery = playerBox.query().order(Player_.name).build()
        }

        return playerQuery
    }

    override fun addPlayer(
        playerRankings: MutableList<Ranking>,
        name: String,
        wins: Int,
        losses: Int
    ) {
        val player = playerRankings.find { ranking -> ranking.name == name }

        if (player == null) {
            playerRankings.add(Ranking(name, wins, losses))
        } else {
            player.wins += wins
            player.losses += losses
        }
    }

    override fun getPlayer(name: String): Player {
        val player = playerBox.query().equal(Player_.name, name, QueryBuilder.StringOrder.CASE_INSENSITIVE).build().findFirst()

        return player ?: Player(name = name)
    }

    override fun getPlayerCount(): Long {
        return playerBox.count()
    }

    override fun getFilteredTeamNames(
        exclude: List<String>
    ): List<String> {
        return getTeams()
            .map { team -> team.name }.filter { name -> !exclude.contains(name) }
    }

    override fun getFilteredPlayerNames(
        exclude: List<String>
    ): List<String> {
        return getPlayers()
            .map { player -> player!!.name }.filter { name -> !exclude.contains(name) }
    }

    override fun getTeamCount(): Long {
        return teamBox.count()
    }

    override fun getMatchCount(): Long {
        return matchBox.count()
    }

    override fun getScoringRules(): ScoringRules {
        if (scoringPreferencesBox.isEmpty) {
            scoringPreferencesBox.put(ScoringRules())
        }

        return scoringPreferencesBox.get(1)
    }

    override fun saveScoringRules(
        nonBiddingPointsType: NonBiddingPointsType,
        isTenTrickBonus: Boolean
    ): ScoringRules {
        val scoringPreferences = getScoringRules()
        scoringPreferences.nonBiddingPointsType = nonBiddingPointsType
        scoringPreferences.isTenTricksBonus = isTenTrickBonus

        scoringPreferencesBox.put(scoringPreferences)

        return scoringPreferences
    }

    override fun getRankings(): Rankings {
        val teamRankings = mutableListOf<Ranking>()
        val playerRankings = mutableListOf<Ranking>()

        getMatches().forEach { match ->
            val ratio = match.getWinLossRatio()

            if (ratio.isNotEmpty()) {
                val team1 = match.teams[0]
                val team2 = match.teams[1]

                addTeam(teamRankings, team1.name, ratio[0], ratio[1])
                addTeam(teamRankings, team2.name, ratio[1], ratio[0])

                addPlayer(playerRankings, team1.players[0].name, ratio[0], ratio[1])
                addPlayer(playerRankings, team1.players[1].name, ratio[0], ratio[1])
                addPlayer(playerRankings, team2.players[0].name, ratio[1], ratio[0])
                addPlayer(playerRankings, team2.players[1].name, ratio[1], ratio[0])
            }
        }

        sortRankings(teamRankings)
        sortRankings(playerRankings)

        return Rankings(
            teamRankings,
            playerRankings
        )
    }

    private fun sortRankings(rankings: MutableList<Ranking>) {
        rankings.sortWith(CompareRankings)
        setChampion(rankings)
    }

    private fun setChampion(rankings: List<Ranking>): List<Ranking> {
        rankings.forEachIndexed { index, ranking ->
            if (index == 0) {
                ranking.champion = true
                ranking.rank = 1
            } else {
                val previousRanking = rankings[index - 1]

                if (ranking.isNoRanking()) {
                    ranking.rank = 0
                } else if (ranking.percentWins == previousRanking.percentWins) {
                    ranking.champion = previousRanking.champion
                    ranking.rank = previousRanking.rank
                } else if (ranking.wins > 0 || ranking.losses > 0) {
                    ranking.rank = previousRanking.rank + 1
                }
            }
        }

        return rankings
    }

    class CompareRankings {
        companion object : Comparator<Ranking> {
            override fun compare(ranking1: Ranking?, ranking2: Ranking?): Int {
                if (ranking1!!.isNoRanking()) {
                    return if (ranking2!!.isNoRanking()) {
                        ranking1.name.compareTo(ranking2.name)
                    } else {
                        1
                    }
                } else if (ranking2!!.isNoRanking()) {
                    return -1
                } else {
                    if (ranking1.wins > ranking2.wins) {
                        return -1
                    }

                    if (ranking1.wins < ranking2.wins) {
                        return 1
                    }

                    // Wins equal, sort according to losses.
                    if (ranking1.losses > ranking2.losses) {
                        return 1
                    }

                    if (ranking1.losses < ranking2.losses) {
                        return -1
                    }
                }

                return 0
            }
        }
    }

    private fun getDate(
        date: String
    ): LocalDateTime {
        return LocalDateTime.parse(date, DateTimeFormatter.ofPattern(DATE_PATTERN))
    }

    override fun clearBackups(
        context: Context
    ) : Boolean {
        return try {
            getFilesPath(context, "backups").deleteRecursively()
            Toast.makeText(
                context,
                "Backups Cleared",
                Toast.LENGTH_LONG
            ).show()
            true
        } catch (ex: IOException) {
            ex.printStackTrace()
            Toast.makeText(
                context,
                "Error, check log",
                Toast.LENGTH_LONG
            ).show()
            false
        }
    }

    override fun clearData(context: Context) {
        if (clearBoxStore(context)) {
            RestartApp.restart(context)
        }
    }
}

