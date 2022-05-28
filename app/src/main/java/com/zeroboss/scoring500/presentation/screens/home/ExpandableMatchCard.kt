package com.zeroboss.scoring500.presentation.screens.home

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zeroboss.scoring500.R
import com.zeroboss.scoring500.data.common.EXPAND_ANIMATION_DURATION
import com.zeroboss.scoring500.domain.model.Game
import com.zeroboss.scoring500.domain.use_cases.ActiveGames
import com.zeroboss.scoring500.presentation.common.DeleteIcon
import com.zeroboss.scoring500.presentation.common.ScoringButton
import com.zeroboss.scoring500.ui.theme.normalText
import com.zeroboss.scoring500.ui.theme.smallerText
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import com.zeroboss.scoring500.domain.model.Match

@OptIn(ExperimentalMaterialApi::class, ExperimentalAnimationApi::class)
@Composable
fun ExpandableMatchCard(
    match: Match,
    onCardArrowClicked: () -> Unit,
    onNewGameClicked: () -> Unit,
    onEditGamesClicked: () -> Unit,
    onDeleteMatchClicked: () -> Unit,
    onGameClicked: (game: Game) -> Unit,
    onDeleteGameClicked: (match: Match, game: Game) -> Unit,
    expanded: Boolean
) {
    val transitionState = remember {
        MutableTransitionState(expanded).apply {
            targetState = !expanded
        }
    }

    val transition = updateTransition(transitionState, label = "transition")

    val arrowRotationDegree by transition.animateFloat({
        tween(durationMillis = EXPAND_ANIMATION_DURATION)
    }, label = "rotationDegreeTransition") {
        if (expanded) 0F else 180f
    }

    Card(
        elevation = 10.dp,
        modifier = Modifier
            .animateContentSize()
            .fillMaxWidth()
            .clickable { onCardArrowClicked() }
            .padding(start = 1.dp, top = 1.dp, end = 10.dp, bottom = 5.dp)
    ) {
        Column(
            modifier = Modifier.padding(top = 10.dp, bottom = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CardArrow(
                    degrees = arrowRotationDegree,
                    onClick = onCardArrowClicked
                )

                Text(
                    text = match.teams[0].name,
                    style = normalText,
                    modifier = Modifier.weight(0.33F)
                )

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.weight(0.33F)
                ) {
                    Row() {
                        Text(
                            text = match.getWinLossRatioText(),
                            style = normalText,
                            modifier = Modifier.padding(end = 5.dp)
                        )

                        if (match.gameIsActive()) {
                            Image(
                                painterResource(R.drawable.star),
                                contentDescription = "",
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    Text(
                        text = match.lastPlayed.format(
                            DateTimeFormatter.ofLocalizedDate(
                                FormatStyle.MEDIUM
                            )
                        ),
                        style = smallerText,
                        fontStyle = FontStyle.Italic
                    )
                }

                Text(
                    text = match.teams[1].name,
                    style = normalText,
                    textAlign = TextAlign.Right,
                    modifier = Modifier.weight(0.33F)
                )

                DeleteIcon(
                    onClick = onDeleteMatchClicked
                )
            }

            if (expanded) {

                Row(
                    modifier = Modifier.padding(top = 5.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    ScoringButton(
                        text = stringResource(R.string.new_game),
                        onClick = onNewGameClicked,
                        smallButtons = true,
                        modifier = Modifier.padding(end = 20.dp)
                    )

                    if (match.games.isNotEmpty()) {
                        ScoringButton(
                            text = stringResource(R.string.edit_games),
                            onClick = onEditGamesClicked,
                            smallButtons = true,
                            modifier = Modifier.padding(bottom = 10.dp)
                        )
                    }
                }

                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    ActiveGames.get(match).forEach { game ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 5.dp, start = 10.dp, end = 10.dp)
                                .clickable { onGameClicked(game) },
                            elevation = 5.dp
                        ) {
                            Column {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(5.dp),
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    if (game.finished == null) {
                                        Image(
                                            painterResource(R.drawable.star),
                                            contentDescription = "",
                                            modifier = Modifier
                                                .size(25.dp)
                                                .padding(end = 5.dp)
                                        )

                                        Text(
                                            buildAnnotatedString {
                                                withStyle(style = SpanStyle(fontSize = 16.sp)) {
                                                    append(game.displayTotalScores())
                                                    append("  ")
                                                }
                                                withStyle(
                                                    style = SpanStyle(
                                                        fontSize = 12.sp,
                                                        fontStyle = FontStyle.Italic
                                                    )
                                                ) {
                                                    append(
                                                        game.started.format(
                                                            DateTimeFormatter.ofLocalizedDateTime(
                                                                FormatStyle.SHORT,
                                                                FormatStyle.SHORT
                                                            )
                                                        )
                                                    )
                                                }
                                            },

                                            textAlign = TextAlign.Center,
                                        )
                                    } else {
                                        val teamId = game.getWinningTeamId()
                                        val losingTeamId = if (teamId == 0) 1 else 0
                                        val scores = game.getAllHandsTotal()

                                        Text(
                                            buildAnnotatedString {
                                                withStyle(
                                                    style = SpanStyle(fontSize = 16.sp)
                                                ) {
                                                    append(game.match.target.teams[teamId].name)
                                                    append(" won ")
                                                    append(scores.last()[teamId].toString())
                                                    append(" to ")
                                                    append(scores.last()[losingTeamId].toString())
                                                    append(" ")
                                                }

                                                withStyle(
                                                    style = SpanStyle(
                                                        fontSize = 12.sp,
                                                        fontStyle = FontStyle.Italic
                                                    )
                                                ) {
                                                    append(
                                                        game.finished!!.format(
                                                            DateTimeFormatter.ofLocalizedDateTime(
                                                                FormatStyle.SHORT,
                                                                FormatStyle.SHORT
                                                            )
                                                        )
                                                    )
                                                }
                                            }
                                        )
                                    }
                                }
                            }

                            Column(
                                horizontalAlignment = Alignment.End,
                                verticalArrangement = Arrangement.Center
                            ) {
                                DeleteIcon(
                                    onClick = {
                                        onDeleteGameClicked(match, game)
                                    },
                                    modifier = Modifier.size(30.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun CardArrow(
    degrees: Float,
    onClick: () -> Unit
) {
    IconButton(
        onClick = onClick,
        content = {
            Icon(
                painter = painterResource(R.drawable.ic_expand_less_24),
                contentDescription = "Expandable Arrow",
                modifier = Modifier.rotate(degrees),
            )
        }
    )
}



