package com.zeroboss.scoring500.presentation.common

import androidx.compose.animation.expandHorizontally
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.zeroboss.scoring500.ui.theme.*
import com.zeroboss.scoring500.ui.theme.normalText

@Composable
fun TextFieldWithDropDown(
    modifier: Modifier = Modifier,
    text: String,
    onNameChanged: (String) -> Unit = {},
    label: String = "Test Label",
    backgroundColor: Color = Color.White,
    onTrailingIconClicked: () -> Unit,
    onFocusAltered: () -> Unit = {}
) {
    val focusRequester = FocusRequester()

    OutlinedTextField(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 5.dp)
            .focusRequester(focusRequester)
            .onFocusChanged {
                if (it.hasFocus) {
                    onFocusAltered()
                }
            },
        textStyle = normalText,
        value = text,
        onValueChange = { onNameChanged(it) },
        trailingIcon = { TrailingIcon( onTrailingIconClicked, focusRequester) },
        label = { Text(text = label) },
        colors = TextFieldDefaults.textFieldColors(
            cursorColor = Blue600,
            backgroundColor = backgroundColor,
            focusedLabelColor = Blue800,
            unfocusedLabelColor = Blue300,
            focusedIndicatorColor = Blue800,
            unfocusedIndicatorColor = Blue300,
            textColor = Blue800
        )
    )
}
