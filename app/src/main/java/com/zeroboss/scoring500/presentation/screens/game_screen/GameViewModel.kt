package com.zeroboss.scoring500.presentation.screens.game_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zeroboss.scoring500.data.common.ActiveStatus
import com.zeroboss.scoring500.domain.model.Hand
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class GameViewModel() : ViewModel() {
    val activeGame = ActiveStatus.activeGame!!

    private val _hands = MutableStateFlow(mutableListOf<Hand>())
    val hands: StateFlow<List<Hand>> get() = _hands

    init {
        viewModelScope.launch(Dispatchers.Default) {
            buildHandsList()
        }
    }

    private suspend fun buildHandsList() {
        _hands.emit(activeGame.hands)
    }
}