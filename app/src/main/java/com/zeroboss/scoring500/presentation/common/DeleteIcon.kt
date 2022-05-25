package com.zeroboss.scoring500.presentation.common

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun DeleteIcon(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onClick,
        modifier = modifier,
        content = {
            Icon(
                Icons.Rounded.Delete,
                contentDescription = null
            )
        }
    )
}
