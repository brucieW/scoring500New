package com.zeroboss.scoring500.data.common

import com.zeroboss.scoring500.R

enum class Suit(
    val suit: Int = 0,
    val basePoints: Int = 0,
    val suitTag: String = ""
) {
    Spades(R.drawable.spades, 40, "S"),
    Clubs(R.drawable.clubs, 60, "C"),
    Diamonds(R.drawable.diamonds, 80, "D"),
    Hearts(R.drawable.hearts, 100, "H"),
    NoTrumps(basePoints = 120, suitTag = "N"),
    Misere(basePoints = 250, suitTag = "M"),
    OpenMisere(basePoints = 500, suitTag = "O");

    fun isMisere() = this == Misere || this == OpenMisere
}

fun getSuitFromTag(tag: String) : Suit {
    return Suit.values().first { it.suitTag == tag }
}

