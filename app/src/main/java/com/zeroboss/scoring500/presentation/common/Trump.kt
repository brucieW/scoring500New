package com.zeroboss.scoring500.presentation.common

import com.zeroboss.scoring500.data.common.ActiveStatus.nonBiddingPointsType
import com.zeroboss.scoring500.data.common.NonBiddingPointsType
import com.zeroboss.scoring500.data.common.Suit
import com.zeroboss.scoring500.data.common.getSuitFromTag

data class Trump(
    val playerOrTeamId: Int = 0,
    val suit: Suit = Suit.Spades,
    val bidCount: Int = 0,
    var tricksWon: MutableList<Int> = mutableListOf(0, 0, 0),
) {
    fun swapTeams() : Trump {
        val newId = if (playerOrTeamId == 0) 1 else 0
        val oldOther = if (playerOrTeamId == 0) 1 else 0
        val newOther = if (newId == 0) 1 else 0
        val newTricksWon = mutableListOf(0, 0, 0)
        newTricksWon[newId] = tricksWon[playerOrTeamId]
        newTricksWon[newOther] = tricksWon[oldOther]

        return copy(
            playerOrTeamId = newId,
            tricksWon = newTricksWon
        )
    }

    fun getTeamScore() : MutableList<Int> {
        val scores = mutableListOf<Int>()

        if (playerOrTeamId == 1) {
            scores.add(getBidValue())
            scores.add(getOppositionValue())
        } else {
            scores.add(getOppositionValue())
            scores.add(getBidValue())
        }

        return scores
    }

    fun getTricks() : Int {
        return tricksWon[playerOrTeamId]
    }

    fun getBidValue(): Int {
        var value = suit.basePoints
        val tricksWon = tricksWon[playerOrTeamId]

        if (suit < Suit.Misere) {
            value += (bidCount - 6) * 100

            if (tricksWon < bidCount) {
                value = -value
            } else if (tricksWon == 10 && value < 250) {
                value = 250
            }
        } else if (tricksWon > 0) {
            value = -value
        }

        return value
    }

    fun getOppositionValue() : Int {
        val value = if (suit < Suit.Misere) (10 - tricksWon[playerOrTeamId]) * 10 else 0

        when (nonBiddingPointsType) {
            NonBiddingPointsType.NEVER -> return 0
            NonBiddingPointsType.ONLY_IF_LOSS -> return if (getBidValue() < 0) value else 0
            NonBiddingPointsType.ALWAYS -> return value
        }
    }

    override fun toString() : String {
        val sb = StringBuilder()
        sb.append(playerOrTeamId)
        addValue(sb, suit.suitTag)
        addValue(sb, bidCount)
        tricksWon.forEach { addValue(sb, it) }

        return sb.toString()
    }

    private fun addValue(sb: StringBuilder, value: Any) {
        sb.append(",").append(value)
    }
}

fun trumpFromString(text: String) : Trump {
    val tokens = text.split(",")
    val tricksWon = mutableListOf(tokens[3].toInt(), tokens[4].toInt())

    if (tokens.size == 6) {
        tricksWon.add(tokens[5].toInt())
    }

    return Trump(
        playerOrTeamId = tokens[0].toInt(),
        suit = getSuitFromTag(tokens[1]),
        bidCount = tokens[2].toInt(),
        tricksWon = tricksWon
    )
}


