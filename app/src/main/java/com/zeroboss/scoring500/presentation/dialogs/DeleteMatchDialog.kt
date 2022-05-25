package com.zeroboss.scoring500.presentation.dialogs

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.zeroboss.scoring500.R
import com.zeroboss.scoring500.data.common.ActiveStatus.activeMatch
import com.zeroboss.scoring500.domain.model.Match
import com.zeroboss.scoring500.presentation.common.ButtonData
import com.zeroboss.scoring500.presentation.common.MultipleButtonBar
import com.zeroboss.scoring500.presentation.common.ShowDialogText
import com.zeroboss.scoring500.presentation.screens.home.HomeEvent
import com.zeroboss.scoring500.presentation.screens.home.HomeViewModel
import com.zeroboss.scoring500.presentation.screens.home.MenuEvent
import com.zeroboss.scoring500.ui.theme.textTitleStyle
import com.zeroboss.scoring500.ui.theme.warningTitle

@Composable
fun DeleteMatchDialog(
    homeViewModel: HomeViewModel
) {
    val state by homeViewModel.homeState

    if (state.showDeleteMatch) {
        Dialog(
            onDismissRequest = { closeDialog(homeViewModel) }
        ) {
            Card(
                modifier = Modifier.size(390.dp, 340.dp),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row {
                        Image(
                            modifier = Modifier
                                .fillMaxWidth(fraction = .35F),
                            painter = painterResource(R.drawable.player),
                            contentDescription = ""
                        )

                        ShowDialogText(
                            style = warningTitle,
                            topPadding = 0.dp,
                            text = R.string.delete_match
                        )
                    }

                    ShowDialogText(
                        style = textTitleStyle,
                        text = R.string.warning_deleting_match
                    )

                    ShowDialogText(
                        style = textTitleStyle,
                        text = R.string.are_you_sure
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    MultipleButtonBar(
                        buttonData = listOf(
                            ButtonData(
                                "Yes",
                                onClicked = {
                                    homeViewModel.onEvent(HomeEvent.DeleteMatch(activeMatch))
                                    closeDialog(homeViewModel)
                                }
                            ),

                            ButtonData(
                                " No ",
                                onClicked = {
                                    closeDialog(homeViewModel)
                                },
                            ),
                        )
                    )
                }
            }
        }
    }
}

private fun closeDialog(
    homeViewModel: HomeViewModel
) {
    homeViewModel.onEvent(HomeEvent.ShowDeleteMatchDialog(false))
}