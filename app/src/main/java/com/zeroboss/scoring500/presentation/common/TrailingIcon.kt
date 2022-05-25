package com.zeroboss.scoring500.presentation.common

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun TrailingIcon(
    onTrailingIconClicked: () -> Unit,
    focusRequester: FocusRequester
) {
    IconButton(
        onClick = {
            focusRequester.freeFocus()
            onTrailingIconClicked()
        }
    ) {
        Icon(
            Icons.Rounded.ArrowDropDown,
            contentDescription = "",
            tint = Color.Black
        )
    }
}