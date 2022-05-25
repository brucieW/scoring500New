package com.zeroboss.scoring500.data.common

enum class NonBiddingPointsType(val text: String) {
    ALWAYS ("Always"),
    ONLY_IF_LOSS ("Only If Loss"),
    NEVER ("Never");

    companion object {
        private val VALUES = values()
        fun getByValue(value: Int?) = if (value == null) ONLY_IF_LOSS else VALUES.firstOrNull() { it.ordinal == value }
    }
}

