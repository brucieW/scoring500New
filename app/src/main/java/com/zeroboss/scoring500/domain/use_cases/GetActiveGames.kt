package com.zeroboss.scoring500.domain.use_cases

import com.zeroboss.scoring500.domain.model.Game
import com.zeroboss.scoring500.domain.model.Match

object ActiveGames {
    fun get(match: Match) : List<Game> {
        val openGames = match.games.filter { it.finished == null }
            .sortedByDescending { it.started }
        val finishedGames = match.games.filter { it.finished != null }
            .sortedByDescending { it.started }
        return openGames + finishedGames
    }
}