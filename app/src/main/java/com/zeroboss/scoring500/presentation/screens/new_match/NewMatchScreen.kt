package com.zeroboss.scoring500.presentation.screens.new_match

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.zeroboss.scoring500.R
import com.zeroboss.scoring500.data.common.ActiveStatus.isThreePlayerGame
import com.zeroboss.scoring500.presentation.common.*
import com.zeroboss.scoring500.ui.theme.Blue50
import com.zeroboss.scoring500.ui.theme.Blue800
import com.zeroboss.scoring500.ui.theme.normalText
import com.zeroboss.scoring500.presentation.screens.destinations.HomeScreenDestination
import com.zeroboss.scoring500.presentation.screens.destinations.NewHandDestination
import com.zeroboss.scoring500app.presentation.screens.new_match.NewMatchViewModel
import org.koin.androidx.compose.get
import java.util.*

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Destination
@Composable
fun NewMatchScreen(
    navigator: DestinationsNavigator
) {
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier.fillMaxSize(),
        backgroundColor = Blue50,
        topBar = {
            TopPanel(
                R.string.new_match,
                onClickReturn = {
                    navigator.navigate(HomeScreenDestination)
                }
            )
        },
        content = { NewMatchBody(navigator, get()) },
    )
}

@Composable
fun NewMatchBody(
    navigator: DestinationsNavigator,
    newMatchViewModel: NewMatchViewModel
) {
    val state by newMatchViewModel.state
    val names = mutableListOf<State<String>>()

    for (i in 0..3) {
        names.add(newMatchViewModel.getPlayerNameWithOffset(i).observeAsState(""))
    }

    fun threePlayerStateChanged() {
        newMatchViewModel.onEvent(NewMatchEvent.ThreePlayerChanged)
    }

    Column(
    ) {
        ActionTitle(R.string.select_teams_title)

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            CommonRadioButton(
                selected = (!state.isThreePlayer),
                text = R.string.teams,
                onClick = { threePlayerStateChanged() }
            )

            CommonRadioButton(
                selected = state.isThreePlayer,
                text = R.string.three_players,
                onClick = { threePlayerStateChanged() }
            )
        }

        if (state.isThreePlayer) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                elevation = 10.dp
            ) {
                Column {
                    Row(
                        modifier = Modifier.padding(5.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val modifier = Modifier
                            .weight(0.4f)

                        PlayerSelection(newMatchViewModel, names[0], 1, modifier)
                        PlayerSelection(newMatchViewModel, names[1], 2, modifier)
                    }

                    PlayerSelection(newMatchViewModel, names[2],3)
                }
            }
        } else {
            Row(
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                for (teamId in 1..2) {
                    Card(
                        modifier = Modifier
                            .weight(0.5F)
                            .padding(start = 10.dp, end = 10.dp),
                        elevation = 10.dp
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(10.dp),
                            verticalArrangement = Arrangement.Center
                        ) {
                            TextWithDropDown(
                                text = "Team $teamId",
                                onClickedDropDown = {
                                    newMatchViewModel.onEvent(
                                        NewMatchEvent.TeamDropDownClicked(
                                            teamId,
                                            !state.isTeamListVisible[teamId - 1]
                                        )
                                    )
                                },
                                onFocusAltered = {
                                    newMatchViewModel.onEvent(
                                        NewMatchEvent.ClearAllTeamLists
                                    )
                                }
                            )

                            if (state.isTeamListVisible[teamId - 1]) {
                                TeamSelection(
                                    newMatchViewModel,
                                    teamId
                                )
                            }

                            for (playerId in 1..2) {
                                TextFieldWithDropDown(
                                    text = names[newMatchViewModel.getOffset(
                                        teamId,
                                        playerId
                                    )].value,
                                    label = "Player $playerId",
                                    onNameChanged = { text ->
                                        newMatchViewModel.onEvent(
                                            NewMatchEvent.PlayerNameChanged(
                                                teamId,
                                                playerId,
                                                text
                                            )
                                        )
                                    },
                                    onTrailingIconClicked = {
                                        newMatchViewModel.onEvent(
                                            NewMatchEvent.PlayerDropDownClicked(
                                                teamId,
                                                playerId,
                                                true
                                            )
                                        )
                                    },
                                    onFocusAltered = {
                                        newMatchViewModel.onEvent(NewMatchEvent.ClearAllPlayerLists)
                                    }
                                )

                                if (newMatchViewModel.isPlayerListVisible(teamId, playerId)) {
                                    PlayerNamesSelection(
                                        newMatchViewModel,
                                        teamId,
                                        playerId
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        if (!state.isUniquePlayerNames) {
            Text(
                modifier = Modifier
                    .padding(top = 5.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center,
                text = stringResource(id = R.string.unique_names),
                color = Color.Red,
                style = normalText,
                fontStyle = FontStyle.Italic
            )
        }

        MultipleButtonBar(
            modifier = Modifier.padding(20.dp),
            buttonData = getTwoButtons(
                firstButtonEnabled = state.isDataValid,
                onFirstButtonClicked = {
                    newMatchViewModel.onEvent(NewMatchEvent.SaveMatch)
                    isThreePlayerGame = state.isThreePlayer
                    navigator.navigate(NewHandDestination)
                },

                onSecondButtonClicked = { navigator.popBackStack() }
            )
        )
    }
}

@Composable
fun PlayerSelection(
    newMatchViewModel: NewMatchViewModel,
    name: State<String>,
    playerId: Int,
    modifier: Modifier = Modifier.padding(5.dp)
) {
    val start: Dp
    val end: Dp
    val bottom: Dp

    if (playerId < 3) {
        start = 5.dp
        end = 5.dp
        bottom = 0.dp
    } else {
        start = 60.dp
        end = 60.dp
        bottom = 10.dp
    }

    TextFieldWithDropDown(
        modifier = modifier.padding(start = start, end = end, bottom = bottom),
        text = name.value,
        label = "Player $playerId",
        onNameChanged = { text ->
            newMatchViewModel.onEvent(
                NewMatchEvent.ThreePlayerNameChanged(
                    playerId,
                    text
                )
            )
        },
        onTrailingIconClicked = {
            newMatchViewModel.onEvent(
                NewMatchEvent.ThreePlayerDropDownClicked(
                    playerId,
                    true
                )
            )
        },
        onFocusAltered = {
            newMatchViewModel.onEvent(NewMatchEvent.ClearAllThreePlayerLists)
        }
    )

    if (newMatchViewModel.isThreePlayerListVisible(playerId)) {
        PlayerNamesSelection(
            newMatchViewModel,
            playerId = playerId
        )
    }
}

fun String.capitalizeWords(): String =
    split(" ").joinToString(" ") {
        it.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
        }
    }

@Composable
fun TeamSelection(
    newMatchViewModel: NewMatchViewModel,
    teamId: Int
) {
    Popup() {
        Box(
            modifier = Modifier
                .size(180.dp, 300.dp)
                .background(
                    Color.LightGray,
                    RoundedCornerShape(10.dp)
                )

        ) {
            Column(
            ) {
                Row(
                    modifier = Modifier
                        .padding(start = 10.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Select Team",
                        style = normalText,
                        color = Blue800
                    )

                    IconButton(
                        onClick = { closeTeamPopup(newMatchViewModel, teamId) }
                    ) {
                        Icon(
                            Icons.Rounded.Close, contentDescription = "",
                            tint = Blue800
                        )
                    }
                }

                LazyColumn(
                    modifier = Modifier
                        .background(
                            Color.LightGray,
                            RoundedCornerShape(10.dp)
                        )
                        .padding(start = 10.dp, end = 10.dp, bottom = 10.dp, top = 0.dp)
                ) {
                    items(newMatchViewModel.getFilteredTeamNames()) { name ->
                        ClickableText(
                            text = AnnotatedString(name),
                            style = normalText,
                            onClick = {
                                newMatchViewModel.onEvent(
                                    NewMatchEvent.TeamDropDownClicked(
                                        teamId,
                                        false
                                    )
                                )

                                newMatchViewModel.onEvent(
                                    NewMatchEvent.SetPlayerNames(
                                    teamId, name.split ("/"))
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}

private fun closeTeamPopup(
    newMatchViewModel: NewMatchViewModel,
    teamId: Int
) {
    newMatchViewModel.onEvent(NewMatchEvent.TeamDropDownClicked(teamId, false))
}

@Composable
fun PlayerNamesSelection(
    newMatchViewModel: NewMatchViewModel,
    teamId: Int? = null,
    playerId: Int
) {
    Popup() {
        Column(
            modifier = Modifier
                .background(
                    Color.LightGray,
                    RoundedCornerShape(10.dp)
                )
        ) {
            Row(
                modifier = Modifier.padding(start = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Select Player",
                    style = normalText,
                    color = Blue800
                )

                IconButton(
                    onClick = {
                        if (teamId == null) {
                            closeThreePlayerPopup(newMatchViewModel, playerId)
                        } else {
                            closePlayerPopup(newMatchViewModel, teamId, playerId)
                        }
                    }
                ) {
                    Icon(
                        Icons.Rounded.Close, contentDescription = "",
                        tint = Blue800
                    )
                }
            }

            LazyColumn(
                modifier = Modifier
                    .size(160.dp, 300.dp)
                    .background(
                        Color.LightGray,
                        RoundedCornerShape(10.dp)
                    )
                    .padding(start = 10.dp, end = 10.dp, bottom = 10.dp, top = 0.dp)
            ) {
                items(newMatchViewModel.getFilteredPlayerNames()) { name ->
                    ClickableText(
                        text = AnnotatedString(name),
                        style = normalText,
                        onClick = {
                            if (teamId == null) {
                                closeThreePlayerPopup(newMatchViewModel, playerId)
                                newMatchViewModel.onEvent(
                                    NewMatchEvent.ThreePlayerNameChanged(
                                        playerId,
                                        name)
                                )
                            } else {
                                closePlayerPopup(newMatchViewModel, teamId, playerId)
                                newMatchViewModel.onEvent(
                                    NewMatchEvent.PlayerNameChanged(
                                        teamId,
                                        playerId,
                                        name
                                    )
                                )
                            }
                        }
                    )
                }
            }
        }
    }
}

private fun closeThreePlayerPopup(
    newMatchViewModel: NewMatchViewModel,
    playerId: Int
) {
    newMatchViewModel.onEvent(
        NewMatchEvent.ThreePlayerDropDownClicked(
            playerId,
            false
        )
    )
}

private fun closePlayerPopup(
    newMatchViewModel: NewMatchViewModel,
    teamId: Int,
    playerId: Int
) {
    newMatchViewModel.onEvent(
        NewMatchEvent.PlayerDropDownClicked(
            teamId,
            playerId,
            false
        )
    )
}
