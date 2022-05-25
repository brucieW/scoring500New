package com.zeroboss.scoring500.presentation.dialogs.scoring_rules

import com.zeroboss.scoring500.data.common.NonBiddingPointsType

sealed class ScoringRulesEvent {
    data class ChangeNonBiddingPointsType(val nonBiddingPointsType: NonBiddingPointsType): ScoringRulesEvent()
    object ChangeTenTricksBonus : ScoringRulesEvent()
    data class SaveScoringRules(
        val nonBiddingPointsType: NonBiddingPointsType,
        val tenTricksBonus: Boolean
    ): ScoringRulesEvent()
}
