package com.zeroboss.scoring500.data.common

import com.zeroboss.scoring500.domain.model.Game
import com.zeroboss.scoring500.domain.model.Match
import com.zeroboss.scoring500.domain.model.Player

object ActiveStatus {
    var expandedMatch: Match? = null
    var activePlayer: Player? = null
    var activeMatch: Match? = null
    var activeGame: Game? = null

    var nonBiddingPointsType: NonBiddingPointsType = NonBiddingPointsType.NEVER

    var isTenTricksBonus: Boolean = true

    var isThreePlayerGame: Boolean = false
}