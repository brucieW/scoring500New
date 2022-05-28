package com.zeroboss.scoring500.presentation.screens.tutorial

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.zeroboss.scoring500.R
import com.zeroboss.scoring500.ui.theme.navigationTitle2
import com.zeroboss.scoring500.ui.theme.smallerText

@Composable
fun MainMenuDetail() {
    Row(
        modifier = Modifier.padding(start = 20.dp, end = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Image(
                painterResource(R.drawable.top_bar),
                "top_bar_image"
            )

            Image(
                painterResource(R.drawable.menu_image),
                "menu_image",
                modifier = Modifier.padding(start = 20.dp, top = 10.dp)
            )
        }

        Column(
            modifier = Modifier.padding(start = 20.dp)
        ) {
            Text(
                text = "The main menu is displayed when you click the menu icon on the top bar of the Home Screen. It enables you to set preferences, display statistics and perform some house keeping tasks. For a full description of these options, see the appropriate sections below.",
                style = smallerText
            )
        }
    }

}