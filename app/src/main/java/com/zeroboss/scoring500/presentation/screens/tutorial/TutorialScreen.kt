package com.zeroboss.scoring500.presentation.screens.tutorial

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.runtime.*
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
import com.zeroboss.scoring500.R
import com.zeroboss.scoring500.data.common.ActiveStatus
import com.zeroboss.scoring500.presentation.common.TopPanel
import com.zeroboss.scoring500.presentation.dialogs.DeleteGameDialog
import com.zeroboss.scoring500.presentation.dialogs.DeleteMatchDialog
import com.zeroboss.scoring500.presentation.screens.destinations.GameScreenDestination
import com.zeroboss.scoring500.presentation.screens.destinations.NewHandDestination
import com.zeroboss.scoring500.presentation.screens.home.*
import com.zeroboss.scoring500.presentation.screens.home.HomeAppBar
import com.zeroboss.scoring500.presentation.screens.home.HomeBody
import com.zeroboss.scoring500.ui.theme.Blue50
import com.zeroboss.scoring500.ui.theme.Blue800
import com.zeroboss.scoring500.ui.theme.navigationTitle2
import kotlinx.serialization.json.JsonNull.content
import org.koin.androidx.compose.viewModel

@Destination
@Composable
fun TutorialScreen(
    navigator: DestinationsNavigator
) {
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopPanel(
                R.string.tutorial,
                onClickReturn = { navigator.popBackStack() }
            )
        },
        content = { TutorialBody(navigator) }
    )
}

@Composable
fun TutorialBody(
    navigator: DestinationsNavigator
) {
    var state by remember { mutableStateOf(TutorialItem.None) }
    val lazyListState = rememberLazyListState()

    LazyColumn(
        state = lazyListState,
        modifier = Modifier
            .fillMaxSize()
            .background(Blue50)

    ) {
        items(TutorialItem.values()) { item ->
            if (item != TutorialItem.None) {
                ExpandableTutorialItem(
                    item,
                    onArrowClicked = {
                       item -> state = if (item == state) TutorialItem.None else item
                    },
                    expanded = item == state
                )
            }
        }
    }
}
