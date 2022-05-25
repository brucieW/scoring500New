package com.zeroboss.scoring500.presentation.screens.login

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.annotation.Destination
import com.zeroboss.scoring500.ui.theme.Blue300
import com.zeroboss.scoring500.ui.theme.Blue600
import com.zeroboss.scoring500.ui.theme.Blue800
import com.zeroboss.scoring500.R
import com.zeroboss.scoring500.ui.theme.dialogTitle
import com.zeroboss.scoring500.ui.theme.normalText
import org.koin.androidx.compose.viewModel

@Destination
@Composable
fun LoginScreen(
) {
    val loginViewModel by viewModel<LoginViewModel>()
    val state by loginViewModel.state

    val defaults = TextFieldDefaults.textFieldColors(
        cursorColor = Blue600,
        backgroundColor = Color.White,
        focusedLabelColor = Blue800,
        unfocusedLabelColor = Blue300,
        focusedIndicatorColor = Blue800,
        unfocusedIndicatorColor = Blue300,
        textColor = Blue800
    )

    val email by loginViewModel.email
    val password by loginViewModel.password

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(80.dp))

            Text(
                text = stringResource(id = R.string.login),
                style = dialogTitle
            )

            Spacer(modifier = Modifier.height(30.dp))

            OutlinedTextField(
                textStyle = normalText,
                value = email,
                onValueChange = { loginViewModel.onEvent(LoginEvent.EmailChanged(it)) },
                label = { Text(text = stringResource(R.string.email)) },
                colors = defaults
            )

            Spacer(modifier = Modifier.width(20.dp))

            OutlinedTextField(
                textStyle = normalText,
                value = password,
                onValueChange = { loginViewModel.onEvent(LoginEvent.PasswordChanged(it)) },
                label = { Text(text = stringResource(R.string.password)) },
                colors = defaults,
                trailingIcon = {
                    IconButton(
                        onClick = { loginViewModel.onEvent(LoginEvent.TogglePasswordVisibility) }
                    ) {
                        Icon(
                            if (state.passwordVisible) Icons.Outlined.Visibility
                                else Icons.Outlined.VisibilityOff, contentDescription = "password visible")
                    }
                },
                visualTransformation = if (state.passwordVisible) VisualTransformation.None
                                                else PasswordVisualTransformation()
            )
        }
    }
}
