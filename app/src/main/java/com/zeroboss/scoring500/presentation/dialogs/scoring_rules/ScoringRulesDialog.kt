package com.zeroboss.scoring500.presentation.dialogs.scoring_rules

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.Image
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.zeroboss.scoring500.ui.theme.darkGreen
import com.zeroboss.scoring500.R
import com.zeroboss.scoring500.data.common.NonBiddingPointsType
import com.zeroboss.scoring500.presentation.common.CommonCheckBox
import com.zeroboss.scoring500.presentation.common.MultipleButtonBar
import com.zeroboss.scoring500.presentation.common.getTwoButtons
import com.zeroboss.scoring500.presentation.screens.home.HomeEvent
import com.zeroboss.scoring500.presentation.screens.home.HomeViewModel
import com.zeroboss.scoring500.presentation.screens.home.MenuEvent
import com.zeroboss.scoring500.ui.theme.dialogTitle
import com.zeroboss.scoring500.ui.theme.normalText
import org.koin.androidx.compose.viewModel

@Composable
fun ScoringRulesDialog(
    showDialog: Boolean = false,
    homeViewModel: HomeViewModel
) {
    val scoringRulesViewModel by viewModel<ScoringRulesViewModel>()

    val state by scoringRulesViewModel.state

    if (showDialog) {
        val items = NonBiddingPointsType.values().asList()

        Dialog(
            onDismissRequest = { closeDialog(homeViewModel) }
        ) {
            Card(
                modifier = Modifier.size(300.dp, 320.dp),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(
                    Modifier
                        .fillMaxSize()
                        .padding(20.dp)
                ) {
                    Row {
                        Image(
                            painter = painterResource(R.drawable.player),
                            contentDescription = "",
                            modifier = Modifier.fillMaxWidth(fraction = .35F)
                        )

                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = stringResource(id = R.string.preferences),
                            style = dialogTitle,
                            textAlign = TextAlign.Center
                        )
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp, start = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(id = R.string.non_bidding_points),
                            style = normalText,
                            modifier = Modifier.weight(0.65f)
                        )

                        LazyColumn(
                            modifier = Modifier
                                .padding(end = 10.dp)
                                .weight(0.45f)
                        ) {
                            items(items) { currentType ->
                                val selectedItem = currentType == state.nonBiddingPointsType

                                Text(
                                    modifier = Modifier
                                        .padding(top = 5.dp)
                                        .selectable(
                                            selected = currentType == state.nonBiddingPointsType,
                                            onClick = {
                                                scoringRulesViewModel.onEvent(ScoringRulesEvent.ChangeNonBiddingPointsType(currentType))
                                            }
                                        ),
                                    text = currentType.text,
                                    color = if (selectedItem) darkGreen else Color.Black,
                                    fontWeight = if (selectedItem) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        }
                    }

                    CommonCheckBox(
                        modifier = Modifier
                            .padding(start = 10.dp),
                        text = R.string.ten_trick_bonus,
                        checked = state.isTenTricksBonus,
                        checkEndPadding = 75.dp,
                        checkStateChanged = { scoringRulesViewModel.onEvent(ScoringRulesEvent.ChangeTenTricksBonus) }
                    )

                    MultipleButtonBar(
                        modifier = Modifier.padding(top = 20.dp),
                        buttonData = getTwoButtons(
                            onFirstButtonClicked = {
                                homeViewModel.onEvent(
                                    HomeEvent.SaveScoringRules(
                                        state.nonBiddingPointsType,
                                        state.isTenTricksBonus))
                                closeDialog(homeViewModel)
                            },

                            firstButtonEnabled = state.dataChanged,

                            onSecondButtonClicked = { closeDialog(homeViewModel) }
                        )
                    )
                }
            }
        }
    }
}

private fun closeDialog(homeViewModel: HomeViewModel) {
    homeViewModel.onMenuEvent(MenuEvent.ShowScoringRules(false))
}
