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
import com.zeroboss.scoring500.R
import com.zeroboss.scoring500.R.*
import com.zeroboss.scoring500.ui.common.*
import com.zeroboss.scoring500.ui.common.menu.DialogText
import com.zeroboss.scoring500.ui.common.menu.DialogTextId
import com.zeroboss.scoring500.ui.theme.*
import com.zeroboss.scoring500.data.common.DATE_PATTERN
import com.zeroboss.scoring500.domain.use_cases.ScoringUseCases
import com.zeroboss.scoring500.presentation.common.MultipleButtonBar
import com.zeroboss.scoring500.presentation.common.getYesNoButtons
import com.zeroboss.scoring500.presentation.screens.home.HomeViewModel
import com.zeroboss.scoring500.presentation.screens.home.MenuEvent
import com.zeroboss.scoring500.ui.theme.smallerText
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun BackupDataDialog(
    showBackupDialog: Boolean,
    homeViewModel: HomeViewModel,
    scoringUseCases: ScoringUseCases
) {
    val context = LocalContext.current

    fun closeDialog() {
        homeViewModel.onMenuEvent(MenuEvent.ShowBackupDialog(false))
    }

    if (showBackupDialog) {
        val name = LocalDateTime.now()
            .format(DateTimeFormatter.ofPattern(DATE_PATTERN))

        Dialog(
            onDismissRequest = { closeDialog() }
        ) {
            Card(
                modifier = Modifier
                    .width(390.dp)
                    .height(250.dp),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 20.dp, end = 10.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row {
                        Image(
                            modifier = Modifier
                                .padding(top = 40.dp)
                                .fillMaxSize(fraction = .35f),
                            painter = painterResource(drawable.player),
                            contentDescription = ""
                        )

                        Column(
                            modifier = Modifier.fillMaxSize(fraction = 1f)
                        ) {
                            DialogTextId(string.backup_tag)
                            DialogText(text = name, style = smallerText)
                            DialogTextId(R.string.are_you_sure)

                            MultipleButtonBar(
                                modifier = Modifier.padding(top = 20.dp),
                                buttonData = getYesNoButtons(
                                    onYesClicked = {
                                        scoringUseCases.backupData(context, name)
                                        closeDialog()
                                    },

                                    onNoClicked = { closeDialog() }
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}


