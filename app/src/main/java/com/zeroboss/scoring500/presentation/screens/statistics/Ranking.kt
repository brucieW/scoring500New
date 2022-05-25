package com.zeroboss.scoring500.presentation.screens.statistics

data class Ranking(
    val name: String,
    var wins: Int,
    var losses: Int,
    var percentWins: Float = 0f,
    var rank: Int = 0,
    var champion: Boolean = false
) {
    init {
        percentWins = if (wins == 0 && losses == 0) 0f else (wins.toFloat() / (wins.toFloat() + losses.toFloat()) * 100f)
    }

    fun isNoRanking() : Boolean {
        return wins == 0 && losses == 0
    }

    fun percentWinsText() : String {
        return if (isNoRanking()) "" else String.format("%.0f", percentWins)
    }
}

data class Rankings(
    val teamRanking: List<Ranking>,
    val playerRanking: List<Ranking>
)

