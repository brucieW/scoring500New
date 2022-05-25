package com.zeroboss.scoring500.presentation.screens.game_screen

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.zeroboss.scoring500.ui.theme.*
import com.zeroboss.scoring500.R
import com.zeroboss.scoring500.data.common.Suit
import com.zeroboss.scoring500.presentation.common.ScoringButton
import com.zeroboss.scoring500.presentation.common.TopPanel
import com.zeroboss.scoring500.presentation.screens.destinations.HomeScreenDestination
import com.zeroboss.scoring500.presentation.screens.destinations.NewHandDestination
import com.zeroboss.scoring500.ui.theme.smallerText
import com.zeroboss.scoring500.ui.theme.textInputStyle
import com.zeroboss.scoring500.ui.theme.textTitleStyle
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import org.koin.androidx.compose.viewModel

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Destination
@Composable
fun GameScreen(
    navigator: DestinationsNavigator
) {
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopPanel(
                R.string.game,
                onClickReturn = {
                    navigator.navigate(HomeScreenDestination)
                }
            )
        },
        content = { GameBody(navigator) },
    )
}

@Composable
fun GameBody(
    navigator: DestinationsNavigator
) {
    val gameViewModel by viewModel<GameViewModel>()
    val activeGame = gameViewModel.activeGame

    val hands = gameViewModel.hands.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Blue50),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        GameTime(
            prefix = "Started",
            time = activeGame.started
        )

        if (activeGame.finished == null) {
            ScoringButton(
                text = stringResource(id = R.string.new_hand),
                smallButtons = true,
                onClick = {
                    navigator.navigate(NewHandDestination)
                },
                modifier = Modifier.padding(top = 10.dp)
            )
        }

        val activeMatch = activeGame.match.target
        val winningTeamId = activeGame.getWinningTeamId()

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            for (teamId in 0..1) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = if (teamId == 0) 10.dp else 0.dp)
                        .weight(0.5f),
                    elevation = 10.dp
                ) {
                    Column {
                        Text(
                            text = activeMatch.teams[teamId].name,
                            style = textTitleStyle,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 5.dp)
                        )

                        Line()

                        LazyColumn(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            itemsIndexed(hands.value) { _, hand ->
                                val trump = hand.bid

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(start = 10.dp, end = 10.dp)
                                        .height(30.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxSize(),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Box(
                                            modifier = Modifier.weight(1.5f)
                                        ) {
                                            Row {
                                                var text: String
                                                var style = textInputStyle
                                                var end = 0.dp
                                                var bottom = 0.dp
                                                var image: Int? = null

                                                if (teamId == trump.playerOrTeamId - 1) {
                                                    when (trump.suit) {
                                                        Suit.Misere -> {
                                                            text =
                                                                stringResource(id = R.string.misere)
                                                            style = smallerText
                                                        }

                                                        Suit.OpenMisere -> {
                                                            text =
                                                                stringResource(id = R.string.open_misere)
                                                            style = smallerText
                                                        }

                                                        else -> {
                                                            text = trump.bidCount.toString()
                                                            end = 5.dp

                                                            if (trump.suit == Suit.NoTrumps) {
                                                                text += "NT"
                                                            } else {
                                                                image = trump.suit.suit
                                                            }
                                                        }
                                                    }
                                                } else {
                                                    text = "  "

                                                    // This keeps columns inline when one is Open Misere.
                                                    if (trump.suit == Suit.OpenMisere) {
                                                        if (teamId != trump.playerOrTeamId - 1) {
                                                            bottom = 14.dp
                                                        }
                                                    } else {
                                                        bottom = 6.dp
                                                    }
                                                }

                                                Text(
                                                    text = text,
                                                    style = style,
                                                    modifier = Modifier.padding(
                                                        end = end,
                                                        bottom = bottom
                                                    )
                                                )

                                                if (image != null) {
                                                    Image(
                                                        painterResource(id = image),
                                                        contentDescription = null
                                                    )
                                                }
                                            }
                                        }

                                        Box(
                                            modifier = Modifier.weight(2f)
                                        ) {
                                            val tricks = trump.tricksWon[teamId]
                                            val plural = if (tricks != 1) "s" else ""

                                            Text(
                                                text = "$tricks trick$plural",
                                                style = textInputStyle,
                                            )
                                        }

                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(start = 5.dp)
                                                .weight(1.5f)
                                        ) {
                                            val value: Int = if (trump.playerOrTeamId == teamId + 1) {
                                                trump.getBidValue()
                                            } else {
                                                trump.getOppositionValue()
                                            }

                                            Text(
                                                text = value.toString(),
                                                style = textInputStyle,
                                                textAlign = TextAlign.Right,
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(end = 5.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        Line()

                        val handsTotal = activeGame.getAllHandsTotal()
                        val total = if (handsTotal.isEmpty()) 0 else handsTotal.last()[teamId]

                        Text(
                            buildAnnotatedString {
                                if (winningTeamId == teamId) {
                                    withStyle(
                                        style = SpanStyle(
                                            fontSize = 24.sp,
                                            fontStyle = FontStyle.Italic,
                                            fontWeight = FontWeight.Bold
                                        )
                                    ) {
                                        append("WINNER!    ")
                                    }
                                }

                                withStyle(
                                    style = SpanStyle(
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                ) {
                                    append(total.toString())
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(end = 10.dp, bottom = 5.dp),
                            textAlign = TextAlign.Right
                        )
                    }
                }
            }
        }

        if (winningTeamId != -1) {
            GameTime(
                prefix = "Finished",
                time = activeGame.finished!!,
                modifier = Modifier.padding(top = 10.dp),
            )
        }
    }
}

@Composable
fun Line() {
    Divider(
        color = Blue800,
        thickness = 2.dp,
        modifier = Modifier.padding(top = 4.dp, bottom = 5.dp)
    )
}

@Composable
fun GameTime(
    prefix: String,
    time: LocalDateTime,
    modifier: Modifier = Modifier
) {
    val text =
        time.format(
            DateTimeFormatter.ofLocalizedDateTime(
                FormatStyle.MEDIUM,
                FormatStyle.SHORT
            )
        )

    Text(
        text = "$prefix $text",
        style = textInputStyle,
        fontStyle = FontStyle.Italic,
        modifier = modifier.padding(top = 10.dp)
    )
}

