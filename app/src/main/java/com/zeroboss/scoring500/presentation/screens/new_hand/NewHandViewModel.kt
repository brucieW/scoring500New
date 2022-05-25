package com.zeroboss.scoring500.presentation.screens.new_hand

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zeroboss.scoring500.data.common.ActiveStatus.activeGame
import com.zeroboss.scoring500.data.common.Suit
import com.zeroboss.scoring500.domain.use_cases.ScoringUseCases
import com.zeroboss.scoring500.presentation.common.Trump

enum class SelectionType(val text: String) {
    NotSelected("Not Selected"),
    Selected("Selected"),
    Won("Won"),
    Loss("Loss")
}

class NewHandViewModel(
    private val useCases: ScoringUseCases
) : ViewModel() {
    private val _state = mutableStateOf(NewHandState())
    val state = _state

    private val _scores = mutableListOf(
        MutableLiveData(0),
        MutableLiveData(0),
        MutableLiveData(0)
    )

    private val _tricksWon = mutableListOf(
        MutableLiveData(0),
        MutableLiveData(0),
        MutableLiveData(0)
    )

    fun onEvent(event: NewHandEvent) {
        when (event) {
            is NewHandEvent.SelectTeam -> {
                val types = mutableListOf(SelectionType.NotSelected, SelectionType.NotSelected)
                types[event.teamId] = SelectionType.Selected
                val selectedBid = _state.value.selectedBid

                if (selectedBid != null) {
                    swapDetails()
                } else {
                    _state.value = state.value.copy(
                        selectedTeam = event.team,
                        selectedOffset = event.teamId,
                        opposingTeamOffset = if (event.teamId == 0) 1 else 0,
                        selectionTypes = types,
                        selectedBid = selectedBid,
                        isDataValid = false
                    )

                    calculateTeamScores()
                }
            }

            is NewHandEvent.SelectPlayer -> {

            }

            is NewHandEvent.SelectedBid -> {
                val selectedOffset = _state.value.selectedOffset
                val opposingOffset = _state.value.opposingTeamOffset
                val types = mutableListOf(SelectionType.NotSelected, SelectionType.NotSelected)

                _tricksWon[selectedOffset].value = event.trump.bidCount
                _tricksWon[opposingOffset].value = 10 - event.trump.bidCount
                val selectedBid = event.trump.copy(
                    tricksWon = mutableListOf(_tricksWon[0].value!!, _tricksWon[1].value!!)
                )

                types[selectedOffset] = getSelectionType(selectedOffset, selectedBid)

                _state.value = state.value.copy(
                    selectionTypes = types,
                    selectedBid = selectedBid,
                    isDataValid = _state.value.selectedTeam != null
                )

                calculateTeamScores()
            }

            is NewHandEvent.SetTeamTricksWon -> {
                val selectedOffset = _state.value.selectedOffset

                if (selectedOffset == event.teamId) {
                    _tricksWon[event.teamId].value = event.tricks
                    _tricksWon[_state.value.opposingTeamOffset].value = 10 - event.tricks
                } else {
                    _tricksWon[event.teamId].value = event.tricks
                    _tricksWon[_state.value.selectedOffset].value = 10 - event.tricks
                }

                _state.value = state.value.copy(
                    selectedBid = _state.value.selectedBid!!.copy(
                        tricksWon = mutableListOf(_tricksWon[0].value!!, _tricksWon[1].value!!)
                    )
                )

                calculateTeamScores()
            }

            is NewHandEvent.SetPlayerTricksWon -> {
//                _state.value = state.value.copy(
//                    selectedBid = _state.value.selectedBid!!.copy(
//                        tricksWon = event.tricks)
//                )
            }

            is NewHandEvent.SaveHand -> {
                useCases.createHand(
                    activeGame!!,
                    _state.value.selectedBid!!
                )
            }
        }
    }

    fun getScore(
        offset: Int
    ) : MutableLiveData<Int> {
        return _scores[offset]
    }

    fun getTricksWon(
        offset: Int
    ) : MutableLiveData<Int> {
        return _tricksWon[offset]
    }

    private fun swapDetails() {
        val currentSelection = _state.value.selectedOffset
        val currentOpposing = _state.value.opposingTeamOffset
        val newSelection = if (currentSelection == 0) 1 else 0
        val newOpposing = if (newSelection == 0) 1 else 0

        val currentScores = _scores.toList()
        val currentScore = currentScores[currentSelection].value
        val oppositeScore = currentScores[currentOpposing].value
        _scores[newSelection].value = currentScore
        _scores[newOpposing].value = oppositeScore

        val currentTricks = _tricksWon.toList()
        val currentTrick = currentTricks[currentSelection].value
        val opposingTrick = currentTricks[currentOpposing].value
        _tricksWon[newSelection].value = currentTrick
        _tricksWon[newOpposing].value = opposingTrick

        val types = _state.value.selectionTypes
        val newTypes = mutableListOf(types[1], types[0])

        _state.value = state.value.copy(
            selectedOffset = newSelection,
            opposingTeamOffset = newOpposing,
            selectionTypes = newTypes,
            selectedBid = _state.value.selectedBid!!.swapTeams()
        )
    }

    private fun getSelectionType(
        selectedOffset: Int,
        selectedBid: Trump
    ) : SelectionType {
        val tricksWon = _tricksWon[selectedOffset].value!!

        return if (selectedBid.suit < Suit.Misere) {
            if (tricksWon < selectedBid.bidCount) SelectionType.Loss else SelectionType.Won
        } else {
            if (tricksWon > 0) SelectionType.Loss else SelectionType.Won
        }
    }

    fun getGameScore(
        offset: Int
    ) : String {
        val handsTotal = activeGame!!.getAllHandsTotal()
        val gameScore = StringBuffer()

        gameScore.append(getScore(offset).value)
        gameScore.append(" (")
        gameScore.append(if (handsTotal.isEmpty()) "0" else handsTotal.last()[offset].toString())
        gameScore.append(")")

        return gameScore.toString()
    }

    private fun calculateTeamScores() {
        // Check data valid
        val selectedTeam = _state.value.selectedTeam
        val selectedBid = _state.value.selectedBid

        if (selectedTeam == null || selectedBid == null) {
            return
        }

        val biddingTeamOffset = _state.value.selectedOffset
        val opposingTeamOffset = _state.value.opposingTeamOffset

        val bid = _state.value.selectedBid
        val teamTricks = bid!!.tricksWon[biddingTeamOffset]

        val bidScore: Int
        val opposingScore: Int

        when (bid.suit) {
            Suit.Misere -> {
                bidScore = if (teamTricks > 0) -250 else 250
                opposingScore = teamTricks * 10
            }

            Suit.OpenMisere -> {
                bidScore = if (teamTricks > 0) -500 else 500
                opposingScore = teamTricks * 10
            }

            else -> {
                bidScore = bid.getBidValue()
                opposingScore = ((10 - teamTricks) * 10)
            }
        }

        _scores[biddingTeamOffset].value = bidScore
        _scores[opposingTeamOffset].value = opposingScore
    }
}
