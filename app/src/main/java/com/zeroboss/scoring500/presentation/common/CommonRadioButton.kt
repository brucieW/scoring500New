package com.zeroboss.scoring500.presentation.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.zeroboss.scoring500.ui.theme.Blue800

@Composable
fun CommonRadioButton(
    modifier: Modifier = Modifier,
    text: Int? = null,
    selected: Boolean,
    onClick: () -> Unit
) {
    RadioButton(
        modifier = modifier,
        selected = selected,
        onClick = { onClick() },
        colors = RadioButtonDefaults.colors(
            selectedColor = Blue800,
            unselectedColor = Blue800
        )
    )

    Text(
        text = stringResource(text!!),
        modifier = Modifier
            .padding(end = 20.dp)
            .clickable { onClick() }
    )

}