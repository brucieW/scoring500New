package com.zeroboss.scoring500app.presentation.screens.new_match

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zeroboss.scoring500.data.common.ActiveStatus.activeGame
import com.zeroboss.scoring500.data.common.ActiveStatus.activeMatch
import com.zeroboss.scoring500.domain.use_cases.ScoringUseCases
import com.zeroboss.scoring500.presentation.screens.new_match.NewMatchEvent

class NewMatchViewModel(
    private val scoringUseCases: ScoringUseCases
) : ViewModel() {

    private val _state = mutableStateOf(NewMatchState())
    val state = _state

    private val _playerNames = createNames(4)

    private fun createNames(count: Int): List<MutableLiveData<String>> {
        val list = mutableListOf<MutableLiveData<String>>()

        for (i in 1..count) {
            list.add(MutableLiveData<String>(""))
        }

        return list
    }

    fun getFilteredTeamNames(): List<String> {
        val excludeTeamNames = mutableListOf<String>()

        for (i in 0..3 step 2) {
            val player1 = _playerNames[i].value
            val player2 = _playerNames[i + 1].value

            if (player1!!.isNotEmpty() && player2!!.isNotEmpty()) {
                excludeTeamNames.add("$player1/$player2")
            }
        }

        return getFilteredTeamNames(excludeTeamNames)
    }

    private fun getFilteredTeamNames(
        exclude: List<String>
    ): List<String> {
        return scoringUseCases.getTeams()
            .map { team -> team.name }.filter { name -> !exclude.contains(name) }
    }

    fun getFilteredPlayerNames(): List<String> {
        val exclude = _playerNames.filter { it.value!!.isNotEmpty() }.map { it.value }

        return getFilteredPlayerNames(exclude)
    }

    private fun getFilteredPlayerNames(
        exclude: List<String?>
    ): List<String> {
        return scoringUseCases.getPlayers()
            .map { player -> player!!.name }.filter { name -> !exclude.contains(name) }
    }

    fun getOffset(
        teamId: Int,
        playerId: Int
    ): Int {
        when (teamId) {
            -1 -> return playerId
            1 -> return (if (playerId == 1) 0 else 1)
            2 -> return (if (playerId == 1) 2 else 3)
            else -> return -1
        }
    }

    fun onEvent(
        event: NewMatchEvent
    ) {
        when (event) {
            is NewMatchEvent.ThreePlayerChanged -> {
                _state.value = state.value.copy(
                    isThreePlayer = !_state.value.isThreePlayer
                )
            }

            is NewMatchEvent.ThreePlayerNameChanged -> {
                _playerNames[event.playerId - 1].value = event.text
            }

            is NewMatchEvent.PlayerNameChanged -> {
                val offset = getOffset(event.teamId, event.playerId)
                _playerNames[offset].value = event.text

                // Check data is valid.

                var valid = true
                var unique = true

                val itemsWithText = _playerNames.filter { it.value!!.isNotEmpty() }.map { it.value }

                if (itemsWithText.size < 4) {
                    valid = false
                }

                val distinct = itemsWithText.distinctBy { it!!.uppercase() }

                if (distinct.size != itemsWithText.size) {
                    unique = false
                }

                _state.value = state.value.copy(
                    isDataValid = valid,
                    isUniquePlayerNames = unique
                )
            }

            is NewMatchEvent.SaveMatch -> {
                val match = scoringUseCases.createMatch(
                    players = _playerNames.map { it.value!! }
                )

                activeGame = scoringUseCases.createGame(match)
                activeMatch = match
            }

            is NewMatchEvent.TeamDropDownClicked -> {
                val teamListVisible = mutableListOf(false, false)
                teamListVisible[event.teamId - 1] = event.visible
                _state.value = state.value.copy(
                    isTeamListVisible = teamListVisible
                )
            }

            is NewMatchEvent.PlayerDropDownClicked -> {
                val playerListVisible = mutableListOf(false, false, false, false)
                val offset = getOffset(event.teamId, event.playerId)
                playerListVisible[offset] = event.visible
                _state.value = state.value.copy(
                    isPlayerListVisible = playerListVisible
                )
            }

            is NewMatchEvent.ClearAllTeamLists -> {
                val teamsVisible = mutableListOf(false, false)
                _state.value = state.value.copy(
                    isTeamListVisible = teamsVisible
                )
            }

            is NewMatchEvent.ClearAllPlayerLists -> {
                val playersVisible = mutableListOf(false, false, false, false)
                _state.value = state.value.copy(
                    isPlayerListVisible = playersVisible
                )
            }

            is NewMatchEvent.SetPlayerNames -> {
                val offset = if (event.teamId == 1) 0 else 2

                event.playerNames.forEachIndexed { i, name ->
                    _playerNames[offset + i].value = name
                }
            }

            is NewMatchEvent.ClearAllThreePlayerLists -> TODO()
            is NewMatchEvent.ThreePlayerDropDownClicked -> TODO()
        }
    }

    fun isThreePlayerListVisible(
        playerId: Int
    ): Boolean {
        return _state.value.isPlayerListVisible[playerId - 1]
    }

    fun isPlayerListVisible(
        teamId: Int,
        playerId: Int
    ): Boolean {
        return _state.value.isPlayerListVisible[getOffset(teamId, playerId)]
    }

    fun getPlayerNameWithOffset(
        offset: Int
    ): MutableLiveData<String> {
        return _playerNames[offset]
    }

    private fun getPlayerName(
        teamId: Int,
        playerId: Int
    ): String? {
        return _playerNames[getOffset(teamId, playerId)].value
    }
}
