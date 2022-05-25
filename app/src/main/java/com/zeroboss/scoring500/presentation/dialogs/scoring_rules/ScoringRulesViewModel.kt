package com.zeroboss.scoring500.presentation.dialogs.scoring_rules

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.*
import com.zeroboss.scoring500.data.common.ActiveStatus
import com.zeroboss.scoring500.domain.use_cases.ScoringUseCases

class ScoringRulesViewModel(
    private val useCases: ScoringUseCases
) : ViewModel() {

    private val _state = mutableStateOf(ScoringRulesState())
    val state = _state

    init {
        _state.value = state.value.copy(
            nonBiddingPointsType = ActiveStatus.nonBiddingPointsType,
            isTenTricksBonus = ActiveStatus.isTenTricksBonus
        )
    }

    fun onEvent(event: ScoringRulesEvent) {
        when (event) {
            is ScoringRulesEvent.ChangeNonBiddingPointsType -> {
                _state.value = state.value.copy(nonBiddingPointsType = event.nonBiddingPointsType)
                checkDataChanged()
            }

            is ScoringRulesEvent.ChangeTenTricksBonus -> {
                _state.value = state.value.copy(isTenTricksBonus = !_state.value.isTenTricksBonus)
                checkDataChanged()
            }

            is ScoringRulesEvent.SaveScoringRules -> {
                useCases.saveScoringRules(
                    nonBiddingPointsType = event.nonBiddingPointsType,
                    isTenTrickBonus = event.tenTricksBonus
                )

                ActiveStatus.nonBiddingPointsType = event.nonBiddingPointsType
                ActiveStatus.isTenTricksBonus = event.tenTricksBonus
            }
        }
    }

    private fun checkDataChanged() {
        val changed = state.value.nonBiddingPointsType != ActiveStatus.nonBiddingPointsType ||
                            state.value.isTenTricksBonus != ActiveStatus.isTenTricksBonus

        if (changed != state.value.dataChanged) {
            _state.value = state.value.copy(dataChanged = changed)
        }
    }
}