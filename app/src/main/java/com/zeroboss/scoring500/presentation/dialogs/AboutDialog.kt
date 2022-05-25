package com.zeroboss.scoring500.presentation.dialogs

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.zeroboss.scoring500.ui.theme.*
import com.zeroboss.scoring500.R
import com.zeroboss.scoring500.presentation.common.ScoringButton
import com.zeroboss.scoring500.presentation.common.ShowDialogText
import com.zeroboss.scoring500.presentation.screens.home.HomeViewModel
import com.zeroboss.scoring500.presentation.screens.home.MenuEvent
import com.zeroboss.scoring500.ui.theme.normalText
import com.zeroboss.scoring500.ui.theme.typography

@Composable
fun AboutDialog(
    homeViewModel: HomeViewModel
) {
    Dialog(
        onDismissRequest = { homeViewModel.onMenuEvent(MenuEvent.ShowAboutDialog(false)) }
    ) {
        Card(
            modifier = Modifier.size(340.dp, 380.dp),
            shape = RoundedCornerShape(20.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                ) {
                    Image(
                        painter = painterResource(R.drawable.player),
                        contentDescription = "",
                        modifier = Modifier.fillMaxWidth(fraction = .35F)
                    )

                    ShowDialogText(
                        style = typography.h4,
                        topPadding = 0.dp,
                        text = R.string.app_name
                    )
                }

                ShowDialogText(
                    style = normalText,
                    text = R.string.about_text
                )

                ShowDialogText(
                    topPadding = 20.dp,
                    text = R.string.version
                )

                ShowDialogText(
                    topPadding = 20.dp,
                    text = R.string.programmed_by
                )

                ShowDialogText(
                    topPadding = 0.dp,
                    text = R.string.copyright
                )

                ScoringButton(
                    modifier = Modifier.padding(top = 30.dp),
                    text = stringResource(id = R.string.close),
                    onClick = { homeViewModel.onMenuEvent(MenuEvent.ShowAboutDialog(false)) }
                )
            }
        }
    }
}


