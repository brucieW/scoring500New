package com.zeroboss.scoring500.presentation.screens.new_hand

import android.annotation.SuppressLint
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.livedata.observeAsState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.zeroboss.scoring500.presentation.common.isSmallScreen
import com.zeroboss.scoring500.ui.theme.*
import com.zeroboss.scoring500.R
import com.zeroboss.scoring500.data.common.ActiveStatus.activeGame
import com.zeroboss.scoring500.data.common.ActiveStatus.activeMatch
import com.zeroboss.scoring500.data.common.ActiveStatus.isThreePlayerGame
import com.zeroboss.scoring500.data.common.Suit
import com.zeroboss.scoring500.domain.model.Player
import com.zeroboss.scoring500.presentation.common.*
import com.zeroboss.scoring500.presentation.screens.destinations.GameScreenDestination
import com.zeroboss.scoring500.ui.theme.navigationTitle2
import com.zeroboss.scoring500.ui.theme.normalText
import com.zeroboss.scoring500.ui.theme.textInputStyle
import com.zeroboss.scoring500.ui.theme.typography
import org.koin.androidx.compose.viewModel
import java.util.*

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Destination
@Composable
fun NewHand(
    navigator: DestinationsNavigator
) {
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopPanel(
                R.string.new_hand,
                onClickReturn = { navigator.popBackStack() }
            )
        },
        content = { NewHandBody(navigator) },
    )
}

@Composable
fun NewHandBody(
    navigator: DestinationsNavigator
) {
    val newHandViewModel by viewModel<NewHandViewModel>()

    Card(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (isThreePlayerGame) {
                Row(
                    modifier = Modifier
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    for (playerId in 0..2) {
                        PlayerHeadingCard(
                            newHandViewModel,
                            player = activeMatch!!.players[playerId],
                            playerId,
                            Modifier.weight(0.33f)
                        )
                    }
                }
            } else {
                TeamHeadingCards(newHandViewModel)
            }

            BiddingCard(navigator, newHandViewModel)
        }
    }
}

@Composable
fun TeamHeadingCards(
    newHandViewModel: NewHandViewModel
) {
    val state by newHandViewModel.state
    val scores = mutableListOf<MutableState<Int>>()
    val tricksWon = mutableListOf<MutableState<Int>>()

    for (i in 0..1) {
        scores.add(newHandViewModel.getScore(i).observeAsState(0) as MutableState<Int>)
        tricksWon.add(newHandViewModel.getTricksWon(i).observeAsState(0) as MutableState<Int>)
    }

    Row(
        modifier = Modifier.padding(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        for (teamId in 0..1) {
            val teamSelectionType = state.selectionTypes[teamId]
            val team = activeMatch!!.teams[teamId]

            Card(
                modifier = Modifier
                    .padding(10.dp)
                    .weight(0.5f)
                    .border(
                        getBorder(
                            teamSelectionType,
                            Blue50
                        )
                    )
                    .clickable {
                        newHandViewModel.onEvent(NewHandEvent.SelectTeam(team, teamId))
                    },
                elevation = 10.dp
            ) {
                Column {
                    Text(
                        text = team.name,
                        style = if (isSmallScreen()) textInputStyle else typography.h6,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(top = 10.dp, bottom = if (state.isDataValid) 0.dp else 10.dp)
                            .fillMaxWidth()
                    )

                    if (state.isDataValid) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 5.dp, bottom = 5.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = newHandViewModel.getGameScore(teamId),
                                style = normalText,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .padding(top = 5.dp, bottom = 5.dp)
                            )

                            NumberPicker(
                                modifier = Modifier.padding(start = 10.dp),
                                range = IntRange(0, 10),
                                state = tricksWon[teamId],
                                onStateChanged = {
                                    newHandViewModel.onEvent(
                                        NewHandEvent.SetTeamTricksWon(
                                            teamId,
                                            it
                                        )
                                    )
                                }
                            )
                        }
                    }
                }
            }

        }
    }
}

@Composable
fun PlayerHeadingCard(
    newHandViewModel: NewHandViewModel,
    player: Player,
    playerId: Int,
    modifier: Modifier = Modifier
) {
    val state by newHandViewModel.state
    val teamSelectionType = state.selectionTypes[playerId]
    val scores = mutableListOf<MutableState<Int>>()
    val tricksWon = mutableListOf<MutableState<Int>>()

    for (i in 0..1) {
        scores.add(newHandViewModel.getScore(i).observeAsState(0) as MutableState<Int>)
        tricksWon.add(newHandViewModel.getTricksWon(i).observeAsState(0) as MutableState<Int>)
    }


    Card(
        modifier = modifier
            .padding(10.dp)
            .border(
                getBorder(
                    teamSelectionType,
                    Blue50
                )
            )
            .clickable {
                newHandViewModel.onEvent(NewHandEvent.SelectPlayer(player, playerId))
            },
        elevation = 10.dp
    ) {
        Column {
            Text(
                text = player.name,
                style = if (isSmallScreen()) textInputStyle else typography.h6,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(top = 10.dp, bottom = if (state.isDataValid) 0.dp else 10.dp)
                    .fillMaxWidth()
            )

            if (state.isDataValid) {
                val handsTotal = activeGame!!.getAllHandsTotal()
                val gameScore = StringBuffer()

                gameScore.append(newHandViewModel.getScore(playerId).value)
                gameScore.append(" (")
                gameScore.append(if (handsTotal.isEmpty()) "0" else handsTotal.last()[playerId].toString())
                gameScore.append(")")

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 5.dp, bottom = 5.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = gameScore.toString(),
                        style = normalText,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(top = 5.dp, bottom = 5.dp)
                    )

                    NumberPicker(
                        modifier = Modifier.padding(start = 10.dp),
                        range = IntRange(0, 10),
                        state = tricksWon[playerId],
                        onStateChanged = { newHandViewModel.onEvent(NewHandEvent.SetPlayerTricksWon(playerId, it)) }
                    )
                }
            }
        }
    }
}

@Composable
fun BiddingCard(
    navigator: DestinationsNavigator,
    newHandViewModel: NewHandViewModel
) {
    val state by newHandViewModel.state

    Card(
        modifier = Modifier
            .padding(start = 20.dp, end = 20.dp),
        elevation = 10.dp,
    ) {
        Column(
            modifier = Modifier.padding(top = 10.dp)
        ) {
            for (size in 6..10) {
                Row(
                    modifier = Modifier
                        .padding(start = 5.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Suit.values().forEach { suit ->
                        if (suit < Suit.Misere) {
                            val trump = Trump(
                                suit = suit,
                                bidCount = size
                            )
                            BidIndicator(
                                trump,
                                state.selectedBid,
                                onClicked = {
                                    newHandViewModel.onEvent(NewHandEvent.SelectedBid(trump))
                                }
                            )
                        }
                    }
                }
            }

            Row(
                modifier = Modifier
                    .padding(top = 10.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                MisereBid(
                    selected = (state.selectedBid != null && state.selectedBid!!.suit == Suit.Misere),
                    text = stringResource(id = R.string.misere),
                    modifier = Modifier
                        .padding(start = 30.dp)
                        .weight(0.2F),
                    style = textInputStyle,
                    onClicked = {
                        newHandViewModel.onEvent(NewHandEvent.SelectedBid(Trump(suit = Suit.Misere)))
                    }
                )

                Spacer(modifier = Modifier.width(20.dp))

                MisereBid(
                    selected = (state.selectedBid != null && state.selectedBid!!.suit == Suit.OpenMisere),
                    text = stringResource(id = R.string.open_misere),
                    style = textInputStyle,
                    modifier = Modifier
                        .padding(end = 30.dp)
                        .weight(0.2F),
                    onClicked = {
                        newHandViewModel.onEvent(NewHandEvent.SelectedBid(Trump(suit = Suit.OpenMisere)))
                    }
                )
            }

            MultipleButtonBar(
                modifier = Modifier.padding(top = 10.dp, bottom = 20.dp),
                buttonData = getTwoButtons(
                    firstButtonEnabled = state.isDataValid,
                    onFirstButtonClicked = {
                        newHandViewModel.onEvent(NewHandEvent.SaveHand)
                        navigator.navigate(GameScreenDestination)
                    },

                    onSecondButtonClicked = { navigator.popBackStack() }
                )
            )
        }
    }
}

@Composable
fun BidIndicator(
    trump: Trump = Trump(),
    selectedBid: Trump?,
    onClicked: () -> Unit
) {
    val width: Dp
    val height: Dp
    var textStyle = textInputStyle

    val selected =
        (selectedBid != null && selectedBid.suit == trump.suit && selectedBid.bidCount == trump.bidCount)

    if (isSmallScreen()) {
        width = if (trump.suit == Suit.NoTrumps) 72.dp else 68.dp
        height = 58.dp
    } else {
        width = 100.dp
        height = 90.dp
        textStyle = navigationTitle2
    }

    val elevation = if (selected) 0.dp else 10.dp

    Card(
        modifier = Modifier
            .width(width)
            .height(height)
            .padding(8.dp)
            .clickable { onClicked() },
        elevation = elevation,
        shape = RoundedCornerShape(10.dp),
        backgroundColor = Color.White,
        border = getBorder(if (selected) SelectionType.Selected else SelectionType.NotSelected)
    ) {
        Row(
            modifier = Modifier
                .padding(5.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (trump.suit < Suit.Misere) {
                Text(
                    text = trump.bidCount.toString(),
                    style = textStyle,
                    color = Color.Black
                )

                if (trump.suit == Suit.NoTrumps) {
                    Text(
                        text = "NT",
                        style = textStyle,
                        color = Color.Black
                    )
                } else {
                    Image(
                        painter = painterResource(id = trump.suit.suit),
                        contentDescription = ""
                    )
                }
            }
        }
    }
}

private fun getBidBorder(
    selected: Boolean
): BorderStroke {
    return getBorder(if (selected) SelectionType.Selected else SelectionType.NotSelected)
}

private fun getBorder(
    selectionType: SelectionType,
    defaultColor: Color = Color.White
): BorderStroke {

    val color: Color
    val width = if (selectionType == SelectionType.NotSelected) 1.dp else 4.dp

    when (selectionType) {
        SelectionType.NotSelected -> color = defaultColor
        SelectionType.Selected -> color = Blue800
        SelectionType.Loss -> color = Color.Red
        else -> {
            color = Color.Green
        }
    }

    return BorderStroke(width, color)
}

@Composable
fun MisereBid(
    selected: Boolean,
    text: String,
    style: TextStyle,
    modifier: Modifier = Modifier,
    onClicked: () -> Unit
) {
    Card(
        modifier = modifier.clickable { onClicked() },
        elevation = if (selected) 0.dp else 10.dp,
        backgroundColor = Color.White,
        shape = RoundedCornerShape(20.dp),
        border = getBidBorder(selected)
    ) {
        Text(
            text = text,
            textAlign = TextAlign.Center,
            style = style,
            color = Color.Black,
            modifier = Modifier
                .padding(8.dp)
                .background(Color.White)
        )
    }
}

@Composable
fun MisereButton(
    elevation: Dp,
    image: Int,
    click: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        onClick = { click() },
        shape = CircleShape,
        elevation = ButtonDefaults.elevation(
            defaultElevation = elevation,
            pressedElevation = 0.dp
        ),
        modifier = modifier
    ) {
        Image(
            painter = painterResource(id = image),
            contentDescription = null
        )
    }

}
