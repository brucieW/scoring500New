package com.zeroboss.scoring500.presentation.dialogs

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.zeroboss.scoring500.ui.common.menu.DialogText
import com.zeroboss.scoring500.ui.common.menu.DialogTextId
import com.zeroboss.scoring500.ui.theme.darkGreen
import com.zeroboss.scoring500.R
import com.zeroboss.scoring500.domain.use_cases.ScoringUseCases
import com.zeroboss.scoring500.presentation.common.MultipleButtonBar
import com.zeroboss.scoring500.presentation.common.getTwoButtons
import com.zeroboss.scoring500.presentation.screens.home.HomeViewModel
import com.zeroboss.scoring500.presentation.screens.home.MenuEvent
import com.zeroboss.scoring500.ui.theme.smallerText

@Composable
fun RestoreDataDialog(
    showRestoreDialog: Boolean,
    homeViewModel: HomeViewModel,
    scoringUseCases: ScoringUseCases
) {
    if (showRestoreDialog) {
        val context = LocalContext.current
        val items = scoringUseCases.getBackupFiles(context)

        val listState = rememberLazyListState()
        var selectedIndex by remember { mutableStateOf(if (items.size == 1) 0 else -1) }

        Dialog(
            onDismissRequest = { homeViewModel.onMenuEvent(MenuEvent.ShowRestoreDataDialog(false)) }
        ) {
            Card(
                modifier = Modifier.size(390.dp, 360.dp),
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
                            painter = painterResource(R.drawable.player),
                            contentDescription = ""
                        )

                        Column(
                            modifier = Modifier.fillMaxWidth(fraction = 1f)
                        ) {
                            DialogTextId(
                                R.string.select_backup,
                                textAlign = TextAlign.Left
                            )

                            LazyColumn(
                                state = listState,
                                modifier = Modifier
                                    .padding(start = 20.dp, top = 10.dp)
                                    .height(180.dp),

                            ) {
                                items(items) { item ->
                                    Text(
                                        text = item.date,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .selectable(
                                                selected = item.index == selectedIndex,
                                                onClick = {
                                                    if (selectedIndex != item.index) {
                                                        selectedIndex = item.index
                                                    } else {
                                                        selectedIndex = -1
                                                    }
                                                }
                                            ),
                                        color = if (selectedIndex == item.index) darkGreen else Color.Black,
                                        fontWeight = if (selectedIndex == item.index) FontWeight.Bold else FontWeight.Normal
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    MultipleButtonBar(
                        buttonData = getTwoButtons(
                            firstButtonText = "Restore",
                            secondButtonText = "Cancel",
                            firstButtonEnabled = selectedIndex != -1,
                            onFirstButtonClicked = {
                                scoringUseCases.restoreBackup(context, items[selectedIndex].date)
                            },
                            onSecondButtonClicked = { homeViewModel.onMenuEvent(MenuEvent.ShowRestoreDataDialog(false)) },
                        )
                    )

                    DialogText(
                        text = "Note: This will restart the program",
                        style = smallerText
                    )
                }
            }
        }
    }
}





