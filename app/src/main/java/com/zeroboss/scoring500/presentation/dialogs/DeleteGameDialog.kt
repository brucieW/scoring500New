package com.zeroboss.scoring500.presentation.dialogs

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.zeroboss.scoring500.ui.common.*
import com.zeroboss.scoring500.ui.theme.*
import com.zeroboss.scoring500.R
import com.zeroboss.scoring500.data.common.ActiveStatus
import com.zeroboss.scoring500.data.common.ActiveStatus.activeGame
import com.zeroboss.scoring500.domain.model.Game
import com.zeroboss.scoring500.presentation.common.MultipleButtonBar
import com.zeroboss.scoring500.presentation.common.ShowDialogText
import com.zeroboss.scoring500.presentation.common.getYesNoButtons
import com.zeroboss.scoring500.presentation.screens.home.HomeEvent
import com.zeroboss.scoring500.presentation.screens.home.HomeViewModel
import com.zeroboss.scoring500.ui.theme.textTitleStyle

@Composable
fun DeleteGameDialog(
    homeViewModel: HomeViewModel
) {
    if (homeViewModel.homeState.value.showDeleteGame) {
        Dialog(
            onDismissRequest = { closeDialog(homeViewModel) }
        ) {
            Card(
                modifier = Modifier
                    .width(390.dp)
                    .height(300.dp),
                elevation = 10.dp
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        Modifier
                            .fillMaxSize(fraction = .35f)
                            .padding(start = 20.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            modifier = Modifier
                                .fillMaxSize(),

                            painter = painterResource(R.drawable.player),
                            contentDescription = ""
                        )
                    }

                    Column(
                        Modifier
                            .padding(start = 10.dp, end = 20.dp)
                            .fillMaxSize(fraction = 1f),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        ShowDialogText(
                            style = textTitleStyle,
                            text = R.string.warning_deleting_game
                        )

                        ShowDialogText(
                            style = textTitleStyle,
                            text = R.string.are_you_sure
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        MultipleButtonBar(
                            buttonData = getYesNoButtons(
                                onYesClicked = {
                                    homeViewModel.onEvent(HomeEvent.DeleteGame)
                                    closeDialog(homeViewModel)
                                },

                                onNoClicked = { closeDialog(homeViewModel) },
                            )
                        )
                    }
                }
            }
        }
    }
}

private fun closeDialog(
    homeViewModel: HomeViewModel
) {
    homeViewModel.onEvent(HomeEvent.ShowDeleteGameDialog(false))
}
