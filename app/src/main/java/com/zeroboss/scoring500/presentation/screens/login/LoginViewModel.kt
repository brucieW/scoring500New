package com.zeroboss.scoring500.presentation.screens.login

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {

    private val _state = mutableStateOf(LoginState())
    val state = _state

    var email = mutableStateOf("")

    var password = mutableStateOf("")

    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.EmailChanged -> {
                email.value = event.email
                checkValid()
            }

            is LoginEvent.PasswordChanged -> {
                password.value = event.password
                checkValid()
            }

            is LoginEvent.TogglePasswordVisibility -> {
                _state.value = state.value.copy(
                    passwordVisible = !state.value.passwordVisible
                )
            }

        }
    }

    private fun checkValid() {
        _state.value = state.value.copy(isValid = false)
    }
}