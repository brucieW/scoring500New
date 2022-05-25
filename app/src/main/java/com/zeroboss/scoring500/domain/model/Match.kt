package com.zeroboss.scoring500.domain.model

import com.zeroboss.scoring500.data.common.LocalDateTimeConverter
import com.zeroboss.scoring500.domain.model.Team_.players
import io.objectbox.annotation.Backlink
import io.objectbox.annotation.Convert
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.relation.ToMany
import java.time.LocalDateTime

@Entity
data class Match(
    @Id
    var id: Long = 0,

    @Convert(converter = LocalDateTimeConverter::class, dbType = Long::class)
    var lastPlayed: LocalDateTime = LocalDateTime.now()
) {
    var teams = ToMany<Team>(this, Match_.teams)

    // This is used when there is a 3 player game.
    var players = ToMany<Player>(this, Match_.players)

    @Backlink(to = "match")
    var games = ToMany<Game>(this, Match_.games)

    fun getWinLossRatioText() : String {
        val ratio = getWinLossRatio()
        var text = "0 : 0"

        if (ratio.isNotEmpty()) {
            val builder = StringBuilder()
                .append(ratio[0])
                .append(" : ")
                .append(ratio[1])

            text = builder.toString()
        }

        return text
    }

    fun getWinLossRatio() : List<Int> {
        val ratio = mutableListOf<Int>(0, 0)

        games.forEach { game ->
            if (game.finished != null) {
                ++ratio[game.getWinningTeamId()]
            }
        }

        return ratio
    }

    fun gameIsActive() : Boolean {
        return games.filter { game -> game.finished == null }.isNotEmpty()
    }
}
