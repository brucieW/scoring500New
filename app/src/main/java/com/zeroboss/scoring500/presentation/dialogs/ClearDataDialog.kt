package com.zeroboss.scoring500.presentation.dialogs

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.zeroboss.scoring500.ui.common.*
import com.zeroboss.scoring500.ui.theme.*
import com.zeroboss.scoring500.R
import com.zeroboss.scoring500.domain.use_cases.ScoringUseCases
import com.zeroboss.scoring500.presentation.common.ButtonData
import com.zeroboss.scoring500.presentation.common.MultipleButtonBar
import com.zeroboss.scoring500.presentation.common.RestartApp
import com.zeroboss.scoring500.presentation.common.ShowDialogText
import com.zeroboss.scoring500.presentation.screens.home.HomeViewModel
import com.zeroboss.scoring500.presentation.screens.home.MenuEvent
import com.zeroboss.scoring500.ui.theme.textTitleStyle
import com.zeroboss.scoring500.ui.theme.warningTitle
import kotlinx.coroutines.*

@Composable
fun ClearDataDialog(
    showClearData: Boolean = false,
    homeViewModel: HomeViewModel,
    useCases: ScoringUseCases
) {
    if (showClearData) {
        val context = LocalContext.current

        Dialog(
            onDismissRequest = { closeDialog(homeViewModel) }
        ) {
            Card(
                modifier = Modifier.size(390.dp, 300.dp),
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
                            modifier = Modifier.fillMaxWidth(fraction = .35F),
                            painter = painterResource(R.drawable.player),
                            contentDescription = ""
                        )

                        ShowDialogText(
                            style = warningTitle,
                            topPadding = 0.dp,
                            text = R.string.warning
                        )
                    }

                    ShowDialogText(
                        style = textTitleStyle,
                        text = R.string.warning_removal_text
                    )

                    MultipleButtonBar(
                        buttonData = listOf(
                            ButtonData(
                                "Yes",
                                onClicked = {
                                    runBlocking {
                                        launch {
                                            useCases.clearData(context)
                                        }
                                    }

                                    runBlocking {
                                        launch {
                                            RestartApp.restart(context)
                                        }
                                    }
                                }
                            ),

                            ButtonData(
                                " No ",
                                onClicked = { closeDialog(homeViewModel) },
                            )
                        ),

                        modifier = Modifier.padding(top = 20.dp)
                    )
                }
            }
        }

    }
}


private fun closeDialog(homeViewModel: HomeViewModel) {
    homeViewModel.onMenuEvent(MenuEvent.ShowClearDataDialog(false))
}
