package com.zeroboss.scoring500.presentation.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.rounded.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.zeroboss.scoring500.ui.theme.Blue50
import com.zeroboss.scoring500.ui.theme.Blue800
import com.zeroboss.scoring500.R
import com.zeroboss.scoring500.data.common.ActiveStatus.activeGame
import com.zeroboss.scoring500.data.common.ActiveStatus.activeMatch
import com.zeroboss.scoring500.presentation.dialogs.DeleteGameDialog
import com.zeroboss.scoring500.presentation.dialogs.DeleteMatchDialog
import com.zeroboss.scoring500.presentation.screens.destinations.GameScreenDestination
import com.zeroboss.scoring500.presentation.screens.destinations.NewHandDestination
import com.zeroboss.scoring500.presentation.screens.destinations.NewMatchScreenDestination
import com.zeroboss.scoring500.ui.theme.dialogTitle
import com.zeroboss.scoring500.ui.theme.navigationTitle2
import org.koin.androidx.compose.viewModel

@Destination
@Composable
fun HomeScreen(
    navigator: DestinationsNavigator
) {
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier.fillMaxSize(),
        topBar = { HomeAppBar(navigator) },
        content = { HomeBody(navigator) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navigator.navigate(NewMatchScreenDestination)
                },
                contentColor = Color.White,
                backgroundColor = Blue800,
            ) {
                Icon(Icons.Filled.Add, "Add Match")
            }
        },
        isFloatingActionButtonDocked = true,
        floatingActionButtonPosition = FabPosition.End,
    )
}

@Composable
fun HomeAppBar(
    navigator: DestinationsNavigator
) {
    Row(
        modifier = Modifier
            .height(58.dp)
            .background(Blue800)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painterResource(R.drawable.app_image),
            "app_image",
            modifier = Modifier
                .size(64.dp)
                .padding(start = 10.dp, end = 20.dp)
        )

        Text(
            text = stringResource(R.string.app_name),
            style = navigationTitle2,
            modifier = Modifier.weight(1f)
        )

        Box(
            Modifier
                .weight(0.2f)
                .padding(top = 5.dp)

        ) {
            DropDownMenu(
                navigator
            )
        }

        IconButton(
            modifier = Modifier.weight(0.2f),
            onClick = { Runtime.getRuntime().exit(0) }
        ) {
            Icon(
                Icons.Rounded.Close,
                contentDescription = "Close Application",
                tint = Color.White,
                modifier = Modifier.padding(end = 10.dp)
            )
        }
    }
}


@Composable
fun HomeBody(
    navigator: DestinationsNavigator
) {
    val homeViewModel by viewModel<HomeViewModel>()
    val state by homeViewModel.homeState
    val lazyListState = rememberLazyListState()

    DeleteMatchDialog(homeViewModel)
    DeleteGameDialog(homeViewModel)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Blue50)
            .padding(bottom = 5.dp)
    ) {
        if (state.matches.isNotEmpty()) {
            Text(
                text = stringResource(R.string.match_history),
                style = dialogTitle,
                modifier = Modifier
                    .padding(top = 10.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            if (state.matches.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = stringResource(R.string.no_match_records),
                        textAlign = TextAlign.Center,
                        style = typography.h5,
                        modifier = Modifier.padding(start = 40.dp, end = 40.dp, bottom = 40.dp)
                    )

                    Box(
                        Modifier
                            .rotate(20F)
                            .padding(start = 50.dp, end = 100.dp, top = 40.dp)
                    ) {
                        Image(
                            painterResource(R.drawable.arrow),
                            contentDescription = null
                        )
                    }
                }
            } else {
                LazyColumn(
                    state = lazyListState,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 30.dp),
                    contentPadding = PaddingValues(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(state.matches) { matchCard ->
                        ExpandableMatchCard(
                            matchCard,
                            onCardArrowClicked = {
                                homeViewModel.onEvent(HomeEvent.CardArrowClicked(matchCard))
                            },

                            onNewGameClicked = {
                                homeViewModel.onEvent(HomeEvent.CreateNewGame(matchCard))
                                navigator.navigate(NewHandDestination)
                            },

                            onDeleteMatchClicked = {
                                activeMatch = matchCard
                                homeViewModel.onEvent(HomeEvent.ShowDeleteMatchDialog(true))
                            },

                            onGameClicked = { game ->
                                activeMatch = matchCard
                                activeGame = game
                                navigator.navigate(if (game.hands.isEmpty()) NewHandDestination else GameScreenDestination)
                            },

                            onEditGamesClicked = {
                                activeMatch = matchCard
                                navigator.navigate(GameScreenDestination)
                            },

                            onDeleteGameClicked = { match, game ->
                                activeMatch = match
                                activeGame = game
                                homeViewModel.onEvent(HomeEvent.ShowDeleteGameDialog(true))
                            },

                            expanded = state.expandedCard == matchCard
                        )
                    }
                }
            }
        }
    }
}

