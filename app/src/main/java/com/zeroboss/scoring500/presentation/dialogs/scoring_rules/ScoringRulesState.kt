package com.zeroboss.scoring500.presentation.dialogs.scoring_rules

import com.zeroboss.scoring500.data.common.NonBiddingPointsType

data class ScoringRulesState(
    val nonBiddingPointsType: NonBiddingPointsType = NonBiddingPointsType.ONLY_IF_LOSS,
    val isTenTricksBonus: Boolean = true,
    val dataChanged: Boolean = false
)
